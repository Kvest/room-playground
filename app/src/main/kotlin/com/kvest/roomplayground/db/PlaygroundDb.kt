package com.kvest.roomplayground.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kvest.roomplayground.db.dao.ItemDao
import com.kvest.roomplayground.db.entity.ItemEntity

private const val DB_VERSION = 1
const val DB_NAME = "playground.db"

@Database(entities = [ItemEntity::class], version = DB_VERSION)
abstract class PlaygroundDb : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}