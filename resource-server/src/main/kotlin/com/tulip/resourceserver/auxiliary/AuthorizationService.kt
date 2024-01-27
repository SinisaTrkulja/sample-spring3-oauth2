package com.tulip.resourceserver.auxiliary

import org.springframework.security.authentication.InsufficientAuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service

@Service("AS")
class AuthorizationService(private val tenantIdentifierResolver: TenantIdentifierResolver) {

    fun hasRights(requiredRight: AccessRights): Boolean {
        val jwtAuthenticationToken = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val claims = jwtAuthenticationToken.token.claims as Map<String, Any>
        val userRights = claims["rights"] as Long
        if (!littleBitFunny(userRights, requiredRight.value)) throw InsufficientAuthenticationException("Insufficient rights!")

        val tenant = claims["tenant"] as String
        tenantIdentifierResolver.setCurrentTenant(tenant)
        return true
    }

    /**
     For the rare ones among you who will go wandering through this codebase and still not
     know what this marvelous oneliner does, the following is an explanation in the simplest terms:

     A user is authorized to access an endpoint if and only if there is a 1 set in the binary representation
     of the integer that represent his access rights at the bitToShift plus one's place reading from right to left.

     If that's the case then a series of bitsToShift right bit shifts yields an odd number which is the only type of
     number that will return the number 1 when put through an and operation with the number 1.
     */
    private fun littleBitFunny(userRights: Long, bitToShift: Int): Boolean {
        return (userRights shr bitToShift) and 1 == 1L
    }
}
