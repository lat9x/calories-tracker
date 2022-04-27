package com.tuan.tracker_domain.use_case

import com.tuan.tracker_domain.model.TrackedFood
import com.tuan.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

class GetFoodsForDate(
    private val repository: TrackerRepository
) {

    /**
     * Get foods from the database, based on the chosen date
     *
     * @param date
     */
    operator fun invoke(date: LocalDate): Flow<List<TrackedFood>> {
        return repository.getFoodsForDate(localDate = date)
    }
}