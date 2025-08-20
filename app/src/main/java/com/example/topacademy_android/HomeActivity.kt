package com.example.topacademy_android

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import android.widget.Toast

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val btnWeather = findViewById<Button>(R.id.btnWeather)
        val btnCalculator = findViewById<Button>(R.id.btnCalculator)

        btnWeather.setOnClickListener {
            // Переход к погодному экрану (MainActivity)
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnCalculator.setOnClickListener {
            // Переход к калькулятору
            val intent = Intent(this, com.example.topacademy_android.feature_calculator.presentation.CalculatorActivity::class.java)
            startActivity(intent)
        }
    }
}
