package com.hd1998.mydiary.data.local.doa

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.flow.Flow

@Dao
interface DiaryDao {
    @Query("SELECT * FROM Diary WHERE id = :id")
    suspend fun getDiaryById(id: String): Diary?

    @Query("SELECT * FROM Diary WHERE title LIKE :query OR text LIKE :query")
     fun searchDiary(query: String): Flow<List<Diary>?>

    @Query("SELECT * FROM Diary")
    fun getAllDiary(): Flow<List<Diary>>

    @Insert
    suspend fun insertDiary(dairy: Diary)

    @Update
    suspend fun updateDiary(dairy: Diary)

    @Delete
    suspend fun deleteDiary(dairy: Diary)

}