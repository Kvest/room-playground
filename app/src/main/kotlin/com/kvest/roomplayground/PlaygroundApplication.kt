package com.kvest.roomplayground

import android.app.Application
import androidx.room.Room
import com.kvest.roomplayground.db.DBCallback
import com.kvest.roomplayground.db.DB_NAME
import com.kvest.roomplayground.db.PlaygroundDb

class PlaygroundApplication : Application() {
    val db by lazy { createDB() }

    private fun createDB() =
        Room.databaseBuilder(this, PlaygroundDb::class.java, DB_NAME)
            .addCallback(DBCallback)
            .build()
}