# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

MediTurn is an Android medical appointment booking application built with Kotlin and Jetpack Compose. It allows patients to search for doctors by specialty, schedule appointments, manage their medical calendar, and receive reminders.

**Tech Stack:**
- Kotlin
- Jetpack Compose with Material Design 3
- Navigation Compose
- DataStore (for theme persistence)
- Room Database (configured but not yet fully implemented)
- MVVM Architecture

## Building and Running

### Build the app
```bash
# From project root
./gradlew build
```

### Run on emulator/device
```bash
./gradlew installDebug
```

Or use Android Studio's Run button (Shift+F10).

### Clean build
```bash
./gradlew clean
./gradlew build
```

### Run tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

## Architecture Overview

### Package Structure

```
com.tecsup.mediturn/
├── data/
│   ├── local/          # SampleData for development
│   ├── model/          # Data models (Doctor, Appointment, TimeSlot, etc.)
│   ├── repository/     # Repository pattern implementations
│   └── ThemePreferenceManager.kt  # DataStore for theme persistence
├── navigation/         # NavGraph and Routes
├── ui/
│   ├── components/     # Reusable UI components
│   ├── screens/        # Feature screens with ViewModels
│   └── theme/          # Theme configuration and ThemeViewModel
└── MainActivity.kt
```

### Navigation Flow

The app uses a single-activity architecture with Navigation Compose:

1. **Home** → Shows specialty cards, featured doctors, and bottom navigation
2. **Search** → Filter doctors by specialty/name
3. **DoctorDetail** → View doctor profile (via `doctorId`)
4. **Booking** → Select date/time and consultation type (via `doctorId`)
5. **Confirmation** → Appointment confirmed (via `appointmentId`)
6. **Appointments** → View upcoming and past appointments
7. **Profile** → User profile and theme toggle

Navigation is defined in `navigation/NavGraph.kt` using sealed class routes in `navigation/Routes.kt`.

### Data Layer

**Current Implementation:**
- Uses in-memory data from `SampleData.kt` for development
- Repositories (`DoctorRepository`, `AppointmentRepository`) provide abstraction over data sources
- Room dependencies are configured but database implementation is pending

**Data Models:**
- `Doctor` - Contains doctor info, specialty, availability, pricing, time slots
- `Appointment` - References a Doctor, date (as `java.util.Date`), consultation type, status
- `TimeSlot` - Represents available booking times
- `Specialty` - Enum for medical specialties
- `ConsultationType` - Enum for IN_PERSON or TELEHEALTH
- `AppointmentStatus` - Enum for CONFIRMED, PENDING, CANCELLED, COMPLETED

**Note:** Date handling uses `java.util.Date` throughout the app. Recent commits indicate migration from String-based dates to Date objects for better type safety.

### UI Layer (MVVM)

Each screen follows the MVVM pattern:
- **Screen** composables in `ui/screens/[feature]/[Feature]Screen.kt`
- **ViewModel** in `ui/screens/[feature]/[Feature]ViewModel.kt`
- ViewModels use StateFlow for reactive state management
- Repositories are injected directly (no DI framework yet)

**Theme Management:**
- `ThemeViewModel` manages dark/light mode state
- `ThemePreferenceManager` persists theme preference using DataStore
- Theme toggle is available in Profile screen
- Theme state is passed through NavGraph to MainActivity

### Key Components

- `DoctorCard.kt` - Displays doctor information in lists
- `AppointmentCard.kt` - Shows appointment details with status badges
- `TimeSlotButton.kt` - Interactive time slot selection buttons

## Development Guidelines

### Adding a New Screen

1. Create route in `navigation/Routes.kt`:
```kotlin
object NewScreen : Routes("new_screen/{param}") {
    fun createRoute(param: String) = "new_screen/$param"
}
```

2. Create ViewModel in `ui/screens/newscreen/NewScreenViewModel.kt`
3. Create Screen composable in `ui/screens/newscreen/NewScreen.kt`
4. Add to NavGraph in `navigation/NavGraph.kt`

### Working with Dates

The app uses `java.util.Date` for all date/time operations. When displaying dates:
- Use `SimpleDateFormat` for formatting
- Consider time zones when working with appointments
- See existing ViewModels for formatting examples

### State Management

- Use `StateFlow` in ViewModels for UI state
- Collect state in composables using `collectAsState()`
- ViewModels should be stateless between configuration changes

### Theme Customization

Theme colors and typography are defined in:
- `ui/theme/Color.kt` - Color definitions for light/dark themes
- `ui/theme/Theme.kt` - MediTurnTheme composable
- `ui/theme/Type.kt` - Typography styles

## Future Database Migration

Room is configured but not yet integrated. When implementing:
1. Create DAOs in `data/local/dao/`
2. Create AppDatabase with migrations in `data/local/`
3. Update repositories to use DAOs instead of SampleData
4. Consider using a DI framework (Hilt/Koin) for database injection

## Configuration

**Build Configuration:**
- `compileSdk = 34`
- `minSdk = 24`
- `targetSdk = 34`
- Java 17 compatibility
- Kotlin JVM target: 17

**Key Dependencies:**
- Jetpack Compose BOM 2024.09.02
- Navigation Compose 2.8.0
- Room 2.6.1
- DataStore Preferences 1.1.7
- Lifecycle/ViewModel 2.8.6
