package com.example.dice

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlin.random.Random

class DiceModel {

    suspend fun rollAllDice(
        diceCount: Int = 5,
        onDieUpdated: (dieIndex: Int, newValue: Int) -> Unit
    ): List<Int> = coroutineScope {
        val deferredDice = (0 until diceCount).map { index ->
            async {
                rollSingleDie(index, onDieUpdated)
            }
        }
        // Чекаємо, поки всі 5 кісток завершать своє обертання
        deferredDice.awaitAll()
    }

    suspend fun rollSingleDie(
        dieIndex: Int,
        onDieUpdated: (dieIndex: Int, newValue: Int) -> Unit
    ): Int {
        // Випадкова тривалість для кожної кістки: від 1.5 сек (15 кроків) до 3.0 сек (30 кроків)
        val iterations = Random.nextInt(15, 31)
        var currentValue = 6

        repeat(iterations) {
            delay(100L)
            currentValue = Random.nextInt(1, 7)
            onDieUpdated(dieIndex, currentValue)
        }

        return currentValue
    }
}