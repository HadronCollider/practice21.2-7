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
            tv.text = tv.text.toString() + getIntentFromQuizActivity.getStringExtra("points") + " / 10"
            if (getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 10 || getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 9 || getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 8)
                tv_welldone.text = "Ты - молодец!"
            else{
                if (getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 7 || getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 6 || getIntentFromQuizActivity.getIntExtra("pointsint", 0) == 5)
                    tv_welldone.text = "Продолжай совершенствоваться!"
                else
                    tv_welldone.text = "Москва не сразу строилась!"
            }

        }



        home.setOnClickListener {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }

    }
}