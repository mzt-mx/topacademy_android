package com.example.topacademy_android

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.topacademy_android.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "LIFECYCLE_TIMING"
    }

    private lateinit var binding: ActivityMainBinding

    private var createTime: Long = 0
    private var startTime: Long = 0
    private var resumeTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createTime = System.currentTimeMillis()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

<<<<<<< HEAD
        binding.button.setOnClickListener {
            binding.twTitle.text = getString(R.string.creatorName)
        }

        Log.i(ON_CREATE, "Активити создана!")
=======
        Log.i(TAG, "onCreate вызван в $createTime мс")
>>>>>>> c77619caad9d49f7341fdf848208ac09c02942dc
    }

    override fun onStart() {
        super.onStart()
        startTime = System.currentTimeMillis()
        val diff = startTime - createTime
        Log.i(TAG, "onStart вызван в $startTime мс, прошло $diff мс с onCreate")
    }

    override fun onResume() {
        super.onResume()
        resumeTime = System.currentTimeMillis()
        val diffFromCreate = resumeTime - createTime
        val diffFromStart = resumeTime - startTime
        Log.i(TAG, "onResume вызван в $resumeTime мс, прошло $diffFromCreate мс с onCreate, $diffFromStart мс с onStart")
    }
}
