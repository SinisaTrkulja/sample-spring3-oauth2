package com.tulip.authorizationserver

import jakarta.persistence.*
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

@Entity
@Table(name = "USER")
class User(@Id
           @GeneratedValue
           private var id: UUID = UUID.randomUUID(),
           private var tenant: String = "",
           private var firstName: String = "",
           private var lastName: String = "",
           private var email :String = "",
           private var password: String = "",
           @Enumerated(EnumType.STRING)
           private var role: Role = Role.USER,
           private var rights: Int = 1) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return mutableListOf(SimpleGrantedAuthority(role.name))
    }

    override fun getPassword(): String = password
    override fun getUsername(): String = email
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true
    fun getRights(): Int = rights
    fun getTenant(): String = tenant

    enum class Role {
        USER, ADMIN
    }
}
