
package com.example.calculatorfull

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.calculatorfull.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    private lateinit var binding: ActivityMainBinding

    private var firstNumber = 0.0
    private var operation = ""
    private var isNewNumber = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val numberButtons = listOf(
            binding.btn0, binding.btn1, binding.btn2,
            binding.btn3, binding.btn4, binding.btn5,
            binding.btn6, binding.btn7, binding.btn8,
            binding.btn9
        )

        numberButtons.forEach { button ->
            button.setOnClickListener {
                enterNumber(button.text.toString())
            }
        }

        binding.btnPlus.setOnClickListener {
            chooseOperation("+")
        }

        binding.btnMinus.setOnClickListener {
            chooseOperation("-")
        }

        binding.btnMultiply.setOnClickListener {
            chooseOperation("*")
        }

        binding.btnDivide.setOnClickListener {
            chooseOperation("/")
        }

        binding.btnEquals.setOnClickListener {
            calculate()
        }

        binding.btnC.setOnClickListener {
            clear()
        }

        binding.btnDot.setOnClickListener {
            enterDot()
        }

        binding.btnBackspace.setOnClickListener {
            deleteLast()
        }

        Log.d(TAG, "Калькулятор запущено")
    }

    private fun enterNumber(number: String) {
        val current = binding.displayTextView.text.toString()

        if (current == "Помилка" || isNewNumber) {
            binding.displayTextView.text = number
            isNewNumber = false
        } else if (current == "0") {
            binding.displayTextView.text = number
        } else {
            binding.displayTextView.append(number)
        }
    }

    private fun enterDot() {
        val current = binding.displayTextView.text.toString()

        if (isNewNumber || current == "Помилка") {
            binding.displayTextView.text = "0."
            isNewNumber = false
        } else if (!current.contains(".")) {
            binding.displayTextView.append(".")
        }
    }

    private fun chooseOperation(op: String) {
        val current = binding.displayTextView.text.toString().toDoubleOrNull()

        if (current == null) return

        if (operation.isNotEmpty() && !isNewNumber) {
            calculate()
        }

        val number = binding.displayTextView.text.toString().toDoubleOrNull()

        if (number != null) {
            firstNumber = number
            operation = op
            isNewNumber = true

            Log.d(TAG, "Перше число: $firstNumber")
            Log.d(TAG, "Операція: $operation")
        }
    }

    private fun calculate() {
        if (operation.isEmpty() || isNewNumber) return

        val secondNumber = binding.displayTextView.text.toString().toDoubleOrNull()
            ?: return

        val result = when (operation) {
            "+" -> firstNumber + secondNumber
            "-" -> firstNumber - secondNumber
            "*" -> firstNumber * secondNumber
            "/" -> {
                if (secondNumber == 0.0) {
                    binding.displayTextView.text = "Помилка"
                    operation = ""
                    isNewNumber = true
                    Log.e(TAG, "Ділення на нуль")
                    return
                }
                firstNumber / secondNumber
            }
            else -> return
        }

        showResult(result)

        Log.d(TAG, "Результат: $result")

        firstNumber = result
        operation = ""
        isNewNumber = true
    }

    private fun showResult(number: Double) {
        binding.displayTextView.text = if (number % 1.0 == 0.0) {
            number.toLong().toString()
        } else {
            number.toString()
        }
    }

    private fun deleteLast() {
        val current = binding.displayTextView.text.toString()

        if (isNewNumber || current == "Помилка") {
            binding.displayTextView.text = "0"
            isNewNumber = false
            return
        }

        if (current.length > 1) {
            val newValue = current.dropLast(1)
            binding.displayTextView.text =
                if (newValue == "-" || newValue.isEmpty()) "0" else newValue
        } else {
            binding.displayTextView.text = "0"
        }
    }

    private fun clear() {
        firstNumber = 0.0
        operation = ""
        isNewNumber = false

        binding.displayTextView.text = "0"

        Log.d(TAG, "Калькулятор очищено")
    }
}
