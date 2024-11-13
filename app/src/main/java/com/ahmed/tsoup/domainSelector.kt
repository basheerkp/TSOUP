package com.ahmed.tsoup

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

fun saveAddress(addresses: List<String>, prefs: SharedPreferences) {
    val json = Gson().toJson(addresses)
    prefs.edit().putString("addresses", json).apply()
}

fun loadAddress(prefs: SharedPreferences): List<String> {
    val json = prefs.getString("addresses", "[]")
    val type = object : TypeToken<List<String>>() {}.type
    return Gson().fromJson(json, type)
}

fun setDefaultAddress(address: String, prefs: SharedPreferences) {
    prefs.edit().putString("default_address", address).apply()
}

fun getDefaultAddress(prefs: SharedPreferences): String? {
    return prefs.getString("default_address", null)
}