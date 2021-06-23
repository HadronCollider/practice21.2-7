package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.view.View
import android.widget.*
import android.widget.SeekBar
import android.widget.TextView


class SettingActivity : AppCompatActivity() {
    var radiobn: Int = 0
    var delay: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_setting)

        val radioGroup = findViewById<RadioGroup>(R.id.radiogroup)
        val num_oq_label = findViewById<TextView>(R.id.numOQ)
        val num_oq_picker = findViewById<NumberPicker>(R.id.numQQPicker)
        num_oq_picker.minValue = 1
        num_oq_picker.maxValue = 100

        // getPickerVals begin
        var pickerVals: Array<String> = arrayOf()
        for (i in 1..101) pickerVals = pickerVals.plus(i.toString())
        // getPickerVals end

        num_oq_picker.displayedValues = pickerVals

        radiobn = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0)
        delay = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("delay", 0)
        num_oq_picker.value =
            getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)

        when (radiobn) {
            0 -> radioGroup.check(R.id.radioclassic)
            1 -> radioGroup.check(R.id.radioclassic)
            2 -> radioGroup.check(R.id.radiotime)
            3 -> radioGroup.check(R.id.radio3)
        }

        if (radiobn != 1) {
            num_oq_picker.visibility = android.view.View.INVISIBLE
            num_oq_label.visibility = android.view.View.INVISIBLE
        } else {
            num_oq_picker.visibility = android.view.View.VISIBLE
            num_oq_label.visibility = android.view.View.VISIBLE
        }

        radioGroup?.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioclassic -> radiobn = 1
                R.id.radiotime -> radiobn = 2
                R.id.radio3 -> radiobn = 3
            }
            if (radiobn != 1) {
                num_oq_picker.visibility = android.view.View.INVISIBLE
                num_oq_label.visibility = android.view.View.INVISIBLE
            } else {
                num_oq_picker.visibility = android.view.View.VISIBLE
                num_oq_label.visibility = android.view.View.VISIBLE
            }
        }


        findViewById<Button>(R.id.buttondone).setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            val pref = getSharedPreferences("settings", Context.MODE_PRIVATE)
            val editor = pref.edit()

            editor.putInt("limitations", radiobn)
            editor.putInt("delay", delay)
            if (num_oq_picker.value != 0 && num_oq_picker.value <= 194)
                editor.putInt("numOQ", num_oq_picker.value)
            else
                editor.putInt("numOQ", 10)

            editor.apply()
            startActivity(intent)
        }

        val seekBar: SeekBar = findViewById(R.id.seekBar_delay)
        findViewById<TextView>(R.id.tv_delay).text = "Время задержки (в секундах): ${(delay.toFloat() / 2)}"
        seekBar.progress = delay
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
                findViewById<TextView>(R.id.tv_delay).text = "Время задержки (в секундах): ${(progress.toFloat() / 2)}"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {   }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
                delay = seekBar.getProgress()
            }
            })
    }
}