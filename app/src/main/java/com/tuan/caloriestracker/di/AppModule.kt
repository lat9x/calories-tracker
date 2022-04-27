package com.tuan.caloriestracker.di

import android.app.Application
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.tuan.core.data.preferences.DefaultPreferences
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.domain.use_case.TakeOnlyDigits
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideSharedPreferences(
        app: Application
    ): SharedPreferences {
        return app.getSharedPreferences("shared_pref", MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun providePreferences(
        sharedPreferences: SharedPreferences
    ): Preferences {
        return DefaultPreferences(sharedPref = sharedPreferences)
    }

    @Provides
    @Singleton
    fun provideTakeOnlyDigitsUseCase(): TakeOnlyDigits {
        return TakeOnlyDigits()
    }
}