package com.tulip.resourceserver.hours

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth

@Service
@Transactional
class HoursService(private val repository: HoursRepository) {
    fun getHours(jmb: String?, month: Short?, year: Short, pageable: Pageable): Page<Hours> =
        repository.find(jmb, month, year, pageable)

    fun addHours(hours: Hours) {
        val id = HoursPK(hours.jmb, hours.day, hours.month, hours.year)
        if (repository.existsById(id)) throw DuplicateKeyException("Hours entry already exists!")
        repository.save(hours)
    }

    fun editHours(newHoursData: Hours) {
        val id = HoursPK(newHoursData.jmb, newHoursData.day, newHoursData.month, newHoursData.year)
        val existingHoursEntry: Hours = repository.findById(id)
            .orElseThrow { IOException("The requested hours entry doesn't exist!") }
        val hoursDate = LocalDate.of(newHoursData.year.toInt(), newHoursData.month.toInt(), newHoursData.day.toInt())
        if (YearMonth.from(hoursDate).isBefore(YearMonth.from(LocalDate.now())))
            throw IOException("The supplied hours entry is more than a month in the past!")

        existingHoursEntry.hours = newHoursData.hours
        existingHoursEntry.hoursType = newHoursData.hoursType
        existingHoursEntry.additionalInfo = newHoursData.additionalInfo
        repository.save(existingHoursEntry)
    }
}
