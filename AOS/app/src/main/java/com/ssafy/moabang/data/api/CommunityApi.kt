package com.ssafy.moabang.data.api

import com.ssafy.moabang.data.model.dto.*
import retrofit2.Call
import retrofit2.http.*

interface CommunityApi {
    @GET("/community/read/list")
    fun getAllCommunity() : Call<List<Community>>

    @POST("/community/write")
    fun insertCommunity(
        @Body requestCreateRequest: RecruitCreateRequest
    ) : Call<Boolean>

    @GET("/community/read/{rid}")
    fun getCommunity(
        @Path("rid") rid : Int
    ) : Call<Community>

    @PUT("/community/update/{rid}")
    fun updateCommunity(
        @Path("rid") rid : Int,
        @Body requestCreateRequest: RecruitCreateRequest
    ) : Call<Boolean>

    @DELETE("/community/delete/{rid}")
    fun deleteCommunity(
        @Path("rid") rid : Int
    ) : Call<Boolean>

    @GET("/community/new")
    fun getLatest5Community() : Call<List<Community>>

    // 댓글 생성
    @POST("/community/comment/create")
    fun insertComment(
        @Body commentRequest: CommentRequest
    ) : Call<Boolean>

    // 댓글 삭제
    @DELETE("/community/comment/delete/{coid}")
    fun deleteComment(
        @Path("coid") coid : Int
    ) : Call<Boolean>

    // 댓글 목록 불러오기
    @GET("/community/comment/list/{community_id}")
    fun getCommentList(
        @Path("community_id") community_id : Int
    ) : Call<List<Comment>>

    // 댓글 수정
    @PUT("/community/comment/update")
    fun updateComment(
        @Body commentUpdateRequest: CommentUpdateRequest
    ) : Call<Boolean>

}