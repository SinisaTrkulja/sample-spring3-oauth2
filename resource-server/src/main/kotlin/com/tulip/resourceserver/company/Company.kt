package com.tulip.resourceserver.company

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tulip.resourceserver.banks.BankAccountCompany
import com.tulip.resourceserver.employee.BHEntity
import jakarta.persistence.*
import org.hibernate.annotations.TenantId
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited

@Entity
@Table(name = "COMPANY")
@Audited
class Company(
    @Id @Column(name = "jib")
    var jib: String,

    @TenantId
    @JsonIgnore
    var tenant: String,

    var name: String,

    @Column(nullable = false)
    var pib: UInt,

    @Column(nullable = false)
    var country: String,

    @Column(nullable = false)
    var city: String,

    @Column(nullable = false)
    var municipality: String,

    @Column(nullable = false)
    var streetName: String,

    @Column(nullable = false)
    var streetNumber: Short,

    @Column(nullable = false)
    var zip: Int,

    @Column(nullable = false)
    var entity: BHEntity,
    var typeOfCompany: CompanyType,

    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JoinColumn(name = "jib")
    @NotAudited
    var bankAccounts: List<BankAccountCompany>,

    var phoneNumber: String,
    var email: String,
    var inPDV: Boolean,
    var owner: String?,
    var representative: String?,

    @Column(columnDefinition = "boolean default true")
    var active: Boolean = true
)

enum class CompanyType {
    SP, DOO, DNO, KD, DD
}


