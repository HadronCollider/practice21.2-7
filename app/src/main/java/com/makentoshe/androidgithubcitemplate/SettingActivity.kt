package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View

class SettingActivity : AppCompatActivity() {
    var radiobn: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_setting)

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)

        radiobn = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0)

        when (radiobn){
            0 -> radioGroup.check(R.id.radioclassic)
            1 -> radioGroup.check(R.id.radioclassic)
            2 -> radioGroup.check(R.id.radiotime)
            3 -> radioGroup.check(R.id.radio3)
        }

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioclassic -> radiobn = 1
                R.id.radiotime -> radiobn = 2
                R.id.radio3 -> radiobn = 3
            }
        }


        findViewById<Button>(R.id.buttondone).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putInt("limitations", radiobn)
            editor.apply()
            startActivity(intent)
        }
    }

    }