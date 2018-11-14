package com.kvest.roomplayground.db

import android.content.Context
import androidx.room.Room

fun createDB(context: Context) =
    Room.inMemoryDatabaseBuilder(context, PlaygroundDb::class.java)
        .addCallback(DBCallback)
        .allowMainThreadQueries()
        .build()