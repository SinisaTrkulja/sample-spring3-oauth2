package com.tulip.resourceserver.audit

import org.hibernate.envers.RevisionListener
import org.springframework.security.core.context.SecurityContextHolder

class CustomRevisionListener : RevisionListener {

    override fun newRevision(revisionEntity: Any) {
        val customRevisionEntity = revisionEntity as CustomRevisionEntity
        val author = SecurityContextHolder.getContext().authentication.name
        customRevisionEntity.author = author
    }
}
