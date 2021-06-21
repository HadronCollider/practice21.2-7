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
import android.widget.EditText
import android.widget.TextView

class SettingActivity : AppCompatActivity() {
    var radiobn: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_setting)

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        val num_oq_label = findViewById<TextView>(R.id.numOQ)
        val num_oq_edit = findViewById<EditText>(R.id.numOQEdit)

        radiobn = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0)
        num_oq_edit.setText(getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10).toString())

        when (radiobn){
            0 -> radioGroup.check(R.id.radioclassic)
            1 -> radioGroup.check(R.id.radioclassic)
            2 -> radioGroup.check(R.id.radiotime)
            3 -> radioGroup.check(R.id.radio3)
        }

        if (radiobn != 1) {
            num_oq_edit.visibility = android.view.View.GONE
            num_oq_label.visibility = android.view.View.GONE
        }
        else {
            num_oq_edit.visibility = android.view.View.VISIBLE
            num_oq_label.visibility = android.view.View.VISIBLE
        }

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioclassic -> radiobn = 1
                R.id.radiotime -> radiobn = 2
                R.id.radio3 -> radiobn = 3
            }
            if (radiobn != 1) {
                num_oq_edit.visibility = android.view.View.GONE
                num_oq_label.visibility = android.view.View.GONE
            }
            else {
                num_oq_edit.visibility = android.view.View.VISIBLE
                num_oq_label.visibility = android.view.View.VISIBLE
            }
        }


        findViewById<Button>(R.id.buttondone).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putInt("limitations", radiobn)
            if (num_oq_edit.text.toString().toInt() != 0 && num_oq_edit.text.toString().toInt() <= 194)
                editor.putInt("numOQ", num_oq_edit.text.toString().toInt())
            else
                editor.putInt("numOQ", 10)

            editor.apply()
            startActivity(intent)
        }
    }

    }