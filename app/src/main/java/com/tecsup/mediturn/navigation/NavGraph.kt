package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.mediturn.ui.AppointmentsScreen
import com.tecsup.mediturn.ui.BookingScreen
import com.tecsup.mediturn.ui.ConfirmationScreen
import com.tecsup.mediturn.ui.ProfileScreen
import com.tecsup.mediturn.ui.screens.detail.DoctorDetailScreen
import com.tecsup.mediturn.ui.screens.home.HomeScreen
import com.tecsup.mediturn.ui.screens.search.SearchScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        composable(route = Routes.Home.route) {
            HomeScreen(
                onNavigateToSearch = {
                    navController.navigate(Routes.Search.route)
                },
                onNavigateToAppointments = {
                    navController.navigate(Routes.Appointments.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Routes.Profile.route)
                },
                onNavigateToDoctorDetail = { doctorId ->
                    navController.navigate(Routes.DoctorDetail.createRoute(doctorId))
                }
            )
        }

        composable(route = Routes.Search.route) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToDoctorDetail = { doctorId ->
                    navController.navigate(Routes.DoctorDetail.createRoute(doctorId))
                }
            )
        }

        composable(
            route = Routes.DoctorDetail.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            DoctorDetailScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToBooking = { doctorId ->
                    navController.navigate(Routes.Booking.createRoute(doctorId))
                }
            )
        }

        composable(
            route = Routes.Booking.route,
            arguments = listOf(
                navArgument("doctorId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: ""
            BookingScreen(
                doctorId = doctorId,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onNavigateToConfirmation = { appointmentId ->
                    navController.navigate(Routes.Confirmation.createRoute(appointmentId))
                }
            )
        }

        composable(route = Routes.Appointments.route) {
            AppointmentsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(route = Routes.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.Confirmation.route,
            arguments = listOf(
                navArgument("appointmentId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            ConfirmationScreen(
                appointmentId = appointmentId,
                onNavigateToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                },
                onNavigateToAppointments = {
                    navController.navigate(Routes.Appointments.route) {
                        popUpTo(Routes.Home.route)
                    }
                }
            )
        }
    }
}