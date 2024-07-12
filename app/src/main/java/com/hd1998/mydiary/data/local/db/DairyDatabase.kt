package com.hd1998.mydiary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary

@Database(entities = [Diary::class] , version = 1)
@TypeConverters(value = [DaieryConverter::class])
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun dairyDao() : DiaryDao
}