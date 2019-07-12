package com.example.chronometer

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Chronometer
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val chronometer = findViewById<Chronometer>(R.id.chronometer)

        val button = findViewById<Button>(R.id.button)
        button?.setOnClickListener(object : View.OnClickListener {

            internal var isPlaying = false

            override fun onClick(v: View) {
                if (!isPlaying) {
                    chronometer.start()
                    isPlaying = true
                } else {
                    chronometer.stop()
                    isPlaying = false
                }

                button.setText(if (isPlaying) R.string.start else R.string.stop)
                Toast.makeText(this@MainActivity, getString(if (isPlaying) R.string.playing else R.string.stopped), Toast.LENGTH_SHORT).show()
            }
        })
    }
}
