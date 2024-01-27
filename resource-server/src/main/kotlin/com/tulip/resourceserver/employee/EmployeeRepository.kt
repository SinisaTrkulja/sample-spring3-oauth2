package com.tulip.resourceserver.employee

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface EmployeeRepository : JpaRepository<Employee, String> {
    @Query("""
        SELECT NEW com.tulip.resourceserver.employee.EmployeeOverview(
            e.firstName,  
            e.lastName,  
            e.telephone,  
            e.city,  
            e.streetName,  
            e.streetNumber,  
            e.zip, 
            e.entity,  
            e.position,  
            e.contractType) FROM Employee e  
            WHERE (:active IS NULL OR e.active = :active)  
            AND (:jmb IS NULL OR e.jmb = :jmb) """)
    fun getEmployeesOverview(active: Boolean?, jmb: String?, pageable: Pageable): Page<EmployeeOverview>

    @Query("""  
        SELECT e FROM Employee e 
        WHERE (:active IS NULL OR e.active = :active)
        AND (:jmb IS NULL OR e.jmb = :jmb) """)
    fun find(active: Boolean?, jmb: String?, pageable: Pageable): Page<Employee>
}
