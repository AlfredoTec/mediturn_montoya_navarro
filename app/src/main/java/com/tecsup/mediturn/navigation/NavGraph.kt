package com.tecsup.mediturn.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.tecsup.mediturn.ui.screens.home.HomeScreen
import com.tecsup.mediturn.ui.screens.search.SearchScreen
import com.tecsup.mediturn.ui.screens.detail.DoctorDetailScreen
import com.tecsup.mediturn.ui.screens.booking.BookingScreen
import com.tecsup.mediturn.ui.screens.appointments.AppointmentsScreen
import com.tecsup.mediturn.ui.screens.profile.ProfileScreen
import com.tecsup.mediturn.ui.screens.confirmation.ConfirmationScreen

// Se encarga de de la navegación
@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home.route
    ) {
        // Home
        composable(Routes.Home.route) {
            HomeScreen(
                // Navegación: Pantalla de busqueda
                onSearchClick = { navController.navigate(Routes.Search.route) },

                // Navegación: Pantalla de detalle del doctor
                onDoctorClick = { doctorId -> navController.navigate(Routes.DoctorDetail.createRoute(doctorId)) },

                // Navegación: Pantalla de agendar cita
                onAppointmentsClick = { navController.navigate(Routes.Appointments.route) },

                // Navegación: Pantalla de perfil
                onProfileClick = { navController.navigate(Routes.Profile.route) },

                // Navegación: Pantalla de inicio de sesión
                onSpecialtyClick = { navController.navigate(Routes.Search.route) }
            )
        }

        // Search
        composable(Routes.Search.route) {
            SearchScreen(
                // Navegación: Pantalla de inicio
                onNavigateBack = { navController.popBackStack() },

                // Navegación: Pantalla de detalle del doctor
                onDoctorClick = { doctorId -> navController.navigate(Routes.DoctorDetail.createRoute(doctorId)) }
            )
        }

        // Doctor Detail
        composable(
            route = Routes.DoctorDetail.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: "doc_1"
            DoctorDetailScreen(
                doctorId = doctorId,
                onNavigateBack = { navController.popBackStack() },
                onBookAppointment = { doctorId ->
                    navController.navigate(Routes.Booking.createRoute(doctorId))
                }
            )
        }

        // Booking
        composable(
            route = Routes.Booking.route,
            arguments = listOf(navArgument("doctorId") { type = NavType.StringType })
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getString("doctorId") ?: "doc_1"
            BookingScreen(
                doctorId = doctorId,
                onNavigateBack = { navController.popBackStack() },
                onConfirmBooking = { appointmentId ->
                    navController.navigate(Routes.Confirmation.createRoute(appointmentId))
                }
            )
        }

        // Appointments
        composable(Routes.Appointments.route) {
            AppointmentsScreen(
                // Navegación: Pantalla de inicio
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Profile
        composable(Routes.Profile.route) {
            ProfileScreen(
                // Navegación: Pantalla de inicio
                onNavigateBack = { navController.popBackStack() }
            )
        }

        // Confirmation
        composable(
            route = Routes.Confirmation.route,
            arguments = listOf(navArgument("appointmentId") { type = NavType.StringType })
        ) { backStackEntry ->
            val appointmentId = backStackEntry.arguments?.getString("appointmentId") ?: ""
            ConfirmationScreen(
                appointmentId = appointmentId,
                onGoToHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Home.route) { inclusive = true }
                    }
                },
                onViewAppointments = {
                    navController.navigate(Routes.Appointments.route)
                }
            )
        }
    }
}