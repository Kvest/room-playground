package com.kvest.roomplayground.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "states")
data class ItemStateEntity(
    @PrimaryKey
    @ColumnInfo(name = "state_id")
    val id: Long,

    @ColumnInfo(name = "count")
    val count: Int,

    @ColumnInfo(name = "change_time")
    val changeTime: Long
)