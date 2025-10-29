package com.tecsup.mediturn.data

/**
 * Configuración global de la fuente de datos
 * Permite cambiar fácilmente entre Room (local) y Retrofit (remoto)
 */
object DataSourceConfig {

    /**
     * Tipo de fuente de datos a usar
     */
    enum class DataSourceType {
        /**
         * Base de datos local con Room
         * - Funciona sin conexión a Internet
         * - Datos de ejemplo pre-cargados
         * - Ideal para desarrollo y demos
         */
        LOCAL_ROOM,

        /**
         * API remota con Retrofit
         * - Requiere conexión a Internet
         * - Datos desde el servidor Django
         * - Ideal para producción y testing de API
         */
        REMOTE_API,

        /**
         * Híbrido: Room como caché + Retrofit
         * - Funciona offline con datos cacheados
         * - Sincroniza con el servidor cuando hay conexión
         * - Ideal para apps productivas (futura implementación)
         */
        HYBRID
    }

    /**
     * Fuente de datos actual
     * Cambiar este valor para usar Room o Retrofit globalmente
     *
     * LOCAL_ROOM   = Usa base de datos local (Room)
     * REMOTE_API   = Usa API remota (Retrofit)
     * HYBRID       = Usa ambos (no implementado aún)
     */
    var currentDataSource: DataSourceType = DataSourceType.LOCAL_ROOM

    /**
     * Verifica si se está usando la fuente local
     */
    fun isUsingLocalData(): Boolean {
        return currentDataSource == DataSourceType.LOCAL_ROOM
    }

    /**
     * Verifica si se está usando la fuente remota
     */
    fun isUsingRemoteData(): Boolean {
        return currentDataSource == DataSourceType.REMOTE_API
    }

    /**
     * Verifica si se está usando modo híbrido
     */
    fun isUsingHybridMode(): Boolean {
        return currentDataSource == DataSourceType.HYBRID
    }
}
