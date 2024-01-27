package com.tulip.resourceserver.hours

import com.fasterxml.jackson.annotation.JsonIgnore
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.IdClass
import jakarta.persistence.Table
import org.hibernate.annotations.TenantId
import org.hibernate.envers.Audited

@Entity
@Table(name = "HOURS")
@IdClass(HoursPK::class)
@Audited
class Hours(@Id var jmb: String,
            @Id var day: Short,
            @Id var month: Short,
            @Id var year: Short,
            @TenantId
            @JsonIgnore
            var tenant: String?,
            var hours: Short,
            var hoursType: String,
            var additionalInfo: String)

