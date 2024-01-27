package com.tulip.authorizationserver.config

import com.nimbusds.jose.jwk.JWKSelector
import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import com.nimbusds.jose.jwk.source.JWKSource
import com.nimbusds.jose.proc.SecurityContext
import com.tulip.authorizationserver.User
import com.tulip.authorizationserver.UserRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configurers.ExceptionHandlingConfigurer
import org.springframework.security.oauth2.core.AuthorizationGrantType
import org.springframework.security.oauth2.core.ClientAuthenticationMethod
import org.springframework.security.oauth2.core.oidc.OidcScopes
import org.springframework.security.oauth2.jwt.JwtDecoder
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration
import java.util.*

@Configuration
@EnableWebSecurity
class SecurityConfiguration(private val repository: UserRepository) {

    @Value("\${spring.authorization-server.clients.web-client.login-redirect}")
    lateinit var webRedirect: String

    @Value("\${spring.authorization-server.clients.web-client.silent-refresh-redirect}")
    lateinit var webSilentRefreshRedirect: String

    @Value("\${spring.authorization-server.issuer}")
    lateinit var issuer: String

    @Value("\${spring.authorization-server.clients.web-client.origin}")
    lateinit var webClientOrigin: String

    @Value("\${spring.authorization-server.token-ttl}")
    lateinit var tokenTTL: String

    @Bean
    @Order(1)
    fun authorizationServerSecurityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http)
        http.cors(Customizer.withDefaults()).csrf { it.disable() }
        http.getConfigurer(OAuth2AuthorizationServerConfigurer::class.java)
            .oidc(Customizer.withDefaults())
        http.authenticationProvider(AuthenticationProvider(repository))
        http.exceptionHandling { exceptions: ExceptionHandlingConfigurer<HttpSecurity?> ->
            exceptions.defaultAuthenticationEntryPointFor(
                LoginUrlAuthenticationEntryPoint("/login"),
                MediaTypeRequestMatcher(MediaType.TEXT_HTML))
        }
        return http.build()
    }

    @Bean
    @Order(2)
    fun defaultSecurityFilterChain(http: HttpSecurity): SecurityFilterChain? {
        http.authorizeHttpRequests { authorize -> authorize
            .requestMatchers("/images/**").permitAll()
            .requestMatchers("/css/**").permitAll()
            .anyRequest().authenticated()
        }
            .cors(Customizer.withDefaults())
            .formLogin {
                it.loginPage("/login").permitAll().defaultSuccessUrl(webRedirect)
            }
            .logout {
                it.invalidateHttpSession(true)
                    .deleteCookies("JSESSIONID")
            }
        return http.build()
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val source = UrlBasedCorsConfigurationSource()
        val config = CorsConfiguration()
        config.addAllowedHeader("*")
        config.addAllowedMethod("*")
        config.addAllowedOrigin(webClientOrigin)
        config.allowCredentials = true
        source.registerCorsConfiguration("/**", config)
        return source
    }

    @Bean
    fun tokenSettings(): TokenSettings? {
        return TokenSettings.builder()
            .accessTokenTimeToLive(Duration.ofMinutes(tokenTTL.toLong()))
            .build()
    }

    @Bean
    fun jwtTokenCustomizer(): OAuth2TokenCustomizer<JwtEncodingContext>? {
        return OAuth2TokenCustomizer { context: JwtEncodingContext ->
            if (OAuth2TokenType.ACCESS_TOKEN == context.tokenType) {
                val authentication = context.getPrincipal() as UsernamePasswordAuthenticationToken
                val loggedInUser = authentication.principal as User
                loggedInUser.let { user ->
                    context.claims
                        .claims { claims: MutableMap<String?, Any?> ->
                            claims["rights"] = user.getRights()
                            claims["tenant"] = user.getTenant()
                        }
                }
            }
        }
    }

    @Bean
    fun registeredClientRepository(): RegisteredClientRepository {
        val webClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("web-client")
            .clientAuthenticationMethod(ClientAuthenticationMethod.NONE)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .tokenSettings(tokenSettings())
            .redirectUri(webRedirect)
            .redirectUri(webSilentRefreshRedirect)
            .postLogoutRedirectUri(webRedirect)
            .scope(OidcScopes.OPENID)
            .clientSettings(
                ClientSettings.builder()
                    .requireProofKey(true)
                    .build())
            .build()
        return InMemoryRegisteredClientRepository(webClient)
    }

    @Bean
    fun providerSettings(): AuthorizationServerSettings =
        AuthorizationServerSettings
            .builder()
            .issuer(issuer)
            .build()

    /* ------------------------------------------------------------------------------------------------------------- */

    @Bean
    fun jwtDecoder(jwkSource: JWKSource<SecurityContext>): JwtDecoder =
        OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource)

    @Bean
    fun jwkSource(): JWKSource<SecurityContext?>? {
        val jwkSet = JWKSet(generateRsa())
        return JWKSource { jwkSelector: JWKSelector, _: SecurityContext? -> jwkSelector.select(jwkSet)
        }
    }

    private fun generateRsa(): RSAKey {
        val keyPair: KeyPair = generateRsaKey()
        val publicKey = keyPair.public as RSAPublicKey
        val privateKey = keyPair.private as RSAPrivateKey

        return RSAKey.Builder(publicKey)
            .privateKey(privateKey)
            .keyID(UUID.randomUUID().toString())
            .build()
    }

    private fun generateRsaKey(): KeyPair {
        val keyPairGenerator = KeyPairGenerator.getInstance("RSA")
        keyPairGenerator.initialize(2048)
        return keyPairGenerator.generateKeyPair()
    }
}
