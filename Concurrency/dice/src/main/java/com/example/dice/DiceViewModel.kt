package com.example.dice

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DiceViewModel : ViewModel() {

    private val diceModel = DiceModel()
    private val currentDice = MutableList(5) { 6 }

    private val _diceValues = MutableLiveData<List<Int>>(currentDice.toList())
    val diceValues: LiveData<List<Int>>
        get() = _diceValues

    private val _isRolling = MutableLiveData<Boolean>(false)
    val isRolling: LiveData<Boolean>
        get() = _isRolling

    private val _buttonText = MutableLiveData<String>("ROLL")
    val buttonText: LiveData<String>
        get() = _buttonText

    private var rollJob: Job? = null

    init {
        Log.d(TAG, "DiceViewModel створено")
    }

    fun rollDice() {
        // Якщо натиснули "RESTART" під час обертання то скасовуємо поточну корутину
        rollJob?.cancel()

        rollJob = viewModelScope.launch {
            _isRolling.value = true
            _buttonText.value = "RESTART"

            val finalValues = diceModel.rollAllDice(diceCount = 5) { index, newValue ->
                currentDice[index] = newValue
                _diceValues.value = currentDice.toList()
            }

            // Виконається тільки після того, як усі 5 корутин завершать роботу
            _diceValues.value = finalValues
            _isRolling.value = false
            _buttonText.value = "ROLL"
        }
    }

    override fun onCleared() {
        super.onCleared()
        // viewModelScope автоматично скасовує rollJob при остаточному закритті екрана
        Log.d(TAG, "DiceViewModel onCleared: viewModelScope автоматично скасував корутини")
    }

    companion object {
        private const val TAG = "DiceCoroutines"
    }
}