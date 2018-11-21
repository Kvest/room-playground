package com.kvest.roomplayground.db

import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase

object DBCallback : RoomDatabase.Callback() {
    override fun onCreate(db: SupportSQLiteDatabase) {
        super.onCreate(db)

        //create triggers for changing state of the shipment
        db.execSQL("CREATE TRIGGER IF NOT EXISTS UPDATE_CHANGE_TIME_TRIGGER AFTER UPDATE OF count ON states BEGIN UPDATE states SET change_time = (julianday(\'now\') - 2440587.5)*86400000 WHERE state_id = NEW.state_id; END")
        db.execSQL("CREATE TRIGGER IF NOT EXISTS CREATE_CHANGE_TIME_TRIGGER AFTER INSERT ON states BEGIN UPDATE states SET change_time = (julianday(\'now\') - 2440587.5)*86400000 WHERE state_id = NEW.state_id; END")
        db.execSQL("CREATE TRIGGER IF NOT EXISTS CREATE_STATE_TRIGGER AFTER INSERT ON items BEGIN INSERT INTO states(state_id, count, change_time) VALUES (NEW.id, 0, 0); END")
    }
}