package com.tulip.resourceserver.company

import com.tulip.resourceserver.banks.BankAccountCompany
import com.tulip.resourceserver.banks.BankAccountCompanyPK
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface CompanyBankRepository : CrudRepository<BankAccountCompany, BankAccountCompanyPK> {

    fun findByJib(jib: String): List<BankAccountCompany>
}
