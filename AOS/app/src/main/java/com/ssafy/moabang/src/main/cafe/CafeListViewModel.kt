package com.ssafy.moabang.src.main.cafe

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssafy.moabang.R
import com.ssafy.moabang.data.model.dto.Cafe

class CafeListViewModel : ViewModel() {
    var island : String = ""
    var si : String = ""
    var cafeList : List<Cafe> = listOf()
}