package com.kvest.roomplayground.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.kvest.roomplayground.db.dao.ItemDao
import com.kvest.roomplayground.db.dao.StatefulItemDao
import com.kvest.roomplayground.db.dao.StateDao
import com.kvest.roomplayground.db.entity.ItemEntity
import com.kvest.roomplayground.db.entity.ItemStateEntity
import com.kvest.roomplayground.db.entity.StatefulItemEntity

private const val DB_VERSION = 1
const val DB_NAME = "playground.db"

@Database(
    entities = [ItemEntity::class, ItemStateEntity::class],
    views = [StatefulItemEntity::class],
    version = DB_VERSION)
abstract class PlaygroundDb : RoomDatabase() {
    abstract fun itemDao(): ItemDao
    abstract fun stateDao(): StateDao
    abstract fun statefulItemDao(): StatefulItemDao
}