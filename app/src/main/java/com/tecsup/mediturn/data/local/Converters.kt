package com.tecsup.mediturn.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tecsup.mediturn.data.model.*
import java.util.Date

class Converters {
    private val gson = Gson()

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? = value?.let { Date(it) }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? = date?.time

    @TypeConverter
    fun fromSpecialty(specialty: Specialty): String = specialty.name

    @TypeConverter
    fun toSpecialty(value: String): Specialty = Specialty.valueOf(value)

    @TypeConverter
    fun fromConsultationType(type: ConsultationType): String = type.name

    @TypeConverter
    fun toConsultationType(value: String): ConsultationType = ConsultationType.valueOf(value)

    @TypeConverter
    fun fromAppointmentStatus(status: AppointmentStatus): String = status.name

    @TypeConverter
    fun toAppointmentStatus(value: String): AppointmentStatus = AppointmentStatus.valueOf(value)

    @TypeConverter
    fun fromTimeSlotList(value: List<TimeSlot>): String = gson.toJson(value)

    @TypeConverter
    fun toTimeSlotList(value: String): List<TimeSlot> {
        val listType = object : TypeToken<List<TimeSlot>>() {}.type
        return gson.fromJson(value, listType)
    }
}