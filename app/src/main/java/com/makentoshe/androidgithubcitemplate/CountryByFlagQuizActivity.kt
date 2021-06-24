package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.TypedValue
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import android.content.Context
import android.os.CountDownTimer
import java.text.DecimalFormat
import java.text.NumberFormat
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*
import android.os.Handler
import android.os.Looper
import androidx.core.content.res.ResourcesCompat
import android.media.MediaPlayer


class CountryByFlagQuizActivity : AppCompatActivity() {

    var points: Int = 0
    var incorrect: Int = 0
    var tries: Int = 0
    var right_option = (0..3).random()
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_country_by_flag_quiz)

        var right_sound = MediaPlayer.create(this, R.raw.correct4)
        var incorrect_sound = MediaPlayer.create(this, R.raw.incorrect2)

        val db = DataBase(resources)

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)

        var delay: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("delay", 0)

        var number_of_questions: Int = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)

        val questions = db.getCountries(if (number_of_questions <= db.getSize() && limitation_mode < 2) number_of_questions else db.getSize())

        var countries = db.get4Countries(questions[0], right_option)
        var ctr = 1

        val img = findViewById<ImageView>(R.id.country_img)

        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, metrics)

        img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))
        var params = img.layoutParams
        val _ratio = img.background.minimumWidth.toDouble() / img.background.minimumHeight
        if (_ratio > 2)
        {
            params.width = pixels.toInt() * 2
            params.height = (pixels * 2 / _ratio).toInt()
        }
        else
        {
            params.width = (_ratio * pixels).toInt()
            params.height = pixels.toInt()
        }
        img.layoutParams = params

        var counter: Long = 60000 // время на вопросы

        country0.text = countries[0].country
        country1.text = countries[1].country
        country2.text = countries[2].country
        country3.text = countries[3].country

        val country_btns = arrayOf(country0, country1, country2, country3)
        if (limitation_mode == 3) {
            time.setTextColor(Color.parseColor("#FF0000"))
        }
        else if (limitation_mode < 2) time.setText((tries + 1).toString() + "/" + number_of_questions.toString())

        fun next_question (){
            if (((limitation_mode == 0 || limitation_mode == 1) && tries == number_of_questions) ||
                (limitation_mode == 3 && incorrect == 3) || (tries == db.getSize())) {
                val intent = Intent(this,MarkActivity::class.java)
                intent.putExtra("points", points.toString())
                intent.putExtra("tries", tries.toString())
                startActivity(intent)

            } else {

                right_option = (0..3).random()
                countries = db.get4Countries(questions[ctr], right_option)
                ctr++
                img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))
                val ratio = img.background.minimumWidth.toDouble() / img.background.minimumHeight
                if (ratio > 2)
                {
                    params.width = pixels.toInt() * 2
                    params.height = (pixels * 2 / ratio).toInt()
                }
                else
                {
                    params.width = (ratio * pixels).toInt()
                    params.height = pixels.toInt()
                }
                img.layoutParams = params

                for (k in 0 until country_btns.size) {
                    country_btns[k].setBackgroundColor(Color.WHITE)
                    country_btns[k].setTextColor(Color.BLACK)
                }

                if (limitation_mode < 2 && (tries + 1) <= number_of_questions) time.setText((tries + 1).toString() + "/" + number_of_questions.toString())

                country0.text = countries[0].country
                country1.text = countries[1].country
                country2.text = countries[2].country
                country3.text = countries[3].country

            }
        }

        for (i in 0 until country_btns.size) {
            country_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    right_sound.start()
                    if(limitation_mode != 2 && delay != 0) {
                        country_btns[i].setBackgroundColor(Color.argb(255, 80, 162, 55))
                        country_btns[i].setTextColor(Color.WHITE)
                        Handler(Looper.getMainLooper()).postDelayed({ next_question() }, delay.toLong() * 500)
                    }
                    else next_question()
                } else {
                    incorrect++
                    incorrect_sound.start()
                    if (limitation_mode != 2 && delay != 0) {
                        country_btns[i].setBackgroundColor(Color.argb(255,255, 92, 68))
                        country_btns[i].setTextColor(Color.WHITE)
                        country_btns[right_option].setBackgroundColor(Color.argb(80, 80, 162, 55))
                        country_btns[right_option].setTextColor(Color.WHITE)
                        Handler(Looper.getMainLooper()).postDelayed({ next_question() }, delay.toLong() * 500)
                    }
                    else next_question()
                }


                if (limitation_mode == 3) {
                    var incor: String = ""
                    for (i in 0 until incorrect) incor += "×"
                    time.setText(incor)
                }
            }


        }
        if (limitation_mode == 2) {
            timer = object : CountDownTimer(counter, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var min = (millisUntilFinished / 60000) % 60
                    var sec = (millisUntilFinished / 1000) % 60
                    time.text = min.toString() + ':' + DecimalFormat("00").format(sec)
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