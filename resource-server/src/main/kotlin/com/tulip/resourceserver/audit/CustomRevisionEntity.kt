package com.tulip.resourceserver.audit

import jakarta.persistence.Entity
import jakarta.persistence.Table
import org.hibernate.envers.DefaultRevisionEntity
import org.hibernate.envers.RevisionEntity

@Entity
@RevisionEntity(com.tulip.resourceserver.audit.CustomRevisionListener::class)
@Table(name = "revinfo")
class CustomRevisionEntity : DefaultRevisionEntity() {
    var author: String? = null
}
