# MediTurn - Sistema de Citas Médicas
Aplicación móvil moderna para agendar citas médicas de forma rápida y sencilla.

## Sobre el Proyecto
MediTurn es una aplicación móvil desarrollada con Kotlin y Jetpack Compose que permite a los pacientes buscar médicos por especialidad, agendar citas, gestionar su calendario médico y recibir recordatorios.
Objetivo: Facilitar el acceso a servicios de salud mediante una plataforma intuitiva y moderna que conecte pacientes con profesionales médicos.
Curso: Aplicaciones Móviles con Android
Duración: 6 días
Tecnologías: Kotlin, Jetpack Compose, Material Design 3

## Equipo de Desarrollo
| **Rol**                | **Nombre**                        | **Responsabilidades**                                                                 |
|-------------------------|-----------------------------------|----------------------------------------------------------------------------------------|
| Líder Técnico           | Rotativo                          | Coordina el desarrollo, estructura el proyecto y controla Git/GitHub.                 |
| Diseñador UI/UX         | Aldy Montoya y Alfredo Navarro     | Diseña pantallas en Figma y define colores y estilo.                       |
| Tester / Documentador   | Aldy Montoya y Alfredo Navarro     | Elabora documentación, pruebas funcionales y validación visual.                       |


## Diseño
Prototipo en Figma
Link: [Link al Figma](https://www.figma.com/make/QWOHgKKIefRiGSiYTL8zOg/MediTurn-App-Views?node-id=0-1&t=ycZM8o0khEL4Kazb-1)

## Historias de Usuario
**HU-01: Búsqueda de Médicos**

Como paciente, quiero buscar médicos por especialidad y filtrar por disponibilidad para encontrar el profesional adecuado para mi necesidad

Criterios de Aceptación:

- Búsqueda por nombre de médico
- Filtrado por especialidad médica
- Visualización de disponibilidad inmediata

**Prioridad: Alta**

**HU-02: Agendar Cita Médica**

Como paciente, quiero agendar una cita médica seleccionando fecha, hora y modalidad para reservar una consulta con el médico elegido

Criterios de Aceptación:

- Selección de fecha disponible
- Selección de hora disponible
- Campo para motivo de consulta
- Visualización del costo total
- Confirmación de cita agendada

**Prioridad: Alta**

**HU-03: Visualizar Mis Citas**

Como paciente, quiero ver todas mis citas médicas (próximas y pasadas) para llevar un control de mis consultas

Criterios de Aceptación:

- Lista de citas próximas
- Historial de citas pasadas (pestaña separada)
- Información completa de cada cita (médico, especialidad, fecha, hora, modalidad)
- Acceso a detalles completos
- Ordenamiento por fecha
- Estado de cada cita visible

**Prioridad: Alta**

**HU-04: Reprogramar/Cancelar Cita**

Como paciente, quiero poder reprogramar o cancelar una cita existente para gestionar cambios en mi disponibilidad

Criterios de Aceptación:

- Acceso a opciones desde detalle de cita
- Selección de nueva fecha/hora al reprogramar
- Cancelación con confirmación
- Actualización correcta del estado
- Mensaje de confirmación

**Prioridad: Media**

**HU-05: Ver Perfil del Médico**

Como paciente, quiero ver información detallada de un médico para tomar una decisión informada antes de agendar

Criterios de Aceptación:

- Foto y nombre completo
- Especialidad y años de experiencia
- Ubicación del consultorio
- Horarios disponibles
- Precio de consulta
- Biografía profesional

**Prioridad: Alta**

**HU-06: Recordatorios de Citas**

Como paciente, quiero recibir recordatorios de mis próximas citas para no olvidar mis consultas médicas

Criterios de Aceptación:

- Notificación 24 horas antes
- Notificación 1 hora antes
- Información completa en notificación (médico, hora, especialidad)
- Acceso directo a cita desde notificación

**Prioridad: Media**

**HU-07: Perfil de Usuario**

Como paciente, quiero gestionar mi perfil personal para mantener mi información actualizada

Criterios de Aceptación:

- Visualización de perfil
- Edición de información personal
- Historial médico resumido
- Opción de cerrar sesión

**Prioridad: Baja**
