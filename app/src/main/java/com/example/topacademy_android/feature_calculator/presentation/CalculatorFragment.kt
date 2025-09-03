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
                viewModel.onCalculate(expression)
            }
        }
        viewModel.result.observe(viewLifecycleOwner) { formatted ->
            binding.tvResult.text = formatted
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
