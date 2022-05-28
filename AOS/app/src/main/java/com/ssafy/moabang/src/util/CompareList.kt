package com.ssafy.moabang.src.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.ssafy.moabang.data.model.dto.ThemeForCompare

object CompareList {
    var items : MutableList<ThemeForCompare> = mutableListOf()
    var clist = mutableListOf<ThemeForCompare>()

    private val _clistLiveData = MutableLiveData<List<ThemeForCompare>>()
    val clistLiveData : LiveData<List<ThemeForCompare>>
        get() = _clistLiveData

    fun addTheme(theme : ThemeForCompare) : Int {
        if(!clist.contains(theme) && clist.size < 3) {
            clist.add(theme)
            _clistLiveData.postValue(clist)
            return 1
        }
        if(clist.contains(theme)) return 2
        if(clist.size >= 3) return 3
        return 0
    }

    fun deleteTheme() {
        _clistLiveData.postValue(clist)
    }
}