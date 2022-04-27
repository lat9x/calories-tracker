package com.tuan.tracker_domain.use_case

import com.tuan.tracker_domain.model.TrackableFood
import com.tuan.tracker_domain.repository.TrackerRepository

class SearchFood(
    private val repository: TrackerRepository
) {

    /**
     * Search for food
     *
     * @param query food name
     * @param page the page to query
     * @param pageSize how many foods per page
     * @return list of trackable food
     */
    suspend operator fun invoke(
        query: String,
        page: Int = FIRST_PAGE,
        pageSize: Int = FOODS_PER_PAGE
    ): Result<List<TrackableFood>> {

        return if (query.isBlank()) {
            Result.success(value = emptyList())
        } else {
            repository.searchFood(
                query.trim(),
                page = page,
                pageSize = pageSize
            )
        }
    }

    companion object {
        private const val FIRST_PAGE = 1
        private const val FOODS_PER_PAGE = 50
    }
}