package com.kvest.roomplayground.db.dao

import androidx.room.Dao
import androidx.room.Query
import com.kvest.roomplayground.db.entity.StatefulItemEntity

@Dao
interface StatefulItemDao {
    @Query("SELECT * FROM StatefulItemEntity")
    fun getAll(): List<StatefulItemEntity>
}