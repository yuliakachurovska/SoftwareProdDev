package com.example.eventlogcompose

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class BroadcastRepository(private val context: Context) {

    fun observeSystemEvents(): Flow<BroadcastEvent> = callbackFlow {
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(ctx: Context?, intent: Intent?) {
                val action = intent?.action ?: return
                val event = mapIntentToEvent(action, intent)
                // Неблокуюча відправка події у потік Flow
                trySend(event)
            }
        }

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            addAction(Intent.ACTION_BATTERY_LOW)
            addAction(Intent.ACTION_BATTERY_OKAY)
            addAction(Intent.ACTION_HEADSET_PLUG)
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }

        ContextCompat.registerReceiver(
            context,
            receiver,
            filter,
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        awaitClose {
            context.unregisterReceiver(receiver)
        }
    }

    private fun mapIntentToEvent(action: String, intent: Intent): BroadcastEvent {
        val now = Date()
        val shortTime = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(now)
        val fullTimestamp = SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS", Locale.getDefault()).format(now)

        val (title, description) = when (action) {
            Intent.ACTION_POWER_CONNECTED ->
                "Зарядний пристрій підключено" to "Пристрій під'єднано до зовнішнього джерела живлення."

            Intent.ACTION_POWER_DISCONNECTED ->
                "Зарядний пристрій відключено" to "Кабель живлення або бездротову зарядку від'єднано."

            Intent.ACTION_AIRPLANE_MODE_CHANGED -> {
                val isEnabled = intent.getBooleanExtra("state", false)
                val statusText = if (isEnabled) "УВІМКНЕНО" else "ВИМКНЕНО"
                "Режим «У літаку»: $statusText" to "Стан авіарежиму змінено на $statusText."
            }

            Intent.ACTION_BATTERY_LOW ->
                "Низький заряд батареї" to "Рівень заряду акумулятора впав до критичного порогу."

            Intent.ACTION_BATTERY_OKAY ->
                "Заряд батареї в нормі" to "Рівень заряду відновився після критично низького стану."

            Intent.ACTION_HEADSET_PLUG -> {
                val state = intent.getIntExtra("state", -1)
                val name = intent.getStringExtra("name") ?: "Невідомий пристрій"
                val status = if (state == 1) "підключено" else "відключено"
                "Дротові навушники $status" to "Аудіогарнітура ($name) була $status."
            }

            Intent.ACTION_SCREEN_ON ->
                "Екран увімкнено" to "Дисплей пристрою активовано."

            Intent.ACTION_SCREEN_OFF ->
                "Екран вимкнено" to "Дисплей пристрою заблоковано або вимкнено."

            else -> action to "Отримано системне широкомовне сповіщення."
        }

        val extrasText = formatExtras(intent)
        val fullDetails = buildString {
            appendLine("Action: $action")
            appendLine("Часовий штамп: $fullTimestamp")
            appendLine("Опис: $description")
            append("Додаткові параметри (Extras): $extrasText")
        }

        return BroadcastEvent(
            id = UUID.randomUUID().toString(),
            type = title,
            timestamp = shortTime,
            details = fullDetails
        )
    }

    private fun formatExtras(intent: Intent): String {
        val extras = intent.extras ?: return "відсутні"
        val keys = extras.keySet()
        if (keys.isEmpty()) return "відсутні"

        return keys.joinToString(separator = ", ") { key ->
            @Suppress("DEPRECATION")
            val value = extras.get(key)
            "$key = $value"
        }
    }
}