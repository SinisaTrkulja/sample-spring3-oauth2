package com.tulip.resourceserver.password

import jakarta.transaction.Transactional
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@Transactional
class PasswordService(private val userRepository: UserRepository) {

    fun changePassword(passwords: Passwords) {
        if (passwords.oldPassword == passwords.newPassword)
            throw IOException("Old and new password shouldn't match!")
        val jwtAuthenticationToken = SecurityContextHolder.getContext().authentication as JwtAuthenticationToken
        val username = jwtAuthenticationToken.token.subject
        val user: User = userRepository.findByEmail(username).get()
        if (!BCrypt.checkpw(passwords.oldPassword, user.password))
            throw IOException("Given password doesn't match user's password!")
        user.password = BCryptPasswordEncoder().encode(passwords.newPassword)
        userRepository.save(user)
    }
}
