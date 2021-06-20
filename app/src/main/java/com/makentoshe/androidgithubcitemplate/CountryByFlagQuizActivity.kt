package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.util.TypedValue
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import kotlin.random.Random
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.os.CountDownTimer
import java.text.DecimalFormat
import java.text.NumberFormat
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*


class CountryByFlagQuizActivity : AppCompatActivity() {

    var points: Int = 0
    var incorrect: Int = 0
    var tries: Int = 0
    var right_option = Random.nextInt(0, 3)
    private lateinit var timer: CountDownTimer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_country_by_flag_quiz)
        //right_ans_tv.text = "Правильные ответы: $points / $tries"
        val db = DataBase()

        var countries = db.getCountries(4)

        val img = findViewById<ImageView>(R.id.country_img)

        val metrics: DisplayMetrics = Resources.getSystem().getDisplayMetrics()
        val pixels = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 180f, metrics)

        img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))
        var params = img.layoutParams
        val _ratio = img.background.minimumWidth.toDouble() / img.background.minimumHeight
        if (_ratio > 2) params.height = (pixels * 2 / _ratio).toInt()
        else params.width = (_ratio * pixels).toInt()
        Log.d("width", img.height.toString())
        img.layoutParams = params

        var counter: Long = 60000 // время на вопросы

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)

        var number_of_questions: Int = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)

        country0.text = countries[0].country
        country1.text = countries[1].country
        country2.text = countries[2].country
        country3.text = countries[3].country

        val country_btns = arrayOf(country0, country1, country2, country3)
        if (limitation_mode == 3) {
            time.setTextColor(Color.parseColor("#FF0000"))
        }
        else if (limitation_mode < 2) time.setText((tries + 1).toString() + "/" + number_of_questions.toString())

        for (i in 0 until country_btns.size) {
            country_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                }
                else incorrect++

                if (limitation_mode < 2 && (tries + 1) <= number_of_questions) time.setText((tries + 1).toString() + "/" + number_of_questions.toString())
                else if (limitation_mode == 3) {
                    var incor: String = ""
                    for (i in 0 until incorrect) incor += "×"
                    time.setText(incor)
                }

                //right_ans_tv.text = "Правильные ответы: $points / $tries"
                if (((limitation_mode == 0 || limitation_mode == 1) && tries == number_of_questions) ||
                    (limitation_mode == 3 && incorrect == 3)) {
                    val intent = Intent(this,MarkActivity::class.java)
                    intent.putExtra("points", points.toString())
                    intent.putExtra("tries", tries.toString())
                    startActivity(intent)

                } else {

                    countries = db.getCountries(4)
                    right_option = Random.nextInt(0, 3)
                    img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))
                    val ratio = img.background.minimumWidth.toDouble() / img.background.minimumHeight
                    if (ratio > 2) params.height = (img.width / ratio).toInt()
                    else params.width = (ratio * img.height).toInt()
                    img.layoutParams = params
                    country0.text = countries[0].country
                    country1.text = countries[1].country
                    country2.text = countries[2].country
                    country3.text = countries[3].country
                    country_btns[right_option].setBackgroundColor(Color.WHITE)
                    country_btns[i].setBackgroundColor(Color.WHITE)
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