package com.tulip.resourceserver.employee

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
class EmployeeController(private val employeeService: EmployeeService) {

    @Operation(summary = "Get employees", description = "Get a list of employees.")
    @ApiResponse(responseCode = "200", description = "List of employees retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Employee::class))])
    @GetMapping("/employees")
    @PreAuthorize("@AS.hasRights('GET_EMPLOYEES')")
    fun getEmployees(
        @Parameter(description = "Filter employees by active status")
        @RequestParam(required = false) active: Boolean?,

        @Parameter(description = "Filter employees by JMB number")
        @RequestParam(required = false) jmb: String?,

        @Parameter(description = "Paging and sorting parameters")
        @PageableDefault(page = 0, size = 25, sort = ["jmb"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<Employee>> {
        val employees: Page<Employee> = employeeService.getEmployees(active, jmb, pageable)
        return ResponseEntity<Page<Employee>>(employees, HttpStatus.OK)
    }

    @Operation(summary = "Get employee overview", description = "Get an overview of employees.")
    @ApiResponse(responseCode = "200", description = "Employee overview retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = EmployeeOverview::class))])
    @GetMapping("/employees/overview")
    @PreAuthorize("@AS.hasRights('GET_EMPLOYEES')")
    fun getEmployeeOverview(
        @Parameter(description = "Filter employees by active status")
        @RequestParam(required = false) active: Boolean?,

        @Parameter(description = "Filter employees by JMB number")
        @RequestParam(required = false) jmb: String?,

        @Parameter(description = "Paging and sorting parameters")
        @PageableDefault(page = 0, size = 25, sort = ["firstName"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<EmployeeOverview>> {
        val employees = employeeService.getEmployeesOverview(active, jmb, pageable)
        return ResponseEntity(employees, HttpStatus.OK)
    }

    @Operation(summary = "Add a new employee", description = "Add a new employee.")
    @ApiResponse(responseCode = "201", description = "Employee added successfully")
    @PostMapping("/employees")
    @PreAuthorize("@AS.hasRights('ADD_EMPLOYEES')")
    fun addEmployee(
        @Parameter(description = "The employee to be added", required = true)
        @RequestBody newEmployee: Employee
    ): ResponseEntity<Void> {
        employeeService.addEmployee(newEmployee)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Operation(summary = "Edit an employee", description = "Edit an existing employee.")
    @ApiResponse(responseCode = "204", description = "Employee edited successfully")
    @PutMapping("/employees")
    @PreAuthorize("@AS.hasRights('EDIT_EMPLOYEES')")
    fun editEmployee(
        @Parameter(description = "The employee to be edited", required = true)
        @RequestBody newEmployee: Employee
    ): ResponseEntity<Void> {
        employeeService.editEmployee(newEmployee)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}



