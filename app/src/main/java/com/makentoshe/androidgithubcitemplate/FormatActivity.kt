package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.content.Intent
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

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
        findViewById<Button>(R.id.b_continents).setOnClickListener {
            val intent = Intent(this, ContinentActivity::class.java)
            startActivity(intent)
        }
        findViewById<Button>(R.id.b_cities).setOnClickListener {
            val intent = Intent(this, CitiesActivity::class.java)
            startActivity(intent)
        }

    }
}