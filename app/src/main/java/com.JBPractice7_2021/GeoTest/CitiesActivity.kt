package com.JBPractice7_2021.GeoTest

import android.content.Context
import android.content.Intent
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import kotlinx.android.synthetic.main.activity_capital_by_country_quiz.*
import kotlinx.android.synthetic.main.activity_cities.*
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import java.text.DecimalFormat
import androidx.core.content.res.ResourcesCompat
import android.media.MediaPlayer

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

        var right_sound = MediaPlayer.create(this, R.raw.correct4)
        var incorrect_sound = MediaPlayer.create(this, R.raw.incorrect2)

        val db = CityBase(resources)

        var limitation_mode: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("limitations", 0)
        var delay: Int = getSharedPreferences("settings",
            Context.MODE_PRIVATE).getInt("delay", 0)

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
                val cityDrawable = getDrawable(resources.getIdentifier("c" + questions[ctr].id.toString(), "drawable", packageName))
                val mbitmap = Bitmap.createScaledBitmap(
                    (cityDrawable as BitmapDrawable).getBitmap(),
                    800, (800f * cityDrawable.minimumHeight / cityDrawable.minimumWidth).toInt(),false);
                val imageRounded = Bitmap.createBitmap(mbitmap.getWidth(), mbitmap.getHeight(), mbitmap.getConfig())
                val canvas = Canvas(imageRounded)
                val mpaint = Paint()
                mpaint.setAntiAlias(false)
                mpaint.setShader(BitmapShader(mbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                canvas.drawRoundRect((RectF(0f, 0f, mbitmap.getWidth().toFloat(), mbitmap.getHeight().toFloat())), 25f, 25f, mpaint) // Round Image Corner 100 100 100 100
                cityscape.setImageBitmap(imageRounded)

                cityTextViewMain.setText(questions[ctr].city)

                for (k in 0 until city_btns.size) {
                    city_btns[k].setBackgroundResource(R.drawable.city_shape)
                    city_btns[k].setText(countries[k].country)

                    val fbitmap = (getDrawable(resources.getIdentifier("f" + countries[k].flag_id.toString(), "drawable", packageName)) as BitmapDrawable).getBitmap();
                    val fImageRounded = Bitmap.createBitmap(fbitmap.getWidth(), fbitmap.getHeight(), fbitmap.getConfig())
                    val fCanvas = Canvas(fImageRounded)
                    val fpaint = Paint()
                    fpaint.setAntiAlias(false)
                    fpaint.setShader(BitmapShader(fbitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP));
                    fCanvas.drawRoundRect((RectF(0f, 0f, fbitmap.getWidth().toFloat(), fbitmap.getHeight().toFloat())), 100f, 100f, fpaint) // Round Image Corner 100 100 100 100
                    flag_btns[k].background = BitmapDrawable(resources, fImageRounded)
                }

                if (limitation_mode < 2 && (tries + 1) <= number_of_questions) city_time.setText((tries + 1).toString() + "/" + number_of_questions.toString())
                ctr++

            }
            for (i in 0 until city_btns.size) {
                city_btns[i].setOnClickListener {
                    tries++
                    if (right_option == i) {
                        points++
                        right_sound.start()
                        if(limitation_mode != 2 && delay != 0) {
                            city_btns[i].setBackgroundResource(R.drawable.city_shape_correct)
                            Handler(Looper.getMainLooper()).postDelayed({ next_question() }, delay.toLong() * 500)
                            for (k in 0 until city_btns.size)
                            {
                                city_btns[i].setOnClickListener {}
                            }
                        }
                        else next_question()
                    } else {
                        incorrect++
                        incorrect_sound.start()
                        if (limitation_mode != 2 && delay != 0) {
                            city_btns[i].setBackgroundResource(R.drawable.city_shape_incorrect)
                            city_btns[right_option].setBackgroundResource(R.drawable.city_shape_answer)
                            Handler(Looper.getMainLooper()).postDelayed({ next_question() }, delay.toLong() * 500)
                            for (k in 0 until city_btns.size)
                            {
                                city_btns[i].setOnClickListener {}
                            }
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
        }

        next_question()


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