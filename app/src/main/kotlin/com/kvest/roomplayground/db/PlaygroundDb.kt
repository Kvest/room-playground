package com.kvest.roomplayground.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kvest.roomplayground.db.dao.ItemDao
import com.kvest.roomplayground.db.dao.ItemLocalDao
import com.kvest.roomplayground.db.entity.ItemEntity
import com.kvest.roomplayground.db.entity.ItemLocalEntity

private const val DB_VERSION = 1
const val DB_NAME = "playground.db"

@Database(entities = [ItemEntity::class, ItemLocalEntity::class], version = DB_VERSION)
abstract class PlaygroundDb : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun itemLocalDao(): ItemLocalDao
}