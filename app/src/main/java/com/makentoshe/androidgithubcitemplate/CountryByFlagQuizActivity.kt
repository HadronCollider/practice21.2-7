package com.makentoshe.androidgithubcitemplate

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toDrawable
import com.opencsv.CSVParserBuilder
import java.io.FileReader
import com.opencsv.CSVReaderBuilder
import kotlinx.android.synthetic.main.activity_country_by_flag_quiz.*
import java.io.File
import kotlin.random.Random

class CountryByFlagQuizActivity : AppCompatActivity() {

    var points: Int = 0
    var tries: Int = 0
    var right_option = Random.nextInt(0, 3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_country_by_flag_quiz)
        //right_ans_tv.text = "Правильные ответы: $points / $tries"
        val db = DataBase()

        var countries = db.getCountries(4)

        val img = findViewById<ImageView>(R.id.country_img)

        img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))

        country0.text = countries[0].country
        country1.text = countries[1].country
        country2.text = countries[2].country
        country3.text = countries[3].country

        val country_btns = arrayOf(country0, country1, country2, country3)

        for (i in 0 until country_btns.size) {
            country_btns[i].setOnClickListener {
                tries++
                if (right_option == i) {
                    points++
                    Toast.makeText(this, "Правильно!", Toast.LENGTH_SHORT).show()
                }

                //right_ans_tv.text = "Правильные ответы: $points / $tries"
                if (tries == 10) {
                    val intent = Intent(this,MarkActivity::class.java)
                    Log.d("migav", points.toString())
                    intent.putExtra("points", points.toString())
                    intent.putExtra("pointsint", points)
                    startActivity(intent)

                }
                countries = db.getCountries(4)
                right_option = Random.nextInt(0, 3)

                img.setBackgroundResource(resources.getIdentifier("f" + countries[right_option].id, "drawable", packageName))
                country0.text = countries[0].country
                country1.text = countries[1].country
                country2.text = countries[2].country
                country3.text = countries[3].country
            }
        }







    }
}