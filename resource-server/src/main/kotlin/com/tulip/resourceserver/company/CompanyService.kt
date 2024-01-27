package com.tulip.resourceserver.company

import com.tulip.resourceserver.banks.BankAccountCompany
import com.tulip.resourceserver.banks.BankAccountCompanyPK
import jakarta.transaction.Transactional
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@Transactional
class CompanyService(private val companyRepository: CompanyRepository,
                     private val bankRepository: CompanyBankRepository
) {

    fun getCompanies(active: Boolean?, jib: String?, pageable: Pageable): Page<Company> = companyRepository.find(active, jib, pageable)

    fun addCompany(company: Company) {
        if (companyRepository.existsById(company.jib)) throw DuplicateKeyException("Company with given JIB already exists!")
        for (bankAccount in company.bankAccounts) {
            if (bankAccount.jib != company.jib) throw IOException("Bank account's jib doesn't match the company's jib!")
        }
        companyRepository.save(company)
    }

    fun editCompany(newCompanyData: Company) {
        val existingCompany: Company = companyRepository.findById(newCompanyData.jib)
            .orElseThrow { IOException("The requested company doesn't exist!") }

        existingCompany.name = newCompanyData.name
        existingCompany.pib = newCompanyData.pib
        existingCompany.country = newCompanyData.country
        existingCompany.city = newCompanyData.city
        existingCompany.municipality = newCompanyData.municipality
        existingCompany.streetName = newCompanyData.streetName
        existingCompany.streetNumber = newCompanyData.streetNumber
        existingCompany.zip = newCompanyData.zip
        existingCompany.entity = newCompanyData.entity
        existingCompany.typeOfCompany = newCompanyData.typeOfCompany
        updateBankAccounts(existingCompany.jib, newCompanyData.bankAccounts)
        existingCompany.phoneNumber = newCompanyData.phoneNumber
        existingCompany.email = newCompanyData.email
        existingCompany.inPDV = newCompanyData.inPDV
        existingCompany.owner = newCompanyData.owner
        existingCompany.representative = newCompanyData.representative
        existingCompany.active = newCompanyData.active
        companyRepository.save(existingCompany)
    }

    fun updateBankAccounts(jib: String, newBankAccounts: List<BankAccountCompany>) {
        for (bankAccount in newBankAccounts) {
            if (bankAccount.jib != jib) throw IOException("Bank account's owner JIB doesn't match the company JIB!")
        }
        if (newBankAccounts.distinct().count() != newBankAccounts.size) throw IOException("New bank account list contains duplicates!")
        val existingBankAccounts: List<BankAccountCompany> = bankRepository.findByJib(jib)
        for (bankAccount in existingBankAccounts) {
            if (!newBankAccounts.contains(bankAccount)) bankRepository.delete(bankAccount)
        }
        for (bankAccount in newBankAccounts) {
            if (!exists(bankAccount)) bankRepository.save(bankAccount)
            else editBankAccount(bankAccount)
        }
    }

    private fun exists(bankAccount: BankAccountCompany): Boolean {
        val id = BankAccountCompanyPK(bankAccount.jib, bankAccount.accountNumber)
        return bankRepository.existsById(id)
    }

    private fun editBankAccount(newBankAccount: BankAccountCompany) {
        val id = BankAccountCompanyPK(newBankAccount.jib, newBankAccount.accountNumber)
        val existingBankAccount: BankAccountCompany = bankRepository.findById(id).get()

        existingBankAccount.accountName = newBankAccount.accountName
        existingBankAccount.bankName = newBankAccount.bankName
        existingBankAccount.iban = newBankAccount.iban
    }
}
