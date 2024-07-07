package com.hd1998.mydiary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hd1998.mydiary.data.local.doa.DairyDao
import com.hd1998.mydiary.domain.model.Dairy

@Database(entities = [Dairy::class] , version = 1)
@TypeConverters(value = [DaieryConverter::class])
abstract class DairyDatabase : RoomDatabase() {
    abstract fun dairyDao() : DairyDao
}