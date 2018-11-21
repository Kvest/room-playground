package com.kvest.roomplayground.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kvest.roomplayground.db.entity.ItemEntity
import com.kvest.roomplayground.db.entity.StatefulItemEntity

@Dao
interface StatefulItemDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<ItemEntity>)

    @Update
    fun update(vararg items: ItemEntity)

    @Delete
    fun deleteItems(vararg items: ItemEntity)

    @Query("DELETE FROM items WHERE id = :id")
    fun deleteItem(id: Long)

    @Query("UPDATE states SET count = count + :inc WHERE state_id = :id")
    fun incrementCount(id: Long, inc: Int = 1)

    @Query("SELECT * FROM StatefulItemEntity")
    fun getAll(): List<StatefulItemEntity>

    @Query("SELECT * FROM StatefulItemEntity WHERE id = :id")
    fun getItem(id: Long): StatefulItemEntity?

    @Query("SELECT * FROM StatefulItemEntity")
    fun listenAll(): LiveData<List<StatefulItemEntity>>

    @Query("SELECT * FROM StatefulItemEntity ORDER BY change_time")
    fun listenAllSorted(): LiveData<List<StatefulItemEntity>>
}