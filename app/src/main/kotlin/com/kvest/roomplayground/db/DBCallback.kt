package com.kvest.roomplayground.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DBCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        //create triggers for changing state of the shipment
        db.execSQL("CREATE TRIGGER IF NOT EXISTS UPDATE_CHANGE_TIME AFTER UPDATE OF count ON local_items BEGIN UPDATE local_items SET change_time = (julianday(\'now\') - 2440587.5)*86400000 WHERE id = NEW.id; END")
        db.execSQL("CREATE TRIGGER IF NOT EXISTS CREATE_CHANGE_TIME AFTER INSERT ON local_items BEGIN UPDATE local_items SET change_time = (julianday(\'now\') - 2440587.5)*86400000 WHERE id = NEW.id; END")
    }
}