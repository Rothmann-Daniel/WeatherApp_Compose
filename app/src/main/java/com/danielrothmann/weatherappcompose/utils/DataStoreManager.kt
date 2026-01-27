package com.danielrothmann.weatherappcompose.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        private val SELECTED_CITY_KEY = stringPreferencesKey("selected_city")
        private const val DEFAULT_CITY = "Moscow"
    }

    suspend fun saveSelectedCity(city: String) {
        dataStore.edit { preferences ->
            preferences[SELECTED_CITY_KEY] = city
        }
    }

    // метод, чтобы возвращать Flow<String>
    fun getSelectedCityFlow(): Flow<String> = dataStore.data
        .map { preferences ->
            preferences[SELECTED_CITY_KEY] ?: DEFAULT_CITY
        }

    //  метод для однократного получения
    suspend fun getSelectedCityOnce(): String {
        val preferences = dataStore.data.first()
        return preferences[SELECTED_CITY_KEY] ?: DEFAULT_CITY
    }
}