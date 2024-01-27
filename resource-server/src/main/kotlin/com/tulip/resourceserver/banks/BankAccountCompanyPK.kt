package com.tulip.resourceserver.banks

import java.io.Serializable


class BankAccountCompanyPK(val jib: String, val accountNumber: UInt) : Serializable {
    constructor() : this("", 0U)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BankAccountCompanyPK

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
