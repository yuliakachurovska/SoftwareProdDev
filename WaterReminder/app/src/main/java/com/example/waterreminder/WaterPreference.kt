package com.example.waterreminder

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.waterDataStore: DataStore<Preferences> by preferencesDataStore(name = "water_settings")

class WaterPreferences(private val context: Context) {

    private object Keys {
        val REMINDER_COUNT = intPreferencesKey("reminder_count")
    }

    val reminderCountFlow: Flow<Int> = context.waterDataStore.data
        .map { preferences ->
            preferences[Keys.REMINDER_COUNT] ?: 0
        }

    suspend fun incrementReminderCount() {
        context.waterDataStore.edit { preferences ->
            val current = preferences[Keys.REMINDER_COUNT] ?: 0
            preferences[Keys.REMINDER_COUNT] = current + 1
        }
    }
}