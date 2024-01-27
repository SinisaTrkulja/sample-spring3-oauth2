package com.tulip.authorizationserver.config

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.GrantedAuthority

data class CustomToken(val username: String,
                       val tenant: String,
                       val rights: Int,
                       private val principal: Any,
                       private val credentials: Any,
                       private val authorities: Collection<GrantedAuthority> ) : UsernamePasswordAuthenticationToken(principal, credentials, authorities)

