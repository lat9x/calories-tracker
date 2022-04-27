package com.tuan.tracker_data.repository

import com.tuan.tracker_data.local.TrackerDao
import com.tuan.tracker_data.mapper.toTrackableFood
import com.tuan.tracker_data.mapper.toTrackedFood
import com.tuan.tracker_data.mapper.toTrackedFoodEntity
import com.tuan.tracker_data.remote.OpenFoodApi
import com.tuan.tracker_data.remote.dto.SearchDto
import com.tuan.tracker_domain.model.TrackableFood
import com.tuan.tracker_domain.model.TrackedFood
import com.tuan.tracker_domain.repository.TrackerRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate

class TrackerRepositoryImpl(
    private val dao: TrackerDao,
    private val api: OpenFoodApi
) : TrackerRepository {

    override suspend fun searchFood(
        query: String,
        page: Int,
        pageSize: Int
    ): Result<List<TrackableFood>> {
        return try {
            val searchDto: SearchDto = api.searchFood(
                query = query,
                page = page,
                pageSize = pageSize
            )

            Result.success(
                value = searchDto.products
                    .filter { product ->
                        val calculatedCalories =
                            product.nutriments.carbohydrates100g * 4f +
                                    product.nutriments.proteins100g * 4f +
                                    product.nutriments.fat100g * 9f

                        val lowerBound = calculatedCalories * 0.99f
                        val upperBound = calculatedCalories * 1.01f

                        product.nutriments.energyKcal100g in (lowerBound..upperBound)
                    }
                    .mapNotNull { product ->
                        // just take non-null trackable food
                        product.toTrackableFood()
                    }
            )
        } catch (e: Throwable) {
            e.printStackTrace()
            Result.failure(exception = e)
        }
    }

    override suspend fun insertTrackedFood(food: TrackedFood) {
        dao.insertTrackedFood(trackedFoodEntity = food.toTrackedFoodEntity())
    }

    override suspend fun deleteTrackedFood(food: TrackedFood) {
        dao.deleteTrackedFood(trackedFoodEntity = food.toTrackedFoodEntity())
    }

    override fun getFoodsForDate(localDate: LocalDate): Flow<List<TrackedFood>> {
        return dao.getFoodsForDate(
            day = localDate.dayOfMonth,
            month = localDate.monthValue,
            year = localDate.year
        ).map { entities ->
            entities.map { trackedFoodEntity ->
                trackedFoodEntity.toTrackedFood()
            }
        }
    }
}