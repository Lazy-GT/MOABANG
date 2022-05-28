package com.ssafy.moabang.src.util

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity

class SharedPreferencesUtil(context: Context) {
    private var sp: SharedPreferences = context.getSharedPreferences("moabang", AppCompatActivity.MODE_PRIVATE)
    private val editor = sp.edit()

    fun putString(key:String, value: String){
        editor.putString(key, value)
        editor.apply()
    }

    fun putInt(key: String, value: Int){
        editor.putInt(key, value)
        editor.apply()
    }

    fun getString(key: String): String?{
        return sp.getString(key, null)
    }

    fun getInt(key: String): Int?{
        return sp.getInt(key, 0)
    }
}