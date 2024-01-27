package com.tulip.resourceserver.employee

import com.tulip.resourceserver.company.CompanyRepository
import jakarta.transaction.Transactional
import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.IOException

@Service
@Transactional
class EmployeeService(private val employeeRepository: EmployeeRepository,
                      private val companyRepository: CompanyRepository
) {
    fun getEmployees(active: Boolean?, jmb: String?, pageable: Pageable): Page<Employee> = employeeRepository.find(active, jmb, pageable)

    fun addEmployee(employee: Employee) {
        if (employeeRepository.existsById(employee.jmb)) throw DuplicateKeyException("Employee already exists!")
        if (!companyRepository.existsById(employee.company.jib)) throw IllegalArgumentException("Company with given id doesn't exist!")
        if (employee.jmb != employee.bankAccount.jmb) throw IllegalArgumentException("Employee JMB doesn't match the JMB in the bank account!")
        employeeRepository.save(employee)
    }

    fun editEmployee(newEmployeeData: Employee) {
        val existingEmployee: Employee = employeeRepository.findById(newEmployeeData.jmb).orElseThrow {
            IOException("The requested employee doesn't exist!")
        }
        existingEmployee.firstName = newEmployeeData.firstName
        existingEmployee.lastName = newEmployeeData.lastName
        existingEmployee.fathersName = newEmployeeData.fathersName
        existingEmployee.gender = newEmployeeData.gender
        existingEmployee.dateOfBirth = newEmployeeData.dateOfBirth
        existingEmployee.placeOfBirth = newEmployeeData.placeOfBirth
        existingEmployee.countryOfBirth = newEmployeeData.countryOfBirth
        existingEmployee.citizenship = newEmployeeData.citizenship
        existingEmployee.workPermit = newEmployeeData.workPermit
        existingEmployee.education = newEmployeeData.education
        existingEmployee.professionalExamTaken = newEmployeeData.professionalExamTaken
        existingEmployee.startOfContract = newEmployeeData.startOfContract
        existingEmployee.endOfContract = newEmployeeData.endOfContract
        existingEmployee.country = newEmployeeData.country
        existingEmployee.city = newEmployeeData.city
        existingEmployee.streetName = newEmployeeData.streetName
        existingEmployee.streetNumber = newEmployeeData.streetNumber
        existingEmployee.zip = newEmployeeData.zip
        existingEmployee.entity = newEmployeeData.entity
        existingEmployee.placeOfResidency = newEmployeeData.placeOfResidency
        existingEmployee.position = newEmployeeData.position
        existingEmployee.bankAccount = newEmployeeData.bankAccount
        existingEmployee.agreedGrossPay = newEmployeeData.agreedGrossPay
        existingEmployee.contractType = newEmployeeData.contractType
        existingEmployee.placeOfWork = newEmployeeData.placeOfWork
        existingEmployee.telephone = newEmployeeData.telephone
        existingEmployee.active = newEmployeeData.active
        employeeRepository.save(existingEmployee)
    }

    fun getEmployeesOverview(active: Boolean?, jmb: String?, pageable: Pageable): Page<EmployeeOverview> = employeeRepository.getEmployeesOverview(active, jmb, pageable)
}
