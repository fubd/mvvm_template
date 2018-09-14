package com.viet.news.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.viet.news.core.config.Config

/**
 * @Author Aaron
 * @Email aaron@magicwindow.cn
 * @Date 2018/9/12
 * @Description
 */
@Dao
interface OnlyNullDao {

    @Query("SELECT * FROM " + Config.NULL_TABLE_NAME)
    fun getNull(): LiveData<NullEntity>

//    @Query("SELECT * FROM " + Config.FAVORITE_TABLE_NAME)
//    fun getNull(): LiveData<Any>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(favorite: NullEntity)

    @Delete
    fun deleteSource(favorite: NullEntity)

//    fun insertSources(source: List<Source>) {
//
//        insertSources(*sourceEntityArray.toTypedArray())
//    }
}