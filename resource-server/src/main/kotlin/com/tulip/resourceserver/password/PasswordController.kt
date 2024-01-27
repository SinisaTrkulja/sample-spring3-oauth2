package com.tulip.resourceserver.password

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class PasswordController(private val passwordService: PasswordService) {

    @Operation(summary = "Password change", description = "Change current logged in user's password.")
    @ApiResponse(responseCode = "204", description = "Password changed successfully")
    @PutMapping("/password")
    fun changePassword(@Parameter(description = "Previous and new password in plaintext", required = true)
                       @RequestBody passwords: Passwords): ResponseEntity<Void> {
        passwordService.changePassword(passwords)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
