package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*
import kotlinx.android.synthetic.main.activity_cities.*
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import java.text.DecimalFormat

class CitiesActivity : AppCompatActivity() {

    var points: Int = 0
    var incorrect: Int = 0
    var tries: Int = 0
    var right_option = (0..3).random()
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_cities)

        val db = CityBase(resources)

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)

        var number_of_questions: Int = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)

        val questions = db.getCities(if (number_of_questions <= db.getCitiesNumber() && limitation_mode < 2) number_of_questions else db.getCitiesNumber())

        var countries: List<CBCountryRow>
        var ctr = 0

        var counter: Long = 60000 // время на вопросы

        val city_btns = arrayOf(city1, city2, city3, city4)
        val flag_btns = arrayOf(city_flag1, city_flag2, city_flag3, city_flag4)

        if (limitation_mode == 3) {
            city_time.setTextColor(Color.parseColor("#FF0000"))
        }

        fun next_question (){
            if (((limitation_mode == 0 || limitation_mode == 1) && tries == number_of_questions) ||
                (limitation_mode == 3 && incorrect == 3) || (tries == db.getCitiesNumber())) {
                val intent = Intent(this,MarkActivity::class.java)
                intent.putExtra("points", points.toString())
                intent.putExtra("tries", tries.toString())
                startActivity(intent)

            } else {
                countries = db.getOptions(questions[ctr])
                for (i in 0..3) {
                    if (countries[i].id == questions[ctr].country_id) {
                        right_option = i
                        break
                    }
                }
                cityscape.setImageResource(resources.getIdentifier("c" + questions[ctr].id.toString(), "drawable", packageName))
                cityTextViewMain.setText(questions[ctr].city + "?")

                for (k in 0 until city_btns.size) {
                    city_btns[k].setBackgroundColor(Color.WHITE)
                    city_btns[k].setTextColor(Color.BLACK)
                    city_btns[k].setText(countries[k].country)
                    flag_btns[k].setBackgroundResource(resources.getIdentifier("f" + countries[k].flag_id.toString(), "drawable", packageName))
                }

                if (limitation_mode < 2 && (tries + 1) <= number_of_questions) city_time.setText((tries + 1).toString() + "/" + number_of_questions.toString())
                ctr++

            }
        }

        next_question()
        for (i in 0 until city_btns.size) {
            city_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    if(limitation_mode != 2) {
                        city_btns[i].setBackgroundColor(Color.argb(255, 80, 162, 55))
                        city_btns[i].setTextColor(Color.WHITE)
                        Handler(Looper.getMainLooper()).postDelayed({ next_question() }, 1000)
                    }
                    else next_question()
                } else {
                    incorrect++
                    if (limitation_mode != 2) {
                        city_btns[i].setBackgroundColor(Color.argb(255,255, 92, 68))
                        city_btns[i].setTextColor(Color.WHITE)
                        city_btns[right_option].setBackgroundColor(Color.argb(80, 80, 162, 55))
                        city_btns[right_option].setTextColor(Color.WHITE)
                        Handler(Looper.getMainLooper()).postDelayed({ next_question() }, 1000)
                    }
                    else next_question()
                }


                if (limitation_mode == 3) {
                    var incor: String = ""
                    for (i in 0 until incorrect) incor += "×"
                    city_time.setText(incor)
                }
            }
        }

        if (limitation_mode == 2) {
            timer = object : CountDownTimer(counter, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var min = (millisUntilFinished / 60000) % 60
                    var sec = (millisUntilFinished / 1000) % 60
                    city_time.text = min.toString() + ':' + DecimalFormat("00").format(sec)
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