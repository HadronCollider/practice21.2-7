package com.makentoshe.androidgithubcitemplate

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*
import android.widget.TextView
import android.content.Context
import android.graphics.Color
import android.os.CountDownTimer
import java.text.DecimalFormat
import java.text.NumberFormat
import android.os.Handler
import android.os.Looper



class CapitalByCountryQuizActivity : AppCompatActivity() {
    var points: Int = 0
    var incorrect: Int = 0
    var tries: Int = 0
    var right_option = (0..3).random()
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_capital_by_country_quiz)

        val db = DataBase(resources)

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)

        var number_of_questions: Int = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)

        val questions = db.getCountries(if (number_of_questions <= db.getSize() && limitation_mode < 2) number_of_questions else db.getSize())

        var countries = db.get4Countries(questions[0], right_option)
        var ctr = 1

        val tv = findViewById<TextView>(R.id.tv_country)

        var counter: Long = 60000 // время на вопросы

        tv.text = countries[right_option].country

        if (limitation_mode == 3) {
            tv_time.setTextColor(Color.parseColor("#FF0000"))
        }
        else if (limitation_mode < 2) tv_time.setText((tries + 1).toString() + "/" + number_of_questions.toString())

        capital0.text = countries[0].capital
        capital1.text = countries[1].capital
        capital2.text = countries[2].capital
        capital3.text = countries[3].capital

        val capital_btns = arrayOf(capital0, capital1, capital2, capital3)

        fun next_question () {
            if (((limitation_mode == 0 || limitation_mode == 1) && tries == number_of_questions) ||
                (limitation_mode == 3 && incorrect == 3)) {
                val intent = Intent(this, MarkActivity::class.java)
                intent.putExtra("points", points.toString())
                intent.putExtra("tries", tries.toString())
                startActivity(intent)

            } else {
                right_option = (0..3).random()
                countries = db.get4Countries(questions[ctr], right_option)
                ctr++
                for (k in 0 until capital_btns.size) {
                    capital_btns[k].setBackgroundColor(Color.WHITE)
                    capital_btns[k].setTextColor(Color.BLACK)
                }

                if (limitation_mode < 2 && (tries + 1) <= number_of_questions) tv_time.setText((tries + 1).toString() + "/" + number_of_questions.toString())

                tv.text = countries[right_option].country
                capital0.text = countries[0].capital
                capital1.text = countries[1].capital
                capital2.text = countries[2].capital
                capital3.text = countries[3].capital
            }
        }

        for (i in 0 until capital_btns.size) {
            capital_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    capital_btns[i].setBackgroundColor(Color.argb(255, 80, 162, 55))
                    capital_btns[i].setTextColor(Color.WHITE)
                    Handler(Looper.getMainLooper()).postDelayed({next_question()}, 1000)

                } else  {
                    incorrect++
                    capital_btns[i].setBackgroundColor(Color.argb(255,255, 92, 68))
                    capital_btns[i].setTextColor(Color.WHITE)
                    capital_btns[right_option].setBackgroundColor(Color.argb(80, 80, 162, 55))
                    capital_btns[right_option].setTextColor(Color.WHITE)
                    Handler(Looper.getMainLooper()).postDelayed({next_question()}, 1000)
                }

                if (limitation_mode == 3) {
                    var incor: String = ""
                    for (i in 0 until incorrect) incor += "× "
                    tv_time.setText(incor)
                }
            }
        }

        if (limitation_mode == 2) {
            timer = object : CountDownTimer(counter, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var min = (millisUntilFinished / 60000) % 60
                    var sec = (millisUntilFinished / 1000) % 60
                    tv_time.text = min.toString() + ':' + DecimalFormat("00").format(sec)
                }

                override fun onFinish() {
                    val intent = Intent(applicationContext, MarkActivity::class.java)
                    intent.putExtra("points", points.toString())
                    intent.putExtra("tries", tries.toString())
                    startActivity(intent)

                }
            }.start()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (this::timer.isInitialized) timer.cancel()
    }
}