package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent

class FormatActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_format)

        findViewById<Button>(R.id.b_flags).setOnClickListener {
            val intent = Intent(this, CountryByFlagQuizActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.b_capitals).setOnClickListener {
            val intent = Intent(this, CapitalByCountryQuizActivity::class.java)
            startActivity(intent)
        }

    }
}