package com.tuan.caloriestracker

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.google.common.truth.Truth.assertThat
import com.tuan.caloriestracker.navigation.Param
import com.tuan.caloriestracker.navigation.Route
import com.tuan.caloriestracker.repository.TrackerRepositoryFake
import com.tuan.caloriestracker.ui.theme.CaloriesTrackerTheme
import com.tuan.core.domain.model.ActivityLevel
import com.tuan.core.domain.model.Gender
import com.tuan.core.domain.model.GoalType
import com.tuan.core.domain.model.UserInfo
import com.tuan.core.domain.preferences.Preferences
import com.tuan.core.domain.use_case.TakeOnlyDigits
import com.tuan.tracker_domain.model.TrackableFood
import com.tuan.tracker_domain.use_case.*
import com.tuan.tracker_presentation.search.SearchScreen
import com.tuan.tracker_presentation.search.SearchViewModel
import com.tuan.tracker_presentation.tracker_overview.TrackerOverviewScreen
import com.tuan.tracker_presentation.tracker_overview.TrackerOverviewViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import io.mockk.every
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.math.roundToInt

@ExperimentalCoilApi
@ExperimentalComposeUiApi
@HiltAndroidTest
class TrackerOverviewE2E {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    private lateinit var repository: TrackerRepositoryFake
    private lateinit var trackerUseCases: TrackerUseCases
    private lateinit var preferences: Preferences
    private lateinit var trackerOverviewViewModel: TrackerOverviewViewModel
    private lateinit var searchViewModel: SearchViewModel

    private lateinit var navController: NavHostController

    @Before
    fun setup() {
        preferences = mockk(relaxed = true)
        every { preferences.loadUserInfo() } returns UserInfo(
            gender = Gender.Male,
            age = 20,
            weight = 80f,
            height = 178,
            activityLevel = ActivityLevel.Medium,
            goalType = GoalType.KeepWeight,
            carbRatio = 0.4f,
            proteinRatio = 0.3f,
            fatRatio = 0.3f,
        )

        repository = TrackerRepositoryFake()
        trackerUseCases = TrackerUseCases(
            trackFood = TrackFood(repository = repository),
            searchFood = SearchFood(repository = repository),
            getFoodsForDate = GetFoodsForDate(repository = repository),
            deleteTrackedFood = DeleteTrackedFood(repository = repository),
            calculateMealNutrients = CalculateMealNutrients(preferences = preferences)
        )
        trackerOverviewViewModel = TrackerOverviewViewModel(useCases = trackerUseCases)
        searchViewModel = SearchViewModel(
            trackerUseCases = trackerUseCases,
            takeOnlyDigits = TakeOnlyDigits()
        )

        composeRule.setContent {
            CaloriesTrackerTheme() {
                val scaffoldState = rememberScaffoldState()
                navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = Route.TRACKER_OVERVIEW
                    ) {
                        composable(route = Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearchScreen = { mealName, day, month, year ->
                                    navController.navigate(
                                        route = Route.SEARCH + "/$mealName/$day/$month/$year"
                                    )
                                },
                                viewModel = trackerOverviewViewModel
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{${Param.MEAL_NAME}}" +
                                    "/{${Param.DAY_OF_MONTH}}" +
                                    "/{${Param.MONTH}}" +
                                    "/{${Param.YEAR}}",
                            arguments = listOf(
                                navArgument(name = Param.MEAL_NAME) {
                                    type = NavType.StringType
                                },
                                navArgument(name = Param.DAY_OF_MONTH) {
                                    type = NavType.IntType
                                },
                                navArgument(name = Param.MONTH) {
                                    type = NavType.IntType
                                },
                                navArgument(name = Param.YEAR) {
                                    type = NavType.IntType
                                },
                            )
                        ) { navBackStackEntry ->
                            val mealName = navBackStackEntry.arguments?.getString(Param.MEAL_NAME)!!
                            val dayOfMonth = navBackStackEntry.arguments?.getInt(Param.DAY_OF_MONTH)!!
                            val month = navBackStackEntry.arguments?.getInt(Param.MONTH)!!
                            val year = navBackStackEntry.arguments?.getInt(Param.YEAR)!!

                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                },
                                viewModel = searchViewModel
                            )
                        }
                    }
                }
            }
        }
    }

    @Test
    fun addBreakfast_appearsUnderBreakfast_nutrientsProperlyCalculated() {
        repository.searchResults = listOf(
            TrackableFood(
                name = "banana",
                imageUrl = null,
                caloriesPer100g = 150,
                carbPer100g = 50,
                proteinPer100g = 5,
                fatPer100g = 1,
            )
        )

        val addedAmount = 150
        val expectedCalories = (1.5f * 150).roundToInt()
        val expectedCarbs = (1.5f * 50).roundToInt()
        val expectedProtein = (1.5f * 5).roundToInt()
        val expectedFat = (1.5f * 1).roundToInt()

        composeRule
            .onNodeWithText("Add breakfast")
            .assertDoesNotExist()
        composeRule
            .onNodeWithContentDescription("Breakfast")
            .performClick()
        composeRule
            .onNodeWithText("Add breakfast")
            .assertIsDisplayed()
        composeRule
            .onNodeWithText("Add breakfast")
            .performClick()

        assertThat(navController.currentDestination?.route?.startsWith(Route.SEARCH)).isTrue()

        composeRule
            .onNodeWithTag("search_text_field")
            .performTextInput("banana")

        composeRule
            .onNodeWithContentDescription("Search...")
            .performClick()

        composeRule
            .onNodeWithText("Carbs")
            .performClick()

        composeRule
            .onNodeWithContentDescription("Amount")
            .performTextInput(addedAmount.toString())

        composeRule
            .onNodeWithContentDescription("Track")
            .performClick()

        assertThat(navController.currentDestination?.route?.startsWith(Route.TRACKER_OVERVIEW)).isTrue()

        composeRule
            .onAllNodesWithText(expectedCarbs.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedProtein.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedFat.toString())
            .onFirst()
            .assertIsDisplayed()
        composeRule
            .onAllNodesWithText(expectedCalories.toString())
            .onFirst()
            .assertIsDisplayed()
    }
}