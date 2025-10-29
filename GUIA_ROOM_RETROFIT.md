# Guía de Uso: Sistema Unificado Room y Retrofit

## Introducción

Este documento explica cómo utilizar el sistema de fuentes de datos unificado implementado en MediTurn. El sistema permite alternar entre dos fuentes de datos sin modificar el código de los ViewModels:

- **Room**: Base de datos SQLite local que funciona sin conexión a Internet
- **Retrofit**: Cliente HTTP que consume la API REST de Django

## Arquitectura del Sistema

### Componentes Principales

1. **DataSourceConfig**: Clase singleton que mantiene la configuración global de la fuente de datos activa
2. **UnifiedDoctorRepository**: Repositorio que delega operaciones a Room o Retrofit según la configuración
3. **UnifiedAppointmentRepository**: Similar al anterior, pero para operaciones con citas médicas
4. **DataSourceSettingsScreen**: Interfaz de usuario para cambiar la fuente de datos

### Flujo de Datos

```
ViewModel
    |
    v
UnifiedRepository (decide según DataSourceConfig)
    |
    +---> Room (local)
    |
    +---> Retrofit (remoto)
```

## Configuración

### Cambiar la Fuente de Datos

#### Método 1: Configuración en Código

Editar el archivo `DataSourceConfig.kt`:

```kotlin
object DataSourceConfig {
    var currentDataSource: DataSourceType = DataSourceType.LOCAL_ROOM
}
```

Valores disponibles:
- `DataSourceType.LOCAL_ROOM` - Usa Room (base de datos local)
- `DataSourceType.REMOTE_API` - Usa Retrofit (API remota)
- `DataSourceType.HYBRID` - Modo híbrido (implementación futura)

#### Método 2: Interfaz de Usuario

Integrar `DataSourceSettingsScreen` en la navegación de la aplicación:

```kotlin
// En NavGraph.kt
composable("datasource-settings") {
    DataSourceSettingsScreen(
        onNavigateBack = { navController.popBackStack() }
    )
}
```

Agregar navegación desde otra pantalla:

```kotlin
Button(onClick = { navController.navigate("datasource-settings") }) {
    Text("Configurar Fuente de Datos")
}
```

## Uso en ViewModels

### Inicialización de Repositorios

```kotlin
// Obtener instancia de la base de datos
val database = AppDatabase.getInstance(context)

// Crear repositorio unificado para doctores
val unifiedDoctorRepository = UnifiedDoctorRepository(
    doctorDao = database.doctorDao()
)

// Crear repositorio unificado para citas
val unifiedAppointmentRepository = UnifiedAppointmentRepository(
    appointmentDao = database.appointmentDao()
)
```

### Crear ViewModels con Repositorios Unificados

```kotlin
val homeViewModel = HomeViewModel(
    unifiedRepository = unifiedDoctorRepository
)

val searchViewModel = SearchViewModel(
    unifiedRepository = unifiedDoctorRepository
)

val appointmentsViewModel = AppointmentsViewModel(
    unifiedRepository = unifiedAppointmentRepository
)
```

### ViewModels Compatibles

Los siguientes ViewModels han sido actualizados para soportar repositorios unificados:

- HomeViewModel
- SearchViewModel
- AppointmentsViewModel

Todos mantienen compatibilidad con los repositorios específicos (Room o Retrofit) mediante parámetros opcionales.

## Requisitos por Fuente de Datos

### Para Usar Room (LOCAL_ROOM)

- Base de datos SQLite inicializada
- Datos de ejemplo pre-cargados mediante SampleData
- No requiere conexión a Internet
- No requiere configuración adicional

### Para Usar Retrofit (REMOTE_API)

1. Servidor Django en ejecución:
   ```bash
   cd Django/mediturnapi
   python manage.py runserver
   ```

2. Datos poblados en la base de datos Django:
   ```bash
   python populate_data.py
   ```

3. Configuración de red:
   - Emulador Android: URL `http://10.0.2.2:8000/api/`
   - Dispositivo físico: URL `http://[IP_LOCAL]:8000/api/`

4. Permiso de Internet en AndroidManifest.xml:
   ```xml
   <uses-permission android:name="android.permission.INTERNET" />
   ```

## Procedimientos de Testing

### Test 1: Verificar Funcionamiento con Room

1. Configurar DataSourceConfig:
   ```kotlin
   DataSourceConfig.currentDataSource = DataSourceType.LOCAL_ROOM
   ```

2. Ejecutar la aplicación sin servidor Django activo

3. Verificar que la aplicación muestra datos de ejemplo

4. Resultado esperado: La aplicación funciona normalmente con datos locales

### Test 2: Verificar Funcionamiento con Retrofit

1. Iniciar servidor Django:
   ```bash
   python manage.py runserver
   ```

2. Configurar DataSourceConfig:
   ```kotlin
   DataSourceConfig.currentDataSource = DataSourceType.REMOTE_API
   ```

3. Ejecutar la aplicación en emulador

4. Abrir Logcat y filtrar por "OkHttp"

5. Resultado esperado:
   - Se observan peticiones HTTP en los logs
   - La aplicación muestra datos desde el servidor

### Test 3: Cambio Dinámico de Fuente

1. Iniciar aplicación con Room configurado

2. Navegar a DataSourceSettingsScreen

3. Cambiar a Retrofit

4. Regresar a pantalla principal

5. Resultado esperado: Los datos se actualizan según la nueva fuente

## Debugging

### Verificar Fuente Activa

```kotlin
val sourceName = when (DataSourceConfig.currentDataSource) {
    DataSourceType.LOCAL_ROOM -> "Room (Local)"
    DataSourceType.REMOTE_API -> "Retrofit (API)"
    DataSourceType.HYBRID -> "Híbrido"
}
Log.d("DATA_SOURCE", "Fuente activa: $sourceName")
```

### Logs de Retrofit

Filtrar Logcat por: `OkHttp`

Ejemplo de salida esperada:
```
D/OkHttp: --> GET http://10.0.2.2:8000/api/doctors/
D/OkHttp: <-- 200 OK (156ms)
```

### Logs de Room

Habilitar logging de Room en build.gradle.kts:
```kotlin
kapt {
    arguments {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
        arg("room.logging", "true")
    }
}
```

## Estructura de Archivos

```
mobile/app/src/main/java/com/tecsup/mediturn/
├── data/
│   ├── DataSourceConfig.kt
│   └── repository/
│       ├── UnifiedDoctorRepository.kt
│       ├── UnifiedAppointmentRepository.kt
│       ├── DoctorRepository.kt (Room)
│       ├── RemoteDoctorRepository.kt (Retrofit)
│       ├── AppointmentRepository.kt (Room)
│       └── RemoteAppointmentRepository.kt (Retrofit)
└── ui/screens/
    ├── home/HomeViewModel.kt (actualizado)
    ├── search/SearchViewModel.kt (actualizado)
    ├── appointments/AppointmentsViewModel.kt (actualizado)
    └── settings/DataSourceSettingsScreen.kt (nuevo)
```

## API de los Repositorios Unificados

### UnifiedDoctorRepository

```kotlin
class UnifiedDoctorRepository(doctorDao: DoctorDao? = null) {

    // Obtener todos los doctores como Flow
    fun getAllDoctorsFlow(): Flow<List<Doctor>>

    // Obtener todos los doctores de forma síncrona
    fun getAllDoctors(): List<Doctor>

    // Obtener doctor por ID
    fun getDoctorById(id: String): Doctor?

    // Búsqueda avanzada con filtros
    fun searchDoctorsAdvanced(
        query: String = "",
        specialty: Specialty? = null,
        city: String? = null,
        teleconsultation: Boolean? = null
    ): Flow<List<Doctor>>

    // Obtener nombre de fuente actual
    fun getCurrentDataSourceName(): String
}
```

### UnifiedAppointmentRepository

```kotlin
class UnifiedAppointmentRepository(appointmentDao: AppointmentDao? = null) {

    // Obtener todas las citas
    fun getAllAppointmentsFlow(): Flow<List<Appointment>>

    // Obtener citas próximas
    fun getUpcomingAppointmentsFlow(): Flow<List<Appointment>>

    // Obtener citas pasadas
    fun getPastAppointmentsFlow(): Flow<List<Appointment>>

    // Obtener cita por ID
    fun getAppointmentById(id: String): Appointment?

    // Agregar nueva cita
    fun addAppointment(appointment: Appointment)

    // Cancelar cita
    fun cancelAppointment(id: String)

    // Reprogramar cita
    fun rescheduleAppointment(id: String, newDate: Date)
}
```

## Consideraciones Importantes

### Compatibilidad Retroactiva

Los ViewModels actualizados mantienen compatibilidad con repositorios específicos mediante parámetros opcionales:

```kotlin
class HomeViewModel(
    private val doctorRepository: DoctorRepository? = null,      // Legacy
    private val unifiedRepository: UnifiedDoctorRepository? = null  // Nuevo
)
```

Esto permite migración gradual sin romper código existente.

### Persistencia de Configuración

La configuración actual NO persiste entre reinicios de la aplicación. Para implementar persistencia:

```kotlin
// Guardar con DataStore
suspend fun saveDataSource(type: DataSourceType) {
    dataStore.edit { preferences ->
        preferences[DATA_SOURCE_KEY] = type.name
    }
}

// Cargar al iniciar
suspend fun loadDataSource() {
    val saved = dataStore.data.first()[DATA_SOURCE_KEY]
    if (saved != null) {
        DataSourceConfig.currentDataSource = DataSourceType.valueOf(saved)
    }
}
```

### Modo Híbrido

El modo `HYBRID` está definido pero no completamente implementado. Su propósito es:

- Usar Room como caché local
- Sincronizar con API cuando hay conexión
- Funcionar offline con datos cacheados

## Resolución de Problemas

### Problema: La aplicación no muestra datos con Retrofit

Verificar:
1. Servidor Django está en ejecución
2. URL base correcta en RetrofitClient.kt
3. Permiso de Internet en AndroidManifest.xml
4. Logs en Logcat (filtro: OkHttp)

### Problema: Error "Connection refused"

Solución:
- Verificar que ALLOWED_HOSTS en Django incluye '*' o la IP correcta
- Verificar firewall no bloquea puerto 8000

### Problema: ViewModels no usan repositorio unificado

Solución:
- Verificar que se pasa el parámetro `unifiedRepository` al crear el ViewModel
- Verificar que DataSourceConfig tiene un valor válido

### Problema: Cambio de fuente no tiene efecto

Solución:
- El cambio afecta nuevas instancias de Flow
- Puede requerir reiniciar la pantalla o recolectar el Flow nuevamente

## Conclusión

El sistema de repositorios unificados proporciona flexibilidad para desarrollo, testing y producción sin modificar código de presentación. La implementación permite transición gradual desde datos locales a remotos según las necesidades del proyecto.

Para soporte adicional, revisar los comentarios en el código fuente de cada componente.
