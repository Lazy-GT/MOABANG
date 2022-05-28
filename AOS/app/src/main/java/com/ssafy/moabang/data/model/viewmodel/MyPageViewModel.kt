package com.ssafy.moabang.data.model.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssafy.moabang.data.model.dto.Theme
import com.ssafy.moabang.data.model.repository.MyPageRepository
import com.ssafy.moabang.data.model.repository.Repository
import com.ssafy.moabang.data.model.response.DoneThemeResponse
import com.ssafy.moabang.data.model.response.DoneTidResponse
import com.ssafy.moabang.data.model.response.MyPostResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

class MyPageViewModel: ViewModel() {
    private val mypageRepository = MyPageRepository()

    private val _likeListLiveData = MutableLiveData<List<Theme>>()
    val likeListLiveData : LiveData<List<Theme>>
        get() = _likeListLiveData

    private val _doneThemeListLiveData = MutableLiveData<List<DoneThemeResponse>>()
    val doneThemeListLiveData : LiveData<List<DoneThemeResponse>>
        get() = _doneThemeListLiveData

    private val _doneTidListLiveData = MutableLiveData<List<DoneTidResponse>>()
    val doneTidListLiveData : LiveData<List<DoneTidResponse>>
        get() = _doneTidListLiveData

    private val _myPostLiveData = MutableLiveData<MyPostResponse>()
    val myPostLiveData: LiveData<MyPostResponse>
        get() = _myPostLiveData

    private val totalLikeList = mutableListOf<Theme>()
    private var totalDoneThemeList = mutableListOf<DoneThemeResponse>()
    private val totalDoneTidList = mutableListOf<DoneTidResponse>()
    private var totalPost:MyPostResponse? = null

    fun getAllLikeTheme() = viewModelScope.launch {
        getLikeTheme()
        Log.d("VIEWMODEL TEST", "getAllLikeTheme: $totalLikeList")
    }

    fun getAllDoneTheme() = viewModelScope.launch {
        getDoneTheme()
    }

    fun getAllDoneTid() = viewModelScope.launch {
        getDoneTid()
    }

    fun getAllPost() = viewModelScope.launch {
        getMyPost()
    }

    private suspend fun getLikeTheme() = withContext(Dispatchers.IO) {
        val result: Response<List<Theme>>? = mypageRepository.getAllLikeTheme()

        if(result != null) {
            if (result.isSuccessful) {
                Log.d("MYPAGE VIEWMODEL TEST", "success: ${result.body()}")
                result.body()!!.forEach {
                    if (!totalLikeList.contains(it)) {
                        totalLikeList.add(it)
                    }
                }
            }else{
                Log.d("MYPAGE VIEWMODEL TEST", "SAD: ${result.message()}")
            }
            _likeListLiveData.postValue(totalLikeList)
        } else {
            Log.d("MYPAGE VIEWMODEL TEST", "getLikeTheme: response is null")
        }
    }

    private suspend fun getDoneTheme() = withContext(Dispatchers.IO) {
        val result: Response<List<DoneThemeResponse>>? = mypageRepository.getDoneTheme()

        if(result != null) {
            if (result.isSuccessful) {
                totalDoneThemeList = mutableListOf<DoneThemeResponse>()
                Log.d("MYPAGE VIEWMODEL TEST", "success: ${result.body()}")
                result.body()!!.forEach {
                    if (!totalDoneThemeList.contains(it)) {
                        totalDoneThemeList.add(it)
                    }
                }
            }else{
                Log.d("MYPAGE VIEWMODEL TEST", "SAD: ${result.message()}")
            }
            _doneThemeListLiveData.postValue(totalDoneThemeList)
        } else {
            Log.d("MYPAGE VIEWMODEL TEST", "getDoneTheme: response is null")
        }
    }

    private suspend fun getDoneTid() = withContext(Dispatchers.IO) {
        val result: Response<List<DoneTidResponse>>? = mypageRepository.getDoneTid()

        if(result != null) {
            if (result.isSuccessful) {
                Log.d("MYPAGE VIEWMODEL TEST", "success: ${result.body()}")
                result.body()!!.forEach {
                    if (!totalDoneTidList.contains(it)) {
                        totalDoneTidList.add(it)
                    }
                }
            }else{
                Log.d("MYPAGE VIEWMODEL TEST", "SAD: ${result.message()}")
            }
            _doneTidListLiveData.postValue(totalDoneTidList)
        } else {
            Log.d("MYPAGE VIEWMODEL TEST", "getDoneTid: response is null")
        }
    }

    private suspend fun getMyPost() = withContext(Dispatchers.IO) {
        val result: Response<MyPostResponse>? = mypageRepository.getMyPost()

        if(result != null) {
            if (result.isSuccessful) {
                Log.d("MYPAGE VIEWMODEL TEST", "success: ${result.body()}")
                totalPost = result.body()
            }else{
                Log.d("MYPAGE VIEWMODEL TEST", "getMyPost fail: ${result.message()}")
            }
            _myPostLiveData.postValue(totalPost!!)
        } else {
            Log.d("MYPAGE VIEWMODEL TEST", "getMyPost: response is null")
        }
    }
}