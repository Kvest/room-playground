package com.kvest.roomplayground.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kvest.roomplayground.db.entity.ItemLocalEntity

@Dao
interface ItemLocalDao {
    @Query("SELECT * FROM local_items")
    fun getLocalItems(): List<ItemLocalEntity>

    @Query("SELECT * FROM local_items")
    fun listenLocalItems(): LiveData<List<ItemLocalEntity>>

    @Query("SELECT * FROM local_items WHERE id = :id")
    fun getLocalItem(id: Long): ItemLocalEntity?

    @Query("SELECT * FROM local_items WHERE id = :id")
    fun listenLocalItem(id: Long): LiveData<ItemLocalEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<ItemLocalEntity>)

    @Query("DELETE FROM local_items WHERE id = :id")
    fun delete(id: Long)

    @Query("UPDATE local_items SET count = count + :inc WHERE id = :id")
    fun incrementCount(id: Long, inc: Int = 1)
}