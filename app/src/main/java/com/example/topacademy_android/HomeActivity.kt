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
            // Заглушка для калькулятора
            Toast.makeText(this, "Калькулятор в разработке", Toast.LENGTH_SHORT).show()
        }
    }
}

