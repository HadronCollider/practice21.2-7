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

    fun get4Countries(rightAnswer: CountryRow, rightOption: Int): List<CountryRow>
    {
        var _countries = getCountries(3).plus(rightAnswer)
        while (true)
        {
            var flag = true
            for (i in 0..2)
            {
                if(_countries[i].id == rightAnswer.id)
                    flag = false
            }
            if (flag)
                break
            else
                _countries = getCountries(3).plus(rightAnswer)
        }
        var countries: List<CountryRow> = listOf()
        for (i in 0..2)
        {
            if (i == rightOption) countries = countries.plus(rightAnswer)
            else countries = countries.plus(_countries[i])
        }
        countries = countries.plus(_countries[rightOption])
        return countries
    }

    fun getByContinent(num: Int, continent: Int, this_cont: Boolean): List<CountryRow> {
        var count = num
        if (num > data.size)
            count = data.size

        val dataShuffled = data.shuffled()

        var resData: List<CountryRow> = listOf()

        var ctr = 0
        var i = 0
        while(ctr < num) {
            if ((dataShuffled[i].continent == continent || (continent == 8 && (dataShuffled[i].continent == 0 || dataShuffled[i].continent == 4))) == this_cont) {
                resData = resData.plus(dataShuffled[i])
                ctr++
            }
            i++
        }
        return resData
    }
}

data class CountryRow (
    val id: String,
    val country: String,
    val capital: String,
    val continent: Int)