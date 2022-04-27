package com.tuan.tracker_domain.use_case

import com.tuan.tracker_domain.model.TrackedFood
import com.tuan.tracker_domain.repository.TrackerRepository

class DeleteTrackedFood(
    private val repository: TrackerRepository
) {

    /**
     * Delete food from the database
     *
     * @param trackedFood which food to delete
     */
    suspend operator fun invoke(trackedFood: TrackedFood) {
        repository.deleteTrackedFood(food = trackedFood)
    }
}