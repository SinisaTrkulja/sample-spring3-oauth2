package com.tulip.authorizationserver.config

import com.tulip.authorizationserver.UserRepository
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.bcrypt.BCrypt
import java.util.*

class AuthenticationProvider(private val repository: UserRepository) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val email = authentication.principal as String
        val password = authentication.credentials as String
        val user = repository.findByEmail(email).orElseThrow { BadCredentialsException("Username or password incorrect!") }

        if (!BCrypt.checkpw(password, user.password)) throw BadCredentialsException("Username or password incorrect!")

        return CustomToken(
            user.username,
            user.getTenant(),
            user.getRights(),
            authentication.principal,
            authentication.credentials,
            authentication.authorities)
    }

    override fun supports(authentication: Class<*>): Boolean {
        return authentication == UsernamePasswordAuthenticationToken::class.java
    }
}
