package com.kvest.roomplayground.db.entity

import androidx.room.DatabaseView
import androidx.room.Embedded


@DatabaseView(
    "SELECT items.*, states.* FROM items LEFT JOIN states ON states.state_id = items.id"
)
data class StatefulItemEntity(
    @Embedded
    val item: ItemEntity,
    @Embedded
    val state: ItemStateEntity
)