package ade.test.danamon

import ade.test.danamon.data.remote.dto.PhotoDto
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File

class TestDataClassGenerator {

    private val gson = Gson()

    private inline fun <reified T> buildDataClassFromJson(json: String): T {
        val typeToken = object : TypeToken<T>() {}.type
        return gson.fromJson(json, typeToken)
    }

    private inline fun <reified T> buildDataClassFromArrayJson(json: String): List<T> {
        val typeToken = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, typeToken)
    }

    fun getPhotosResponse(): List<PhotoDto> {
        val jsonString = getJson("photos.json")
        return buildDataClassFromArrayJson(jsonString)
    }

    private fun getJson(resourceName: String): String {
        val file = File("src/test/resources/$resourceName")
        return String(file.readBytes())
    }
}