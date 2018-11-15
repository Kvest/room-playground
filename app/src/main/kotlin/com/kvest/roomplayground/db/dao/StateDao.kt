package com.kvest.roomplayground.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kvest.roomplayground.db.entity.ItemStateEntity

@Dao
interface StateDao {
    @Query("SELECT * FROM states")
    fun getStates(): List<ItemStateEntity>

    @Query("SELECT * FROM states")
    fun listenStates(): LiveData<List<ItemStateEntity>>

    @Query("SELECT * FROM states WHERE state_id = :id")
    fun getState(id: Long): ItemStateEntity?

    @Query("SELECT * FROM states WHERE state_id = :id")
    fun listenState(id: Long): LiveData<ItemStateEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(item: List<ItemStateEntity>)

    @Query("DELETE FROM states WHERE state_id = :id")
    fun delete(id: Long)

    @Query("UPDATE states SET count = count + :inc WHERE state_id = :id")
    fun incrementCount(id: Long, inc: Int = 1)
}