package com.tulip.resourceserver.banks

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.TenantId
import org.hibernate.envers.Audited

@Entity
@Table(name = "BANK_ACCOUNT_EMPLOYEE")
@Audited
class BankAccountEmployee(@Id
                          var jmb: String,
                          @TenantId
                          @JsonIgnore
                          var tenant: String,
                          var accountName: String,
                          var bankName: String,
                          var accountNumber: UInt,
                          var iban: String)
