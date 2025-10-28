package com.tecsup.mediturn.navigation

sealed class Routes(val route: String) {
    object Home : Routes("home")
    object Search : Routes("search")
    object DoctorDetail : Routes("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: String) = "doctor_detail/$doctorId"
    }
    object Booking : Routes("booking/{doctorId}") {
        fun createRoute(doctorId: String) = "booking/$doctorId"
    }
    object Appointments : Routes("appointments")
    object Profile : Routes("profile")
    object Confirmation : Routes("confirmation/{appointmentId}") {
        fun createRoute(appointmentId: String) = "confirmation/$appointmentId"
    }
}