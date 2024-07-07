package com.hd1998.mydiary.data.local.db

import androidx.room.TypeConverter
import java.util.Date

class DaieryConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}