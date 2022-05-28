package com.ssafy.moabang.data.model.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.data.model.repository.ThemeRepository
import com.ssafy.moabang.data.model.response.ReviewStatResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class ThemeViewModel: ViewModel() {
    private val themeRepository = ThemeRepository()

    private val _themeListLiveData = MutableLiveData<List<Theme>>()
    val themeListLiveData : LiveData<List<Theme>>
        get() = _themeListLiveData

    private val _themeStatLiveData = MutableLiveData<ReviewStatResponse>()
    val themeStatLiveDate : LiveData<ReviewStatResponse>
        get() = _themeStatLiveData

    private val totalThemeList = mutableListOf<Theme>()
    private var themeStat:ReviewStatResponse? = null

    fun getAllTheme(jwtToken: String) = viewModelScope.launch {
        getTheme(jwtToken)
        Log.d("VIEWMODEL TEST", "getAllTheme: $totalThemeList")
    }

    fun themeLike(tid: Int) = viewModelScope.launch {
        changeLike(tid)
    }

    fun themeStat(tid: Int) = viewModelScope.launch {
        getThemeStat(tid)
    }

    private suspend fun getTheme(jwtToken: String) = withContext(Dispatchers.IO) {
        val result: Response<List<Theme>>? = themeRepository.getAllTheme()

        if(result != null) {
            if (result.isSuccessful) {
                CoroutineScope(Dispatchers.IO).launch {
                    Repository.get().insertThemes(result.body()!!)
                }
                Log.d("THEME VIEWMODEL TEST", "success: ${result.body()}")
                result.body()!!.forEach {
                    if (!totalThemeList.contains(it)) {
                        totalThemeList.add(it)
                    }
                }
            }else{
                Log.d("THEME VIEWMODEL TEST", "SAD: ${result.message()}")
            }
            _themeListLiveData.postValue(totalThemeList)
        } else {
            Log.d("THEME VIEWMODEL TEST", "getTheme: response is null")
        }
    }

    private suspend fun changeLike(tid: Int) = withContext(Dispatchers.IO) {
        val result: Response<String>? = themeRepository.themeLike(tid)

        if(result != null){
            if(result.isSuccessful){
                Log.d("THEME VIEWMODEL TEST", "changeLike SUCCESS: ${result.body()}")
                if(result.body()!!.contains("취소")){
                    Repository.get().setThemeLike(tid, false)
                } else {
                    Repository.get().setThemeLike(tid, true)
                }
            } else {
                Log.d("THEME VIEWMODEL TEST", "changeLike FAILED: ${result.body()}")
            }
        }
    }

    private suspend fun getThemeStat(tid: Int) = withContext(Dispatchers.IO) {
        val result: Response<ReviewStatResponse>? = themeRepository.themeStat(tid)

        if(result != null){
            if(result.isSuccessful){
                Log.d("THEME VIEWMODEL TEST", "getThemeStat SUCCESS: ${result.body()}")
                themeStat = result.body()
            } else {
                Log.d("THEME VIEWMODEL TEST", "getThemeStat FAILED: ${result.body()}")
            }
            _themeStatLiveData.postValue(themeStat!!)
        } else {
            Log.d("THEME VIEWMODEL TEST", "getThemeStat FAILED: response is null}")
        }
    }
}