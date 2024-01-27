package com.tulip.resourceserver.auxiliary

import org.hibernate.cfg.AvailableSettings
import org.hibernate.context.spi.CurrentTenantIdentifierResolver
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer
import org.springframework.stereotype.Component


@Component
class TenantIdentifierResolver() : CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private var currentTenant: String = "none"

    fun setCurrentTenant(tenant: String) {
        currentTenant = tenant
    }

    override fun resolveCurrentTenantIdentifier(): String = currentTenant

    override fun validateExistingCurrentSessions(): Boolean = false

    override fun customize(hibernateProperties: MutableMap<String, Any>) {
        hibernateProperties[AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER] = this
    }
}
