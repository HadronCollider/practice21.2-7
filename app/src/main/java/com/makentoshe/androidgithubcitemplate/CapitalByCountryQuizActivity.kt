package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random
import android.content.Intent
import android.media.Image
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.opencsv.CSVParserBuilder
import java.io.FileReader
import com.opencsv.CSVReaderBuilder
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*
import java.io.File
import android.widget.TextView
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager


class CapitalByCountryQuizActivity : AppCompatActivity() {
    var points: Int = 0
    var incorrect: Int = 0
    var tries: Int = 0
    var right_option = Random.nextInt(0, 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_capital_by_country_quiz)

        val db = DataBase()

        var countries = db.getCountries(4)

        val tv = findViewById<TextView>(R.id.tv_country)

        tv.text = countries[right_option].country

        capital0.text = countries[0].capital
        capital1.text = countries[1].capital
        capital2.text = countries[2].capital
        capital3.text = countries[3].capital

        val capital_btns = arrayOf(capital0, capital1, capital2, capital3)
        for (i in 0 until capital_btns.size) {
            capital_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                }
                else incorrect++

                //right_ans_tv.text = "Правильные ответы: $points / $tries"
                if (((getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0) == 0 || getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0) == 1) && tries == 10) || (getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0) == 3 && incorrect == 3)) {
                    val intent = Intent(this,MarkActivity::class.java)
                    Log.d("migav", points.toString())
                    intent.putExtra("points", points.toString())
                    intent.putExtra("tries", tries.toString())
                    startActivity(intent)

                } else {
                    countries = db.getCountries(4)
                    right_option = Random.nextInt(0, 3)

                    tv.text = countries[right_option].country
                    capital0.text = countries[0].capital
                    capital1.text = countries[1].capital
                    capital2.text = countries[2].capital
                    capital3.text = countries[3].capital
                }
            }
        }
    }
}

