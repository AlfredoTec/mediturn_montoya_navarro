# MediTurn - Sistema de Citas Médicas

<div align="center">
  <img src="https://img.shields.io/badge/Kotlin-1.9.0-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white" />
  <img src="https://img.shields.io/badge/Jetpack_Compose-1.5.4-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white" />
  <img src="https://img.shields.io/badge/Android-24%2B-3DDC84?style=for-the-badge&logo=android&logoColor=white" />
  <img src="https://img.shields.io/badge/Room-2.6.1-00897B?style=for-the-badge" />
  <img src="https://img.shields.io/badge/Retrofit-2.9.0-48B983?style=for-the-badge" />
</div>

## Descripción

MediTurn es una aplicación móvil moderna desarrollada en Kotlin con Jetpack Compose que revoluciona la forma en que los pacientes acceden a servicios de salud. Permite buscar médicos por especialidad, agendar citas, gestionar el calendario médico y recibir información detallada de profesionales de la salud.

### Características Principales

- Búsqueda avanzada de médicos con filtros por especialidad, ubicación y disponibilidad
- Sistema de reserva de citas con selección de fecha, hora y modalidad
- Gestión completa de citas próximas y pasadas
- Perfiles detallados de médicos con información profesional
- Soporte para teleconsultas
- Interfaz moderna con Material Design 3
- Modo oscuro y claro
- Sistema híbrido de datos (local y remoto)

## Tecnologías Utilizadas

### Frontend Móvil (Android)

- **Lenguaje:** Kotlin 1.9.0
- **UI Framework:** Jetpack Compose
- **Arquitectura:** MVVM (Model-View-ViewModel)
- **Navegación:** Navigation Compose
- **Inyección de Dependencias:** Manual Factory Pattern
- **Base de Datos Local:** Room Database 2.6.1
- **Network:** Retrofit 2.9.0 + OkHttp 4.12.0
- **Coroutines:** Kotlin Coroutines + Flow
- **Gestión de Estado:** StateFlow
- **Material Design:** Material 3
- **Persistencia:** DataStore Preferences

### Backend (Django REST API)

- **Framework:** Django 5.x
- **API:** Django REST Framework
- **Base de Datos:** SQLite (desarrollo)
- **Serialización:** DRF Serializers

## Arquitectura del Proyecto

### Estructura de Carpetas

```
mediturn_montoya_navarro/
├── Django/
│   └── mediturnapi/              # Backend API REST
│       ├── api/                  # App principal de la API
│       │   ├── models.py         # Modelos: Doctor, Patient, Appointment, TimeSlot
│       │   ├── serializers.py    # Serializadores DRF
│       │   ├── views.py          # ViewSets para CRUD
│       │   └── urls.py           # Rutas de la API
│       ├── mediturnapi/          # Configuración Django
│       └── populate_data.py      # Script de datos de prueba
│
└── mobile/                       # Aplicación Android
    └── app/
        └── src/main/java/com/tecsup/mediturn/
            ├── data/
            │   ├── local/
            │   │   ├── dao/              # DAOs de Room
            │   │   │   ├── DoctorDao.kt
            │   │   │   └── AppointmentDao.kt
            │   │   ├── AppDatabase.kt    # Database principal
            │   │   └── SampleData.kt     # Datos de prueba
            │   ├── remote/
            │   │   ├── dto/              # Data Transfer Objects
            │   │   │   ├── DoctorDto.kt
            │   │   │   ├── AppointmentDto.kt
            │   │   │   ├── PatientDto.kt
            │   │   │   └── TimeSlotDto.kt
            │   │   ├── MediTurnApiService.kt  # Retrofit API
            │   │   └── RetrofitClient.kt      # Cliente HTTP
            │   ├── model/                # Modelos de dominio
            │   │   ├── Doctor.kt
            │   │   ├── Patient.kt
            │   │   ├── Appointment.kt
            │   │   ├── Specialty.kt
            │   │   └── TimeSlot.kt
            │   ├── repository/           # Capa de repositorios
            │   │   ├── DoctorRepository.kt
            │   │   ├── AppointmentRepository.kt
            │   │   ├── RemoteDoctorRepository.kt
            │   │   ├── RemoteAppointmentRepository.kt
            │   │   ├── UnifiedDoctorRepository.kt
            │   │   └── UnifiedAppointmentRepository.kt
            │   └── DataSourceConfig.kt   # Configuración de fuente de datos
            │
            ├── ui/
            │   ├── screens/              # Pantallas de la app
            │   │   ├── home/
            │   │   │   ├── HomeScreen.kt
            │   │   │   └── HomeViewModel.kt
            │   │   ├── search/
            │   │   │   ├── SearchScreen.kt
            │   │   │   └── SearchViewModel.kt
            │   │   ├── detail/
            │   │   │   ├── DoctorDetailScreen.kt
            │   │   │   └── DoctorDetailViewModel.kt
            │   │   ├── booking/
            │   │   │   ├── BookingScreen.kt
            │   │   │   └── BookingViewModel.kt
            │   │   ├── appointments/
            │   │   │   ├── AppointmentsScreen.kt
            │   │   │   └── AppointmentsViewModel.kt
            │   │   ├── profile/
            │   │   │   ├── ProfileScreen.kt
            │   │   │   └── ProfileViewModel.kt
            │   │   └── confirmation/
            │   │       └── ConfirmationScreen.kt
            │   ├── components/           # Componentes reutilizables
            │   │   ├── DoctorCard.kt
            │   │   ├── AppointmentCard.kt
            │   │   └── CustomTopAppBar.kt
            │   └── theme/                # Tema Material 3
            │       ├── Color.kt
            │       ├── Theme.kt
            │       └── Type.kt
            │
            ├── navigation/
            │   ├── NavGraph.kt           # Gráfico de navegación
            │   └── Routes.kt             # Definición de rutas
            │
            ├── utils/
            │   └── ViewModelFactory.kt   # Factory para ViewModels
            │
            ├── MediTurnApplication.kt    # Clase Application
            └── MainActivity.kt           # Activity principal
```

### Arquitectura MVVM

```
┌─────────────┐
│     View    │  (Composables)
│  (Screen)   │
└──────┬──────┘
       │ Observa StateFlow
       ▼
┌─────────────┐
│  ViewModel  │  (Lógica de presentación)
│             │
└──────┬──────┘
       │ Llama métodos
       ▼
┌─────────────┐
│ Repository  │  (Capa de abstracción)
│  (Unified)  │
└──────┬──────┘
       │
       ├──────────────┐
       ▼              ▼
┌──────────┐   ┌──────────┐
│   Room   │   │ Retrofit │
│  (Local) │   │ (Remote) │
└──────────┘   └──────────┘
```

## Patrones de Diseño

### 1. Repository Pattern
Abstrae la lógica de acceso a datos, permitiendo cambiar entre fuentes locales y remotas sin modificar los ViewModels.

### 2. Unified Repository Pattern
Implementación híbrida que permite usar Room (local) o Retrofit (remoto) mediante configuración, facilitando desarrollo offline y online.

### 3. Factory Pattern
ViewModelFactory proporciona instancias de ViewModels con dependencias inyectadas.

### 4. Observer Pattern
StateFlow/Flow para comunicación reactiva entre ViewModels y UI.

### 5. Singleton Pattern
Database, ApiService y Application son singletons.

## Configuración del Proyecto

### Requisitos Previos

- Android Studio Hedgehog o superior
- JDK 17
- SDK Android 24+ (Android 7.0+)
- Python 3.8+ (para backend)
- Git

### Instalación

#### 1. Clonar el Repositorio

```bash
git clone https://github.com/AlfredoTec/mediturn_montoya_navarro.git
cd mediturn_montoya_navarro
```

#### 2. Configurar Backend Django (Opcional)

```bash
cd Django/mediturnapi

# Crear entorno virtual
python -m venv venv

# Activar entorno virtual
# Windows:
venv\Scripts\activate
# Linux/Mac:
source venv/bin/activate

# Instalar dependencias
pip install django djangorestframework

# Aplicar migraciones
python manage.py migrate

# Poblar base de datos con datos de prueba
python populate_data.py

# Ejecutar servidor
python manage.py runserver
```

El servidor estará disponible en `http://localhost:8000/api/`

#### 3. Configurar App Android

```bash
cd mobile

# Abrir proyecto en Android Studio
# File > Open > Seleccionar carpeta 'mobile'

# Sincronizar Gradle
# Android Studio sincronizará automáticamente
```

#### 4. Configurar Fuente de Datos

Editar `DataSourceConfig.kt`:

```kotlin
object DataSourceConfig {
    var currentDataSource: DataSourceType = DataSourceType.LOCAL_ROOM
    // Opciones: LOCAL_ROOM, REMOTE_API, HYBRID
}
```

- **LOCAL_ROOM**: Usa base de datos SQLite local (no requiere internet)
- **REMOTE_API**: Usa API Django (requiere servidor corriendo)
- **HYBRID**: Intenta API primero, fallback a local

### Ejecutar la Aplicación

1. Conectar dispositivo Android o iniciar emulador
2. En Android Studio: Run > Run 'app'
3. La app se instalará y abrirá automáticamente

## Funcionalidades Detalladas

### 1. Pantalla de Inicio (HomeScreen)

- Muestra doctores destacados (6 primeros)
- Búsqueda rápida por nombre
- Filtros por especialidad mediante chips
- Navegación al perfil de usuario
- Indicadores de disponibilidad de teleconsulta

### 2. Búsqueda de Doctores (SearchScreen)

**Filtros avanzados:**
- Búsqueda por nombre
- Especialidad médica (Cardiología, Medicina General, Pediatría, etc.)
- Ubicación/ciudad
- Solo teleconsulta
- Rango de precio

**Resultados:**
- Tarjetas con información resumida
- Próxima disponibilidad
- Precio por consulta
- Ícono de teleconsulta disponible

### 3. Detalle del Doctor (DoctorDetailScreen)

- Foto del doctor (o iniciales si no hay imagen)
- Nombre y especialidad
- Años de experiencia
- Ubicación del consultorio
- Precio por consulta
- Biografía profesional
- Información de teleconsulta
- Botón para agendar cita

### 4. Agendar Cita (BookingScreen)

**Proceso de reserva:**
1. Selección de fecha (calendario)
2. Selección de horario disponible
3. Tipo de consulta (presencial/teleconsulta)
4. Motivo de consulta (texto libre)
5. Confirmación con resumen

**Validaciones:**
- Fecha no puede ser en el pasado
- Horario debe estar disponible
- Motivo de consulta obligatorio

### 5. Mis Citas (AppointmentsScreen)

**Dos pestañas:**
- **Próximas**: Citas confirmadas o pendientes
- **Pasadas**: Citas completadas o canceladas

**Información mostrada:**
- Foto y nombre del doctor
- Especialidad
- Fecha y hora
- Tipo de consulta
- Estado de la cita
- Motivo de consulta

**Acciones disponibles:**
- Cancelar cita
- Reprogramar cita

### 6. Perfil de Usuario (ProfileScreen)

- Información personal del paciente
- Nombre, email, teléfono, fecha de nacimiento
- Configuración de tema (claro/oscuro)
- Modo edición (en desarrollo)

### 7. Confirmación (ConfirmationScreen)

- Resumen de cita agendada
- Información completa del doctor
- Fecha, hora y modalidad confirmada
- Botón para ver todas las citas
- Botón para volver al inicio

## Sistema de Datos Unificado

### Configuración de Fuentes de Datos

El sistema permite alternar entre tres modos:

#### LOCAL_ROOM (Por defecto)
```kotlin
DataSourceConfig.currentDataSource = DataSourceType.LOCAL_ROOM
```
- Usa Room Database (SQLite)
- Funciona 100% offline
- Datos de prueba incluidos
- Ideal para desarrollo

#### REMOTE_API
```kotlin
DataSourceConfig.currentDataSource = DataSourceType.REMOTE_API
```
- Usa Django REST API vía Retrofit
- Requiere servidor Django corriendo
- Datos persistentes en servidor
- Ideal para producción

#### HYBRID (Experimental)
```kotlin
DataSourceConfig.currentDataSource = DataSourceType.HYBRID
```
- Intenta usar API primero
- Fallback automático a Room si falla
- Mejor experiencia offline/online
- En desarrollo

### Ventajas del Sistema Unificado

1. **Sin cambios de código**: ViewModels no saben qué fuente usan
2. **Desarrollo flexible**: Trabajar offline con Room, probar con API
3. **Testing simplificado**: Cambiar fuente con una línea
4. **Escalabilidad**: Fácil agregar nuevas fuentes (Firebase, etc.)

## Guías de Uso

Para documentación detallada sobre el sistema de datos unificado, consulta:
- [GUIA_ROOM_RETROFIT.md](GUIA_ROOM_RETROFIT.md)

## Datos de Prueba

### Doctores Disponibles

| Nombre | Especialidad | Precio | Teleconsulta |
|--------|--------------|--------|--------------|
| Dra. María González | Cardiología | S/ 120 | Sí |
| Dr. Carlos Ramírez | Medicina General | S/ 80 | Sí |
| Dra. Ana Morales | Pediatría | S/ 100 | No |
| Dr. Luis Torres | Dermatología | S/ 110 | Sí |
| Dra. Patricia Vega | Neurología | S/ 150 | No |
| Dr. Roberto Flores | Traumatología | S/ 130 | No |

### Paciente de Prueba

- **Nombre**: Aldy Montoya
- **Email**: aldy.montoya@example.com
- **Teléfono**: 987654321

## API Endpoints

### Doctors
```
GET    /api/doctors/              # Lista todos los doctores
GET    /api/doctors/{id}/         # Detalle de doctor
POST   /api/doctors/              # Crear doctor
PUT    /api/doctors/{id}/         # Actualizar doctor
DELETE /api/doctors/{id}/         # Eliminar doctor
```

### Appointments
```
GET    /api/appointments/         # Lista todas las citas
GET    /api/appointments/{id}/    # Detalle de cita
POST   /api/appointments/         # Crear cita
PUT    /api/appointments/{id}/    # Actualizar cita
DELETE /api/appointments/{id}/    # Eliminar cita
```

### Patients
```
GET    /api/patients/             # Lista todos los pacientes
GET    /api/patients/{id}/        # Detalle de paciente
POST   /api/patients/             # Crear paciente
PUT    /api/patients/{id}/        # Actualizar paciente
DELETE /api/patients/{id}/        # Eliminar paciente
```

## Optimizaciones Realizadas

### Rendimiento

- Imágenes de doctores optimizadas (reducción del 92% en tamaño)
- LazyColumn para listas eficientes
- remember y derivedStateOf para evitar recomposiciones
- Coroutines con Dispatchers apropiados (IO para DB/Network)

### Base de Datos

- Índices en columnas frecuentes (especialidad, fecha)
- Converters para tipos complejos (Date, List, Enum)
- Queries optimizadas con Flow reactivo

### UI/UX

- Material Design 3 con componentes modernos
- Transiciones suaves entre pantallas
- Feedback visual inmediato
- Estados de carga y error manejados
- Modo oscuro completo

## Problemas Conocidos

### Imágenes en Dispositivos de Gama Baja
- **Problema**: Crash al cargar imágenes pesadas en dispositivos con poca RAM
- **Solución Aplicada**: Optimización de imágenes a máximo 50KB
- **Alternativa**: Usar solo iniciales (imageResId = 0)

### Edición de Perfil
- **Estado**: Interfaz existe pero funcionalidad incompleta
- **Pendiente**: Implementar persistencia de cambios
- **Workaround**: Datos hardcodeados actualmente


## Equipo de Desarrollo

| Rol | Nombre | Responsabilidades |
|-----|--------|-------------------|
| Líder Técnico | Rotativo | Arquitectura, Git/GitHub, Coordinación |
| Diseñador UI/UX | Aldy Montoya, Alfredo Navarro |  |
| Desarrolladores | Aldy Montoya, Alfredo Navarro |n |

## Diseño

Prototipo en Figma: [Ver Diseño](https://www.figma.com/make/QWOHgKKIefRiGSiYTL8zOg/MediTurn-App-Views?node-id=0-1&t=ycZM8o0khEL4Kazb-1)

## Recursos Adicionales

- [Documentación de Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Django REST Framework](https://www.django-rest-framework.org/)
- [Room Database](https://developer.android.com/training/data-storage/room)
- [Retrofit](https://square.github.io/retrofit/)

## Licencia

Este proyecto fue desarrollado como parte del curso de Programacion en Moviles con Android en TECSUP.

## Contacto

Para consultas sobre el proyecto:
- GitHub: [AlfredoTec/mediturn_montoya_navarro](https://github.com/AlfredoTec/mediturn_montoya_navarro)

---