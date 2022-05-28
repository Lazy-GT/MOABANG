package com.ssafy.moabang.data.model.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssafy.moabang.data.model.dto.Cafe

@Dao
interface CafeDao {

    @Query("SELECT * FROM Cafe")
    fun getAllCafe() : List<Cafe>

    @Query("SELECT * FROM Cafe WHERE cid = :cid")
    fun getCafe(cid: Int) : Cafe

    @Query("SELECT * FROM Cafe WHERE island LIKE :island")
    fun getCafeByIsland(island : String) : List<Cafe>

    @Query("SELECT * FROM Cafe WHERE island LIKE :island AND si LIKE :si")
    fun getCafeByIslandSi(island : String, si : String) : List<Cafe>

    @Query("SELECT * FROM Cafe WHERE cname LIKE '%'||:cname||'%'")
    fun getCafeByName(cname : String) : List<Cafe>

    @Query("SELECT * FROM Cafe WHERE cname LIKE :cname")
    fun getCafeByNameExactly(cname : String) : Cafe

    @Query("SELECT * FROM Cafe WHERE cid LIKE :cid")
    fun getCafeByCid(cid : Int) : Cafe

    @Insert
    fun insertCafe(cafe : Cafe)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCafes(cafeList : List<Cafe>)

    @Query("DELETE FROM Cafe")
    fun clearAll()

}