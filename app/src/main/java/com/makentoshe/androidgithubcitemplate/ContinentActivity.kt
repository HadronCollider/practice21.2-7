package com.makentoshe.androidgithubcitemplate

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_continent.*
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import java.text.DecimalFormat
import androidx.core.content.res.ResourcesCompat
import android.media.MediaPlayer

class ContinentActivity : AppCompatActivity() {

    var _Continent = if ((0..3).random() != 0) arrayOf(0, 1, 3, 4).random() else arrayOf(2, 5).random()
    var numOfCorrect = (3..7).random()
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_continent)

        var right_sound = MediaPlayer.create(this, R.raw.correct3)
        var incorrect_sound = MediaPlayer.create(this, R.raw.incorrect2)

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)
        var delay: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("delay", 0)

        val db = DataBase(resources)

        var number_of_questions: Int = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)
        if (number_of_questions > 20) number_of_questions = 20

        val cont_btns = arrayOf(cont_btn1, cont_btn2, cont_btn3, cont_btn4, cont_btn5, cont_btn6, cont_btn7, cont_btn8, cont_btn9, cont_btn10)
        var selected = arrayOf(false, false, false, false, false, false, false, false, false, false)
        var right = arrayOf(false, false, false, false, false, false, false, false, false, false)
        val continents = arrayOf("в Азии:", "в Африке:", "в Северной Америке:", "в Южной Америке", "в Европе:", "в Океании:")

        var points = 0
        var incorrect = 0
        var tries = 0

        var counter: Long = 60000

        if (limitation_mode == 3) {
            c_time.setTextColor(Color.parseColor("#FF0000"))
            c_time.setText("")
        }

        fun next_question() {
            if (((limitation_mode == 0 || limitation_mode == 1) && tries == number_of_questions) ||
                (limitation_mode == 3 && incorrect == 3) || (tries == db.getSize() / 10)) {
                val intent = Intent(this,MarkActivity::class.java)
                intent.putExtra("points", points.toString())
                intent.putExtra("tries", (tries * 10).toString())
                startActivity(intent)

            } else {

                for (i in 0..(right.size - 1)) {
                    right[i] = false
                    selected[i] = false
                }

                _Continent = if ((0..3).random() != 0) arrayOf(0, 1, 2, 4).random() else arrayOf(2, 5).random()
                numOfCorrect = (3..7).random()
                val countries = db.getByContinent(10 - numOfCorrect, _Continent, false).plus(db.getByContinent(numOfCorrect, _Continent, true)).shuffled()
                textViewС.setText(continents[_Continent])

                for (k in 0 until cont_btns.size) {
                    cont_btns[k].setBackgroundColor(Color.WHITE)
                    cont_btns[k].setTextColor(Color.BLACK)
                    if (countries[k].continent == _Continent || ((_Continent == 0 || _Continent == 4) && countries[k].continent == 8))
                        right[k] = true
                }

                for (i in 0 until cont_btns.size)
                {
                    cont_btns[i].text = countries[i].country
                }

            }
        }

        next_question()

        fun format()
        {
            for (k in 0 until cont_btns.size) {
                if (selected[k])
                {
                    cont_btns[k].setBackgroundColor(Color.argb(255, 71, 178, 206))
                    cont_btns[k].setTextColor(Color.WHITE)
                }
                else
                {
                    cont_btns[k].setBackgroundColor(Color.WHITE)
                    cont_btns[k].setTextColor(Color.BLACK)
                }
            }
        }

        fun format_final()
        {
            for (k in 0 until cont_btns.size) {
                if (selected[k] && right[k])
                {
                    cont_btns[k].setBackgroundColor(Color.argb(255, 80, 162, 55))
                    cont_btns[k].setTextColor(Color.WHITE)
                }
                else if (selected[k] && !right[k])
                {
                    cont_btns[k].setBackgroundColor(Color.argb(255,255, 92, 68))
                    cont_btns[k].setTextColor(Color.WHITE)
                }
                else if (!selected[k] && right[k])
                {
                    cont_btns[k].setBackgroundColor(Color.argb(80, 80, 162, 55))
                    cont_btns[k].setTextColor(Color.WHITE)
                }
                else
                {
                    cont_btns[k].setBackgroundColor(Color.WHITE)
                    cont_btns[k].setTextColor(Color.BLACK)
                }
            }
        }

        for (i in 0 until cont_btns.size) {
            cont_btns[i].setOnClickListener()
            {
                selected[i] = !selected[i]
                format()
            }
        }

        confirm_btn.setOnClickListener {
            tries++
            format_final()

            var curr_points = 0
            for (i in 0 until right.size)
            {
                if (right[i] == selected[i]) {
                    points++
                    curr_points++
                }
            }

            if (curr_points < 9)
            {
                incorrect += 1
                incorrect_sound.start()
            }
            else right_sound.start()

            if (limitation_mode < 2)
            {
                c_time.setText("Правильно: " + points.toString())
            }
            else if (limitation_mode == 3) {
                var incor: String = ""
                for (i in 0 until incorrect) incor += "×"
                c_time.setText(incor)
            }

            if (limitation_mode != 2 && delay != 0) {
                Handler(Looper.getMainLooper()).postDelayed({ next_question() }, delay.toLong() * 500)
            }
            else next_question()
        }

        if (limitation_mode == 2) {
            timer = object : CountDownTimer(counter, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    var min = (millisUntilFinished / 60000) % 60
                    var sec = (millisUntilFinished / 1000) % 60
                    c_time.text = min.toString() + ':' + DecimalFormat("00").format(sec)
                }

                override fun onFinish() {
                    val intent = Intent(applicationContext, MarkActivity::class.java)
                    intent.putExtra("points", points.toString())
                    intent.putExtra("tries", (tries * 10).toString())
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