package com.tulip.resourceserver.password

import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository : CrudRepository<User, Int> {
    fun findByEmail(email: String): Optional<User>
}
