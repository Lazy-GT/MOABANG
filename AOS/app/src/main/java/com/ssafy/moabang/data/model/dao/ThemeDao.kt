package com.ssafy.moabang.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Theme

@Dao
interface ThemeDao {

    @Query("SELECT * FROM Theme")
    fun getAllTheme() : List<Theme>

    @Query("SELECT * FROM Theme WHERE tid = :tid")
    fun getTheme(tid : Int) : Theme

    @Insert
    fun insertTheme(theme : Theme)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertThemes(themeList : List<Theme>)

    @Query("SELECT * FROM Theme WHERE genre IN (:genre) AND type IN (:type) AND (min_player IN (:plist) OR max_player IN (:plist)) AND difficulty IN (:diff) AND activity IN (:active)")
    fun filterThemesNoArea(genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : MutableList<Theme>

    @Query("SELECT * FROM Theme WHERE island = :island AND si IN (:si) AND genre IN (:genre) AND type IN (:type) AND (min_player IN (:plist) OR max_player IN (:plist)) AND difficulty IN (:diff) AND activity IN (:active)")
    fun filterThemes(island: String, si: ArrayList<String>, genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : MutableList<Theme>

    @Query("SELECT * FROM Theme WHERE islike = 1 AND genre IN (:genre) AND type IN (:type) AND (min_player IN (:plist) OR max_player IN (:plist)) AND difficulty IN (:diff) AND activity IN (:active)")
    fun filterLikeThemesNoArea(genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : List<Theme>

    @Query("SELECT * FROM Theme WHERE islike = 0 AND  island = :island AND si IN (:si) AND genre IN (:genre) AND type IN (:type) AND (min_player IN (:plist) OR max_player IN (:plist)) AND difficulty IN (:diff) AND activity IN (:active)")
    fun filterLikeThemes(island: String, si: ArrayList<String>, genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : List<Theme>

    @Query("UPDATE Theme SET isLike = :isLike WHERE tid = :tid")
    fun setThemeLike(tid: Int, isLike: Boolean)

    @Query("DELETE FROM Theme")
    fun clearAll()
}