package com.example.calculatortask01

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "CalculatorApp"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val firstNumberEditText = findViewById<EditText>(R.id.firstNumberEditText)
        val secondNumberEditText = findViewById<EditText>(R.id.secondNumberEditText)

        val addButton = findViewById<Button>(R.id.addButton)
        val subtractButton = findViewById<Button>(R.id.subtractButton)
        val multiplyButton = findViewById<Button>(R.id.multiplyButton)
        val divideButton = findViewById<Button>(R.id.divideButton)

        val clearButton = findViewById<Button>(R.id.clearButton)
        val resultTextView = findViewById<TextView>(R.id.resultTextView)

        fun performOperation(operator: String) {
            val num1Str = firstNumberEditText.text.toString()
            val num2Str = secondNumberEditText.text.toString()

            if (num1Str.isBlank() || num2Str.isBlank()) {
                Log.w(TAG, "Спроба виконати операцію $operator з порожніми полями")
                resultTextView.text = "Будь ласка, введіть обидва числа"
                return
            }

            val num1 = num1Str.toDoubleOrNull()
            val num2 = num2Str.toDoubleOrNull()

            if (num1 == null || num2 == null) {
                Log.w(TAG, "Помилка перетворення тексту в числа")
                resultTextView.text = "Некоректне число"
                return
            }

            Log.d(TAG, "Виконується операція: $num1 $operator $num2")

            val result = when (operator) {
                "+" -> num1 + num2
                "-" -> num1 - num2
                "*" -> num1 * num2
                "/" -> {
                    if (num2 == 0.0) {
                        Log.w(TAG, "Помилка: ділення на нуль!")
                        resultTextView.text = "Помилка: ділення на нуль неможливе!"
                        return
                    } else {
                        num1 / num2
                    }
                }
                else -> 0.0
            }

            resultTextView.text = "Результат: $result"
        }

        addButton.setOnClickListener { performOperation("+") }
        subtractButton.setOnClickListener { performOperation("-") }
        multiplyButton.setOnClickListener { performOperation("*") }
        divideButton.setOnClickListener { performOperation("/") }

        clearButton.setOnClickListener {
            firstNumberEditText.text.clear()
            secondNumberEditText.text.clear()
            resultTextView.text = "Результат: "
            Log.d(TAG, "Поля калькулятора очищено")
        }
    }
}