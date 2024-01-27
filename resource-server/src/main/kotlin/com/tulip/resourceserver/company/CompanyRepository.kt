package com.tulip.resourceserver.company

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface CompanyRepository: JpaRepository<Company, String> {

    @Query("""
        SELECT c FROM Company c
        WHERE (:active IS NULL OR c.active = :active) 
        AND (:jib IS NULL OR c.jib = :jib)""")
    fun find(active: Boolean?, jib: String?, pageable: Pageable): Page<Company>
}
