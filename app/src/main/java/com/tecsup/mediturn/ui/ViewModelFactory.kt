package com.tecsup.mediturn.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.tecsup.mediturn.MediTurnApplication
import com.tecsup.mediturn.ui.screens.appointments.AppointmentsViewModel
import com.tecsup.mediturn.ui.screens.booking.BookingViewModel
import com.tecsup.mediturn.ui.screens.confirmation.ConfirmationViewModel
import com.tecsup.mediturn.ui.screens.detail.DoctorDetailViewModel
import com.tecsup.mediturn.ui.screens.home.HomeViewModel
import com.tecsup.mediturn.ui.screens.search.SearchViewModel

/**
 * Factory para crear ViewModels con dependencias de repositorios
 * Obtiene la instancia de Application para acceder a los repositorios
 */
class ViewModelFactory(private val application: MediTurnApplication) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(application.doctorRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(application.doctorRepository) as T
            }
            modelClass.isAssignableFrom(DoctorDetailViewModel::class.java) -> {
                DoctorDetailViewModel(application.doctorRepository) as T
            }
            modelClass.isAssignableFrom(BookingViewModel::class.java) -> {
                BookingViewModel(
                    application.doctorRepository,
                    application.appointmentRepository
                ) as T
            }
            modelClass.isAssignableFrom(ConfirmationViewModel::class.java) -> {
                ConfirmationViewModel(application.appointmentRepository) as T
            }
            modelClass.isAssignableFrom(AppointmentsViewModel::class.java) -> {
                AppointmentsViewModel(application.appointmentRepository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
