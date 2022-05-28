package com.ssafy.moabang.data.model.repository

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.withTransaction
import com.ssafy.moabang.data.model.database.AppDatabase
import com.ssafy.moabang.data.model.dto.Cafe
import com.ssafy.moabang.data.model.dto.Theme

import java.lang.IllegalStateException

const val DB_NAME = "moabang.db"

class Repository private constructor(context: Context) {

    private val database: AppDatabase = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        DB_NAME
    ).build()

    private val cafeDao = database.cafeDao()
    private val themeDao = database.themeDao()

    suspend fun getAllCafe(): List<Cafe> =
        database.withTransaction {
            cafeDao.getAllCafe()
        }

    suspend fun getCafe(cid : Int): Cafe =
        database.withTransaction {
            cafeDao.getCafe(cid)
        }

    suspend fun getCafeByIsland(island: String): List<Cafe> =
        database.withTransaction {
            cafeDao.getCafeByIsland(island)
        }

    suspend fun getCafeByIslandSi(island: String, si: String): List<Cafe> =
        database.withTransaction {
            cafeDao.getCafeByIslandSi(island, si)
        }

    suspend fun getCafeByName(name: String): List<Cafe> =
        database.withTransaction {
            cafeDao.getCafeByName(name)
        }

    suspend fun getCafeByNameExactly(cname: String): Cafe =
        database.withTransaction {
            cafeDao.getCafeByNameExactly(cname)
        }

    suspend fun getCafeByCid(cid : Int) : Cafe =
        database.withTransaction {
            cafeDao.getCafeByCid(cid)
        }

    suspend fun insertCafe(cafe: Cafe) =
        database.withTransaction {
            cafeDao.insertCafe(cafe)
        }

    suspend fun insertCafes(cafeList: List<Cafe>) =
        database.withTransaction {
            cafeDao.insertCafes(cafeList)
        }

    suspend fun clearAll() =
        database.withTransaction {
            cafeDao.clearAll()
        }

    suspend fun getAllTheme(): List<Theme> =
        database.withTransaction {
            themeDao.getAllTheme()
        }

    suspend fun getTheme(tid: Int): Theme =
        database.withTransaction {
            themeDao.getTheme(tid)
        }

    suspend fun insertThemes(themeList: List<Theme>) =
        database.withTransaction {
            themeDao.insertThemes(themeList)
        }

    suspend fun filterThemes(island: String, si: ArrayList<String>, genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : MutableList<Theme> =
        database.withTransaction {
            themeDao.filterThemes(island, si, genre, type, plist, diff, active)
        }

    suspend fun filterThemesNoArea(genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : MutableList<Theme> =
        database.withTransaction {
            themeDao.filterThemesNoArea(genre, type, plist, diff, active)
        }

    suspend fun filterLikeThemes(island: String, si: ArrayList<String>, genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : List<Theme> =
        database.withTransaction {
            themeDao.filterLikeThemes(island, si, genre, type, plist, diff, active)
        }

    suspend fun filterLikeThemesNoArea(genre: ArrayList<String>, type: ArrayList<String>, plist: ArrayList<Int>, diff : ArrayList<Int>, active : ArrayList<String>) : List<Theme> =
        database.withTransaction {
            themeDao.filterLikeThemesNoArea(genre, type, plist, diff, active)
        }

    suspend fun setThemeLike(tid: Int, isLike: Boolean) =
        database.withTransaction {
            themeDao.setThemeLike(tid, isLike)
        }

    companion object {
        private var instance: Repository? = null
        fun initialize(context: Context) {
            if (instance == null) {
                instance = Repository(context)
            }
        }

        fun get(): Repository {
            if(instance == null){
                Log.d("AAAAA", "instance not init")
            }else{
                Log.d("AAAAA", "instance already init")
            }
            return instance ?: throw IllegalStateException("초기화 안 됨")
        }
    }
}