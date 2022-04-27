package com.tuan.onboarding_domain.di

import com.tuan.onboarding_domain.use_case.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
object OnboardingDomainModule {

    @Provides
    @ViewModelScoped
    fun provideOnboardingUseCases(): OnboardingUseCases {
        return OnboardingUseCases(
            validateAge = ValidateAge(),
            validateHeight = ValidateHeight(),
            validateWeight = ValidateWeight(),
            validateNutrients = ValidateNutrients()
        )
    }
}