package com.ssafy.moabang.data.model.repository

import android.util.Log
import com.ssafy.moabang.config.GlobalApplication
import com.ssafy.moabang.data.api.CommunityApi
import com.ssafy.moabang.data.model.dto.*
import retrofit2.Response
import java.lang.Exception

class CommunityRepository {
    private val communityApi: CommunityApi = GlobalApplication.retrofit.create(CommunityApi::class.java)

    fun getAllCommunity() : Response<List<Community>>?{
        try {
            val res = communityApi.getAllCommunity().execute()
            Log.d("AAAAA", "getAllCommunity OK: ${res}")
            return res
        } catch (e: Exception) {
            Log.d("AAAAA", "getAllCommunity NOT OK: ${e}")
            e.printStackTrace()
        }
        return null
    }

    fun insertCommunity(requestCreateRequest: RecruitCreateRequest) : Response<Boolean>? {
        try {
            val res = communityApi.insertCommunity(requestCreateRequest).execute()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun getCommunity(_rid : Int) : Response<Community>?{
        try {
            val res = communityApi.getCommunity(_rid).execute()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun updateCommunity(_rid: Int, _recruitCreateRequest : RecruitCreateRequest) : Response<Boolean>?{
        try {
            val res = communityApi.updateCommunity(_rid, _recruitCreateRequest).execute()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteCommunity(_rid: Int) : Response<Boolean>?{
        try {
            val res = communityApi.deleteCommunity(_rid,).execute()
            Log.d("BBBBB", "deleteCommunity: OK $res")
            return res
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("BBBBB", "deleteCommunity: NOT OK $e")

        }
        return null
    }

    fun getLatest5Community() : Response<List<Community>>?{
        try {
            val res = communityApi.getLatest5Community().execute()
            return res
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun insertComment(commentRequest: CommentRequest) : Response<Boolean>?{
        try {
            return communityApi.insertComment(commentRequest).execute()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun deleteComment(coid: Int) : Response<Boolean>?{
        try {
            val result = communityApi.deleteComment(coid).execute()
            Log.d("BBBBB", "deleteComment OK : ${result}")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("BBBBB", "deleteComment NOT OK: ${e}")
        }
        return null
    }

    fun getCommentList(community_id : Int) : Response<List<Comment>>?{
        try {
            val result = communityApi.getCommentList(community_id).execute()
            Log.d("BBBBB", "getCommentList OK : ${result}")
            return result
        } catch (e: Exception) {
            e.printStackTrace()
            Log.d("BBBBB", "getCommentList NOT OK: ${e}")
        }
        return null
    }

    fun updateComment(commentUpdateRequest: CommentUpdateRequest) : Response<Boolean>?{
        Log.d("BBBBB", "Input : ${commentUpdateRequest}")
        try {
            val result = communityApi.updateComment(commentUpdateRequest).execute()
            Log.d("BBBBB", "Output OK : ${result}")
            return result
        } catch (e: Exception) {
            Log.d("BBBBB", "Output NOT OK : ${e}")
            e.printStackTrace()
        }
        return null
    }

}