package com.tulip.resourceserver.employee

data class EmployeeOverview(val firstName: String,
                            val lastName: String,
                            val telephone: String,
                            val city: String,
                            val streetName: String,
                            val streetNumber: Short,
                            val zip: Int,
                            val entity: BHEntity,
                            val position: String,
                            val contractType: ContractType
)

