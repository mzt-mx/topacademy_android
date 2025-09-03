package com.example.topacademy_android.feature_calculator.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.topacademy_android.databinding.FragmentCalculatorBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class CalculatorFragment : Fragment() {
    private var _binding: FragmentCalculatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CalculatorViewModel by viewModel()
    private var expression: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCalculatorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttons: List<Button> = listOf(
            binding.btn0, binding.btn1, binding.btn2, binding.btn3, binding.btn4,
            binding.btn5, binding.btn6, binding.btn7, binding.btn8, binding.btn9
        )
        val ops: Map<Button, String> = mapOf(
            binding.btnPlus to "+",
            binding.btnMinus to "-",
            binding.btnMultiply to "*",
            binding.btnDivide to "/"
        )
        buttons.forEach { btn: Button ->
            btn.setOnClickListener {
                val digit = btn.text.toString()
                if (expression == "0") expression = digit else expression += digit
                binding.tvExpression.text = expression
            }
        }
        ops.forEach { (btn: Button, op: String) ->
            btn.setOnClickListener {
                if (expression.isNotEmpty() && !"+-*/".contains(expression.last())) {
                    expression += op
                    binding.tvExpression.text = expression
                }
            }
        }
        binding.btnEquals.setOnClickListener {
            if (expression.isNotEmpty()) {
                try {
                    val result = eval(expression)
                    val formatted = if (result % 1.0 == 0.0) {
                        result.toInt().toString()
                    } else {
                        String.format("%.2f", result)
                    }
                    binding.tvResult.text = formatted
                } catch (e: Exception) {
                    binding.tvResult.text = "Ошибка"
                }
            }
        }
        binding.btnClear.setOnClickListener {
            expression = ""
            binding.tvExpression.text = "0"
            binding.tvResult.text = ""
        }
        binding.btnDelete.setOnClickListener {
            if (expression.isNotEmpty()) {
                expression = expression.dropLast(1)
                binding.tvExpression.text = if (expression.isEmpty()) "0" else expression
            }
        }
    }

    private fun eval(expr: String): Double {
        // Парсер выражений
        return object {
            var pos = -1
            var ch = 0
            fun nextChar() { ch = if (++pos < expr.length) expr[pos].toInt() else -1 }
            fun eat(charToEat: Int): Boolean {
                while (ch == ' '.toInt()) nextChar()
                if (ch == charToEat) { nextChar(); return true }
                return false
            }
            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < expr.length) throw RuntimeException("Unexpected: " + expr[pos])
                return x
            }
            fun parseExpression(): Double {
                var x = parseTerm()
                while(true) {
                    when {
                        eat('+'.toInt()) -> x += parseTerm()
                        eat('-'.toInt()) -> x -= parseTerm()
                        else -> return x
                    }
                }
            }
            fun parseTerm(): Double {
                var x = parseFactor()
                while(true) {
                    when {
                        eat('*'.toInt()) -> x *= parseFactor()
                        eat('/'.toInt()) -> x /= parseFactor()
                        else -> return x
                    }
                }
            }
            fun parseFactor(): Double {
                if (eat('+'.toInt())) return parseFactor() // unary plus
                if (eat('-'.toInt())) return -parseFactor() // unary minus
                var x: Double
                val startPos = pos
                if (ch in '0'.toInt()..'9'.toInt() || ch == '.'.toInt()) {
                    while (ch in '0'.toInt()..'9'.toInt() || ch == '.'.toInt()) nextChar()
                    x = expr.substring(startPos, pos).toDouble()
                } else {
                    throw RuntimeException("Unexpected: " + ch.toChar())
                }
                return x
            }
        }.parse()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
