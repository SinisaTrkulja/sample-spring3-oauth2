package com.tulip.resourceserver.hours

import java.io.Serializable

class HoursPK(var jmb: String,
              var day: Short,
              var month: Short,
              var year: Short) : Serializable {
    constructor() : this("", 0, 0, 0)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as HoursPK

        if (jmb != other.jmb ||
            day != other.day ||
            month != other.month ||
            year != other.year) return false

        return true
    }

    override fun hashCode(): Int {
        var result = jmb.hashCode()
        result = 31 * result + day
        result = 31 * result + month
        result = 31 * result + year
        return result
    }
}
