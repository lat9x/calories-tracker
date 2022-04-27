package com.tuan.caloriestracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.DrawerState
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberScaffoldState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.annotation.ExperimentalCoilApi
import com.tuan.caloriestracker.navigation.Param.DAY_OF_MONTH
import com.tuan.caloriestracker.navigation.Param.MEAL_NAME
import com.tuan.caloriestracker.navigation.Param.MONTH
import com.tuan.caloriestracker.navigation.Param.YEAR
import com.tuan.caloriestracker.navigation.Route
import com.tuan.caloriestracker.ui.theme.CaloriesTrackerTheme
import com.tuan.core.domain.preferences.Preferences
import com.tuan.onboarding_presentation.activity.ActivityScreen
import com.tuan.onboarding_presentation.age.AgeScreen
import com.tuan.onboarding_presentation.gender.GenderScreen
import com.tuan.onboarding_presentation.goal.GoalScreen
import com.tuan.onboarding_presentation.height.HeightScreen
import com.tuan.onboarding_presentation.nutrient_goal.NutrientGoalScreen
import com.tuan.onboarding_presentation.weight.WeightScreen
import com.tuan.onboarding_presentation.welcome.WelcomeScreen
import com.tuan.tracker_presentation.search.SearchScreen
import com.tuan.tracker_presentation.tracker_overview.TrackerOverviewScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@ExperimentalComposeUiApi
@ExperimentalCoilApi
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val shouldShowOnboarding = preferences.loadOnboardingShowState()

        setContent {
            CaloriesTrackerTheme {
                val navController: NavHostController = rememberNavController()
                val scaffoldState = rememberScaffoldState()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    scaffoldState = scaffoldState
                ) {
                    NavHost(
                        navController = navController,
                        startDestination = if (shouldShowOnboarding)
                            Route.WELCOME
                        else
                            Route.TRACKER_OVERVIEW
                    ) {
                        composable(route = Route.WELCOME) {
                            WelcomeScreen(
                                onNextClick = {
                                    navController.navigate(route = Route.GENDER)
                                }
                            )
                        }
                        composable(route = Route.GENDER) {
                            GenderScreen(onNextClick = {
                                navController.navigate(route = Route.AGE)
                            })
                        }
                        composable(route = Route.AGE) {
                            AgeScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(route = Route.HEIGHT)
                                }
                            )
                        }
                        composable(route = Route.HEIGHT) {
                            HeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(route = Route.WEIGHT)
                                }
                            )
                        }
                        composable(route = Route.WEIGHT) {
                            WeightScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(route = Route.ACTIVITY)
                                }
                            )
                        }
                        composable(route = Route.ACTIVITY) {
                            ActivityScreen(
                                onNextClick = {
                                    navController.navigate(route = Route.GOAL)
                                }
                            )
                        }
                        composable(route = Route.GOAL) {
                            GoalScreen(onNextClick = {
                                navController.navigate(route = Route.NUTRIENT_GOAL)
                            })
                        }
                        composable(route = Route.NUTRIENT_GOAL) {
                            NutrientGoalScreen(
                                scaffoldState = scaffoldState,
                                onNextClick = {
                                    navController.navigate(route = Route.TRACKER_OVERVIEW)
                                }
                            )
                        }
                        composable(route = Route.TRACKER_OVERVIEW) {
                            TrackerOverviewScreen(
                                onNavigateToSearchScreen = { mealName, day, month, year ->
                                    navController.navigate(
                                        route = Route.SEARCH + "/$mealName/$day/$month/$year"
                                    )
                                }
                            )
                        }
                        composable(
                            route = Route.SEARCH + "/{$MEAL_NAME}" +
                                    "/{$DAY_OF_MONTH}" +
                                    "/{$MONTH}" +
                                    "/{$YEAR}",
                            arguments = listOf(
                                navArgument(name = MEAL_NAME) {
                                    type = NavType.StringType
                                },
                                navArgument(name = DAY_OF_MONTH) {
                                    type = NavType.IntType
                                },
                                navArgument(name = MONTH) {
                                    type = NavType.IntType
                                },
                                navArgument(name = YEAR) {
                                    type = NavType.IntType
                                },
                            )
                        ) { navBackStackEntry ->
                            val mealName = navBackStackEntry.arguments?.getString(MEAL_NAME)!!
                            val dayOfMonth = navBackStackEntry.arguments?.getInt(DAY_OF_MONTH)!!
                            val month = navBackStackEntry.arguments?.getInt(MONTH)!!
                            val year = navBackStackEntry.arguments?.getInt(YEAR)!!

                            SearchScreen(
                                scaffoldState = scaffoldState,
                                mealName = mealName,
                                dayOfMonth = dayOfMonth,
                                month = month,
                                year = year,
                                onNavigateUp = {
                                    navController.navigateUp()
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}