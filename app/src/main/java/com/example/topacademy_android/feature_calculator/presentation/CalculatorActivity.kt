package com.example.topacademy_android.feature_calculator.presentation

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.topacademy_android.R
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorActivity : AppCompatActivity() {
    private val viewModel: CalculatorViewModel by viewModel()
    private lateinit var tvExpression: TextView
    private lateinit var tvResult: TextView
    private var expression: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculator)

        tvExpression = findViewById(R.id.tvExpression)
        tvResult = findViewById(R.id.tvResult)

        val buttons = listOf(
            R.id.btn0, R.id.btn1, R.id.btn2, R.id.btn3, R.id.btn4, R.id.btn5, R.id.btn6, R.id.btn7, R.id.btn8, R.id.btn9
        )
        val ops = mapOf(
            R.id.btnPlus to "+",
            R.id.btnMinus to "-",
            R.id.btnMultiply to "*",
            R.id.btnDivide to "/"
        )

        buttons.forEach { id ->
            findViewById<Button>(id).setOnClickListener {
                val digit = (it as Button).text.toString()
                if (expression == "0") expression = digit else expression += digit
                tvExpression.text = expression
            }
        }
        ops.forEach { (id, op) ->
            findViewById<Button>(id).setOnClickListener {
                if (expression.isNotEmpty() && !"+-*/".contains(expression.last())) {
                    expression += op
                    tvExpression.text = expression
                }
            }
        }
        findViewById<Button>(R.id.btnDot).setOnClickListener {
            if (expression.isNotEmpty() && expression.last().isDigit()) {
                expression += "."
                tvExpression.text = expression
            }
        }
        findViewById<Button>(R.id.btnClear).setOnClickListener {
            expression = ""
            tvExpression.text = "0"
            tvResult.text = ""
        }
        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.dropLast(1)
                tvExpression.text = if (expression.isEmpty()) "0" else expression
            }
        }
        findViewById<Button>(R.id.btnEquals).setOnClickListener {
            if (expression.isNotEmpty()) {
                viewModel.onCalculate(expression)
            }
        }
        viewModel.result.observe(this) { result ->
            tvResult.text = result
        }
    }
}
