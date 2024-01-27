package com.tulip.resourceserver.company

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
class CompanyController(private val companyService: CompanyService) {

    @Operation(summary = "Get companies", description = "Get a list of companies.")
    @ApiResponse(responseCode = "200", description = "List of companies retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Company::class))])
    @GetMapping("/companies")
    @PreAuthorize("@AS.hasRights('GET_COMPANIES')")
    fun getCompanies(
        @Parameter(description = "Filter companies by active status")
        @RequestParam(required = false) active: Boolean?,

        @Parameter(description = "Filter companies by JIB number")
        @RequestParam(required = false) jib: String?,

        @Parameter(description = "Paging and sorting parameters")
        @PageableDefault(page = 0, size = 25, sort = ["jib"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<Company>> {
        val companies = companyService.getCompanies(active, jib, pageable)
        return ResponseEntity<Page<Company>>(companies, HttpStatus.OK)
    }

    @Operation(summary = "Add a new company", description = "Add a new company.")
    @ApiResponse(responseCode = "201", description = "Company added successfully")
    @PostMapping("/companies")
    @PreAuthorize("@AS.hasRights('ADD_COMPANIES')")
    fun addCompany(
        @Parameter(description = "The company to be added", required = true)
        @RequestBody company: Company
    ): ResponseEntity<Void> {
        companyService.addCompany(company)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Operation(summary = "Edit a company", description = "Edit an existing company.")
    @ApiResponse(responseCode = "204", description = "Company edited successfully")
    @PutMapping("/companies")
    @PreAuthorize("@AS.hasRights('EDIT_COMPANIES')")
    fun editCompany(
        @Parameter(description = "The company to be edited", required = true)
        @RequestBody company: Company
    ): ResponseEntity<Void> {
        companyService.editCompany(company)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
