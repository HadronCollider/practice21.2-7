package com.makentoshe.androidgithubcitemplate

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mark.*
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

class MarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_mark)

        var getIntentFromQuizActivity = intent
        if (getIntentFromQuizActivity.hasExtra("points")) {
            tv.text = tv.text.toString() + getIntentFromQuizActivity.getStringExtra("points") + " / " + getIntentFromQuizActivity.getStringExtra("tries")
            val points = getIntentFromQuizActivity.getStringExtra("points")?.toInt()
            val total = getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("numOQ", 10)
            fun congratulate (p:Double?) {
                if (p != null) {
                    if (p.compareTo(0.8) == 1) tv_welldone.text = "Вы молодец!"
                    else if (p.compareTo(0.5) == 1) tv_welldone.text = "Продолжайте совершенствоваться!"
                    else tv_welldone.text = "Москва не сразу строилась!"
                }
            }

            when (getSharedPreferences("settings", Context.MODE_PRIVATE).getInt("limitations", 0)) {
                0, 1 -> if (points != null) {
                    congratulate(points.toDouble() / total)
                }
                2 -> tv_welldone.text = "Время закончилось"
                3 -> tv_welldone.text = "3 ошибки :( Игра закончилась"
            }

        }



        home.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
    override fun onBackPressed(){
        val intent = Intent(this,MainActivity::class.java)
        startActivity(intent)
    }
}