package com.tulip.resourceserver.hours

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface HoursRepository : JpaRepository<Hours, HoursPK> {

    @Query("""
        SELECT h from Hours h 
        WHERE (:jmb IS NULL OR h.jmb = :jmb) 
        AND (:month IS NULL OR h.month = :month) 
        AND (h.year = :year)""")
    fun find(jmb: String?, month: Short?, year: Short, pageable: Pageable): Page<Hours>
}
