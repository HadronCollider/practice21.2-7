package com.makentoshe.androidgithubcitemplate

import android.content.res.Resources
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.File

class DataBase(resources: Resources) {

    private var data: List<CountryRow> = listOf()

    init {
        val dataString = String(resources.openRawResource(R.raw.initial_data).readBytes())
        data = GsonBuilder().create().fromJson(dataString, Array<CountryRow>::class.java).toList()

    }

    fun getCountries (num: Int) : List<CountryRow> {
        var count = num
        if (num > data.size)
            count = data.size

        val dataShuffled = data.shuffled()

        var resData: List<CountryRow> = listOf()

        for (i in 0..(count - 1)) {
            resData = resData.plus(dataShuffled[i])
        }
        return resData

    }

    fun getSize(): Int {
        return data.size
    }

}

data class CountryRow (
    val id: String,
    val country: String,
    val capital: String)