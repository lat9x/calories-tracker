package com.tuan.tracker_domain.di

import com.tuan.core.domain.preferences.Preferences
import com.tuan.tracker_domain.repository.TrackerRepository
import com.tuan.tracker_domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object TrackerDomainModule {

    @ViewModelScoped
    @Provides
    fun provideTrackerUseCases(
        repository: TrackerRepository,
        preferences: Preferences
    ): TrackerUseCases {
        return TrackerUseCases(
            trackFood = TrackFood(repository = repository),
            searchFood = SearchFood(repository = repository),
            getFoodsForDate = GetFoodsForDate(repository = repository),
            deleteTrackedFood = DeleteTrackedFood(repository = repository),
            calculateMealNutrients = CalculateMealNutrients(preferences = preferences)
        )
    }
}