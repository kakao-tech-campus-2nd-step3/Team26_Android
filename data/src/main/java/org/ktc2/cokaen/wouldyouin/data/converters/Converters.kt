package org.ktc2.cokaen.wouldyouin.data.converters

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.ktc2.cokaen.wouldyouin.data.model.Area
import org.ktc2.cokaen.wouldyouin.data.model.CurationCuratorResponse
import org.ktc2.cokaen.wouldyouin.data.model.CurationEventResponse
import org.ktc2.cokaen.wouldyouin.data.model.ImageResponse
import org.ktc2.cokaen.wouldyouin.data.model.LocalCurationCard
import org.ktc2.cokaen.wouldyouin.data.model.Location

class Converters {

    private val gson = Gson()

    // LocalCurationCard
    @TypeConverter
    fun fromCurationCardResponseList(value: List<LocalCurationCard>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurationCardResponseList(value: String): List<LocalCurationCard>? {
        val listType = object : TypeToken<List<LocalCurationCard>>() {}.type
        return gson.fromJson(value, listType)
    }

    // CurationCuratorResponse
    @TypeConverter
    fun fromCurationCuratorResponse(value: CurationCuratorResponse?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurationCuratorResponse(value: String): CurationCuratorResponse? {
        return gson.fromJson(value, CurationCuratorResponse::class.java)
    }

    // CurationEventResponse
    @TypeConverter
    fun fromCurationEventResponseList(value: List<CurationEventResponse>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toCurationEventResponseList(value: String): List<CurationEventResponse>? {
        val listType = object : TypeToken<List<CurationEventResponse>>() {}.type
        return gson.fromJson(value, listType)
    }

    // Hashtags
    @TypeConverter
    fun fromStringList(value: List<String>?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String): List<String>? {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }

    // Area
    @TypeConverter
    fun fromArea(value: Area?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toArea(value: String): Area? {
        return gson.fromJson(value, Area::class.java)
    }

    // Location
    @TypeConverter
    fun fromLocation(value: Location?): String {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toLocation(value: String): Location? {
        return gson.fromJson(value, Location::class.java)
    }

    @TypeConverter
    fun fromImageUploadResponse(imageResponse: List<ImageResponse>): String {
        return Gson().toJson(imageResponse)
    }

    @TypeConverter
    fun toImageUploadResponse(data: String): List<ImageResponse> {
        val listType = object : TypeToken<List<ImageResponse>>() {}.type
        return Gson().fromJson(data, listType)
    }
}

