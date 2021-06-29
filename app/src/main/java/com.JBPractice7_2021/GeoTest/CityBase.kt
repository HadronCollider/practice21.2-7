package com.JBPractice7_2021.GeoTest

import android.content.res.Resources
import com.google.gson.GsonBuilder

class CityBase(resources: Resources) {

    private var city_data: List<CityRow> = listOf()
    private var country_data: List<CBCountryRow> = listOf()

    init {
        val dataString = String(resources.openRawResource(R.raw.cities_data).readBytes())
        city_data = GsonBuilder().create().fromJson(dataString, Array<CityRow>::class.java).toList()
        val cDataString = String(resources.openRawResource(R.raw.city_countries_data).readBytes())
        country_data = GsonBuilder().create().fromJson(cDataString, Array<CBCountryRow>::class.java).toList()
    }

    fun getCitiesNumber(): Int { return city_data.size }

    fun getCities(num: Int): List<CityRow>
    {
        val dataShuffled = city_data.shuffled()
        return dataShuffled.subList(0, num)
    }

    fun getOptions(rightAnswer: CityRow): List<CBCountryRow>
    {
        val dataShuffled = country_data.shuffled()
        var options: List<CBCountryRow> = listOf(country_data[rightAnswer.country_id - 1])
        val tag = if(country_data[rightAnswer.country_id - 1].tag != -1) country_data[rightAnswer.country_id - 1].tag else 0
        var i: Int = 0
        while (options.size < 4)
        {
            if (dataShuffled[i].tag == tag && !(rightAnswer.chinese_country == true && dataShuffled[i].id == 7) && !(rightAnswer.country_id == dataShuffled[i].id))
                options = options.plus(dataShuffled[i])
            i++
        }
        options = options.shuffled()
        return options
    }
}

data class CityRow (
    val id: Int,
    val city: String,
    val country_id: Int,
    val chinese_country: Boolean)

data class CBCountryRow (
    val id: Int,
    val country: String,
    val tag: Int,
    val flag_id: Int)