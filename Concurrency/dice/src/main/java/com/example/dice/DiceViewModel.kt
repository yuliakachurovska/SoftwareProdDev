package com.example.dice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.concurrent.thread
import kotlin.random.Random

class DiceViewModel : ViewModel() {

    // Поточні значення 5 кісток (за замовчуванням усі 6, як у стартовій розмітці)
    private val currentDice = MutableList(5) { 6 }

    private val _diceValues = MutableLiveData<List<Int>>(currentDice.toList())
    val diceValues: LiveData<List<Int>>
        get() = _diceValues

    // Стан обертання: true — кістки крутяться, false — усі зупинилися
    private val _isRolling = MutableLiveData<Boolean>(false)
    val isRolling: LiveData<Boolean>
        get() = _isRolling

    // Об'єкт синхронізації та список активних потоків для безпечного очищення
    private val lock = Any()
    private var activeThreadsCount = 0
    private val runningThreads = mutableListOf<Thread>()

    @Volatile
    private var isCleared = false

    init {
        Log.d(TAG, "DiceViewModel створено")
    }

    fun rollDice() {
        // Захист від повторного запуску, якщо кістки вже обертаються
        if (_isRolling.value == true) return

        _isRolling.value = true

        synchronized(lock) {
            activeThreadsCount = 5
            runningThreads.clear()
        }

        // Частина 3: запускаємо окремий фоновий потік для кожної з 5 кісток
        for (dieIndex in 0 until 5) {
            // Випадкова тривалість обертання для кожної кістки: від 1.5 сек (15 кроків) до 3.0 сек (30 кроків)
            val iterations = Random.nextInt(15, 31)

            val workerThread = thread(name = "DiceThread-${dieIndex + 1}") {
                try {
                    for (step in 1..iterations) {
                        if (isCleared || Thread.currentThread().isInterrupted) return@thread

                        Thread.sleep(100L)

                        if (isCleared || Thread.currentThread().isInterrupted) return@thread

                        val randomFace = Random.nextInt(1, 7) // Випадкова грань від 1 до 6
                        val snapshot: List<Int>

                        synchronized(lock) {
                            currentDice[dieIndex] = randomFace
                            snapshot = currentDice.toList()
                        }

                        // Безпечно передаємо оновлений список із фонового потоку в Main Thread
                        _diceValues.postValue(snapshot)
                    }
                } catch (e: InterruptedException) {
                    Log.d(TAG, "Потік кістки #${dieIndex + 1} перервано")
                    return@thread
                } finally {
                    var allFinished = false
                    synchronized(lock) {
                        activeThreadsCount--
                        if (activeThreadsCount <= 0) {
                            allFinished = true
                        }
                    }

                    // Розблоковуємо кнопку ROLL тільки після завершення ВСІХ 5 потоків
                    if (allFinished && !isCleared) {
                        Log.d(TAG, "Усі 5 кісток зупинилися!")
                        _isRolling.postValue(false)
                    }
                }
            }

            synchronized(lock) {
                runningThreads.add(workerThread)
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d(TAG, "DiceViewModel onCleared: зупиняємо всі фонові потоки")
        isCleared = true
        synchronized(lock) {
            runningThreads.forEach { it.interrupt() }
            runningThreads.clear()
        }
    }

    companion object {
        private const val TAG = "DiceMVVM"
    }
}