package com.tulip.resourceserver.employee

import com.tulip.resourceserver.banks.BankAccountEmployee
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface EmployeeBankRepository : CrudRepository<BankAccountEmployee, UInt> {
}
