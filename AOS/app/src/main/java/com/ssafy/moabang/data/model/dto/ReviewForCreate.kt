package com.ssafy.moabang.data.model.dto

data class ReviewForCreate(
var active: String,
var clearTime: Int,
var content: String,
var chaegamDif: Int,
var hint: Int,
var isSuccess: Int,
var playDate: String,
var player: Int,
var rating: Float,
var recPlayer: Int,
var tid: Int
)
