package com.tulip.resourceserver.banks

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.*
import org.hibernate.annotations.TenantId
import org.hibernate.envers.Audited
import java.util.*

@Entity
@Table(name = "BANK_ACCOUNT_COMPANY")
@IdClass(BankAccountCompanyPK::class)
@Audited
class BankAccountCompany(@Id @Column(name = "jib")
                         var jib: String,
                         @Id
                         var accountNumber: UInt,
                         @TenantId
                         @JsonIgnore
                         var tenant: String,
                         var accountName: String,
                         var bankName: String,
                         var iban: String) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BankAccountCompany

        if (jib != other.jib) return false
        if (accountNumber != other.accountNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = jib.hashCode()
        result = 31 * result + accountNumber.hashCode()
        return result
    }
}
