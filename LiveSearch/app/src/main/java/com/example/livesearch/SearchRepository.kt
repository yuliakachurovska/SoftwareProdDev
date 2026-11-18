package com.example.livesearch

import android.util.Log
import kotlinx.coroutines.delay

class SearchRepository {

    private val items = listOf(
        "Україна", "Київ", "Володимир", "Львів", "Одеса", "Харків",
        "Дніпро", "Запоріжжя", "Вінниця", "Полтава", "Чернігів",
        "Черкаси", "Житомир", "Суми", "Рівне", "Луцьк", "Тернопіль",
        "Івано-Франківськ", "Ужгород", "Чернівці", "Хмельницький",
        "Польща", "Німеччина", "Франція", "Італія", "Іспанія", "Канада"
    )

    suspend fun search(query: String): List<String> {
        Log.d("SearchFlow", ">>> ЗАПИТ В БД: шукаємо '$query'")
        delay(500L)

        return items.filter { item ->
            item.contains(query.trim(), ignoreCase = true)
        }
    }
}