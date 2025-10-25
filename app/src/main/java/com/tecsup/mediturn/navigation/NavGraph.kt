package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.mediturn.ui.AppointmentsScreen
import com.tecsup.mediturn.ui.screens.home.HomeScreen
import com.tecsup.mediturn.ui.screens.search.SearchScreen
import com.tecsup.mediturn.ui.screens.detail.DoctorDetailScreen
import com.tecsup.mediturn.ui.screens.booking.BookingScreen
import com.tecsup.mediturn.ui.screens.profile.ProfileScreen
import com.tecsup.mediturn.ui.screens.confirmation.ConfirmationScreen

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        // Home Screen
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
                onDoctorClick = { doctorId ->
                    navController.navigate(Routes.DoctorDetail.createRoute(doctorId))
                }
            )
        }

        // Search Screen
        composable(route = Routes.Search.route) {
            SearchScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onDoctorClick = { doctorId ->
                    navController.navigate(Routes.DoctorDetail.createRoute(doctorId))
                }
            )
        }

        // Doctor Detail Screen
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
                onBookAppointment = { doctorId ->
                    navController.navigate(Routes.Booking.createRoute(doctorId))
                }
            )
        }

        // Booking Screen
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
                    navController.navigate(Routes.Confirmation.createRoute(appointmentId)) {
                        // Clear back stack to prevent going back to booking
                        popUpTo(Routes.Home.route) { inclusive = false }
                    }
                }
            )
        }

        // Appointments Screen
        composable(route = Routes.Appointments.route) {
            AppointmentsScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onAppointmentClick = { appointmentId ->
                    // TODO: Navigate to appointment detail if needed
                }
            )
        }

        // Profile Screen
        composable(route = Routes.Profile.route) {
            ProfileScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }

        // Confirmation Screen
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
                        popUpTo(Routes.Home.route) { inclusive = false }
                    }
                }
            )
        }
    }
}