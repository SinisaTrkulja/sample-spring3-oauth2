package com.tulip.resourceserver.hours

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
class HoursController(private val hoursService: HoursService) {

    @Operation(
        summary = "Get hours",
        description = "Get a list of hours for a specified employee, month, and year."
    )
    @ApiResponse(
        responseCode = "200",
        description = "List of hours retrieved successfully",
        content = [Content(mediaType = "application/json", schema = Schema(implementation = Hours::class))]
    )
    @GetMapping("/hours")
    @PreAuthorize("@AS.hasRights('GET_HOURS')")
    fun getHours(
        @Parameter(description = "JMB number of the employee", required = false)
        @RequestParam(name = "jmb") jmb: String?,

        @Parameter(description = "Month (1-12)", required = false)
        @RequestParam(name = "month") month: Short?,

        @Parameter(description = "Year", required = true)
        @RequestParam(name = "year") year: Short,

        @Parameter(description = "Paging and sorting parameters")
        @PageableDefault(page = 0, size = 25, sort = ["jmb"], direction = Sort.Direction.ASC) pageable: Pageable
    ): ResponseEntity<Page<Hours>> {
        val hours = hoursService.getHours(jmb, month, year, pageable)
        return ResponseEntity<Page<Hours>>(hours, HttpStatus.OK)
    }

    @Operation(summary = "Add hours", description = "Add hours for a specified employee.")
    @ApiResponse(responseCode = "201", description = "Hours added successfully")
    @PostMapping("/hours")
    @PreAuthorize("@AS.hasRights('ADD_HOURS')")
    fun addHours(
        @Parameter(description = "The hours to be added", required = true)
        @RequestBody hours: Hours
    ): ResponseEntity<Void> {
        hoursService.addHours(hours)
        return ResponseEntity(HttpStatus.CREATED)
    }

    @Operation(summary = "Edit hours", description = "Edit existing hours for a specified employee.")
    @ApiResponse(responseCode = "204", description = "Hours edited successfully")
    @PutMapping("/hours")
    @PreAuthorize("@AS.hasRights('EDIT_HOURS')")
    fun editHours(
        @Parameter(description = "The hours to be edited", required = true)
        @RequestBody hours: Hours
    ): ResponseEntity<Void> {
        hoursService.editHours(hours)
        return ResponseEntity(HttpStatus.NO_CONTENT)
    }
}
