package com.hd1998.mydiary.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary



@Database(entities = [Diary::class] , version = 3)
@TypeConverters(value = [DiaryConverter::class])
abstract class DiaryDatabase : RoomDatabase() {
    abstract fun dairyDao() : DiaryDao
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
                    CREATE TABLE Diary_new (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        text TEXT NOT NULL,
                        date INTEGER NOT NULL
                    )
                """.trimIndent())


        db.execSQL("""
                    INSERT INTO Diary_new (id, title, text, date)
                    SELECT id, title, text, date
                    FROM Diary
                """.trimIndent())


        db.execSQL("DROP TABLE Diary")


        db.execSQL("ALTER TABLE Diary_new RENAME TO Diary")
    }
}

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(db: SupportSQLiteDatabase) {

        db.execSQL("""
                    CREATE TABLE Diary_new (
                        id TEXT PRIMARY KEY NOT NULL,
                        title TEXT NOT NULL,
                        text TEXT NOT NULL,
                        date INTEGER NOT NULL,
                        password TEXT 
                    )
                """.trimIndent())


        db.execSQL("""
                    INSERT INTO Diary_new (id, title, text, date)
                    SELECT id, title, text, date
                    FROM Diary
                """.trimIndent())


        db.execSQL("DROP TABLE Diary")


        db.execSQL("ALTER TABLE Diary_new RENAME TO Diary")
    }
}
