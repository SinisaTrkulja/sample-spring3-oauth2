package com.tulip.resourceserver.employee

import com.fasterxml.jackson.annotation.JsonIgnore
import com.tulip.resourceserver.banks.BankAccountEmployee
import com.tulip.resourceserver.company.Company
import jakarta.persistence.*
import org.hibernate.annotations.TenantId
import org.hibernate.envers.Audited
import java.util.*

@Entity
@Table(name = "EMPLOYEE")
@Audited
class Employee(
    @Id @Column(name = "employee_jmb")
    val jmb: String,

    @TenantId
    @JsonIgnore
    var tenant: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "jib")
    var company: Company,

    @Column(nullable = false)
    var firstName: String,

    @Column(nullable = false)
    var lastName: String,

    @Column(nullable = false)
    var fathersName: String,

    @Column(nullable = false)
    var gender: Gender,

    @Column(nullable = false)
    var dateOfBirth: Date,

    @Column(nullable = false)
    var placeOfBirth: String,

    @Column(nullable = false)
    var countryOfBirth: String,

    @Column(nullable = false)
    var citizenship: String,

    var workPermit: String,

    @Column(nullable = false)
    var education: Education,

    var professionalExamTaken: Boolean,

    @Column(nullable = false)
    var startOfContract: Date,

    var endOfContract: Date,

    @Column(nullable = false)
    var country: String,

    @Column(nullable = false)
    var city: String,

    @Column(nullable = false)
    var streetName: String,

    @Column(nullable = false)
    var streetNumber: Short,

    @Column(nullable = false)
    var zip: UInt,

    @Column(nullable = false)
    var entity: BHEntity,

    var placeOfResidency: String,

    @Column(nullable = false)
    var position: String,

    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_jmb")
    var bankAccount: BankAccountEmployee,

    var agreedGrossPay: Double,

    @Column(nullable = false)
    var contractType: ContractType,

    @Column(nullable = false)
    var placeOfWork: String,

    var telephone: String,

    var active: Boolean
)

enum class Education {
    NK, PKV, KV, SSS, VKV, VSHS, VSS
}

enum class BHEntity {
    FBIH, RS, DB
}

enum class Gender {
    MALE, FEMALE
}

enum class ContractType {
    MANAGEMENT,
    AUTHORSHIP,
    SHORT_TERM,
    LONG_TERM,
    TRIAL
}

