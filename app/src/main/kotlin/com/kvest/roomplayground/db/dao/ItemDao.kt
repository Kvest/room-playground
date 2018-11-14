package com.kvest.roomplayground.db.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kvest.roomplayground.db.entity.ItemEntity

@Dao
interface ItemDao {
    @Query("SELECT * FROM items")
    fun getItems(): List<ItemEntity>

    @Query("SELECT * FROM items")
    fun listenItems(): LiveData<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItem(id: Long): ItemEntity?

    @Query("SELECT * FROM items WHERE id = :id")
    fun listenItem(id: Long): LiveData<ItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<ItemEntity>)

    @Update
    fun update(vararg items: ItemEntity)

    @Delete
    fun delete(vararg items: ItemEntity)
}