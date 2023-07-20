package com.example.films.data.films.db.favirites.entities.converters

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.example.films.data.films.entities.Actor
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

@ProvidedTypeConverter
class Converters(
    private val gson: Gson
) {

    @TypeConverter
    fun longToDate(value: Long?): Date? {
        if (value == null)
            return null
        return Date(value)
    }

    @TypeConverter
    fun dateToLong(value: Date?): Long? {
        return value?.time
    }

    @TypeConverter
    fun artistsToString(value: List<Actor>?): String? {
        if (value == null)
            return null
        return gson.toJson(value)
    }

    @TypeConverter
    fun stringToArtists(value: String?): List<Actor>? {
        val typeToken = object : TypeToken<List<Actor>>() {}.type
        return gson.fromJson(value, typeToken)
    }

    @TypeConverter
    fun stringToListOfStrings(value: String?): List<String>? {
        return value?.split(DELIMITER)
    }

    @TypeConverter
    fun listOfStringsToString(value: List<String>?): String? {
        return value?.joinToString(DELIMITER)
    }

    companion object {
        private const val DELIMITER = ","
    }
}