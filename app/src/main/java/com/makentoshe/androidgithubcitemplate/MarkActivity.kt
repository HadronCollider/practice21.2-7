package com.makentoshe.androidgithubcitemplate

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_mark.*

class MarkActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide();
        setContentView(R.layout.activity_mark)

        var getIntentFromQuizActivity = intent
        if (getIntentFromQuizActivity.hasExtra("points")) {
            tv.text = tv.text.toString() + getIntentFromQuizActivity.getStringExtra("points") + " / " + getIntentFromQuizActivity.getStringExtra("tries")
            val points = getIntentFromQuizActivity.getStringExtra("points")?.toInt()
            fun congratulate (p:Int) = when (p) {
                10, 9, 8 -> tv_welldone.text = "Ты - молодец!"
                7, 6, 5 -> tv_welldone.text = "Продолжай совершенствоваться!"
                else -> tv_welldone.text = "Москва не сразу строилась!"
            }

            if ((getIntentFromQuizActivity.getStringExtra("tries")?.toInt() == 10) && (points != null)) congratulate(points)
            else tv_welldone.text = "3 ошибки :( Игра закончилась"

        }



        home.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}