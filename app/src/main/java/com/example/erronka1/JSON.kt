package com.example.erronka1

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import java.lang.reflect.Type

class JSON {
    companion object {
        inline fun <reified T> Get(context: Context, archivo: Int): List<T> {
            val jsonStr = context.resources.openRawResource(archivo).bufferedReader().use { it.readText() }
            val tipo: Type = object : TypeToken<List<T>>() {}.type
            return Gson().fromJson(jsonStr, tipo)
        }
    }
}