package com.hd1998.mydiary

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.hd1998.mydiary.data.local.db.DiaryDatabase
import com.hd1998.mydiary.data.local.doa.DiaryDao
import com.hd1998.mydiary.domain.model.Diary
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.text.SimpleDateFormat
import java.util.Locale

@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class DatabaseTest {

   private lateinit var database: DiaryDatabase
   private lateinit var diaryDao: DiaryDao

   @Before
   fun setUp(){
       database =  Room.inMemoryDatabaseBuilder(
           ApplicationProvider.getApplicationContext(),
           DiaryDatabase::class.java
       ).allowMainThreadQueries().build()
       diaryDao = database.dairyDao()
   }

    @After
    fun tearDown() {
        // Close the database after tests
        database.close()
    }

    @Test
    fun insertDiary()= runBlocking{
        val diary = Diary(
            id = "1234" ,
            title = "Evening Relaxation",
            text = "Spent the evening reading a book and drinking tea.",
            date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("03 07 2024, 20:00:00")!!,
            password = "12345"
        )
        val diary2 =  Diary(
            id = "12345" ,
            title = "Evening Relaxation",
            text = "Spent the evening reading a book and drinking tea.",
            date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("03 07 2024, 20:00:00")!!,
            password = "12345"
        )

        diaryDao.insertDiary(diary)
        diaryDao.insertDiary(diary2)


        val d = diaryDao.getDiaryById("1234")
        assertEquals(diary , d)
    }

    @Test
    fun deleteDiary() = runBlocking {
        val diary = Diary(
            id = "1234" ,
            title = "Evening Relaxation",
            text = "Spent the evening reading a book and drinking tea.",
            date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("03 07 2024, 20:00:00")!!,
            password = "12345"
        )
        val diary2 =  Diary(
            id = "12345" ,
            title = "Evening Relaxation",
            text = "Spent the evening reading a book and drinking tea.",
            date = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault()).parse("03 07 2024, 20:00:00")!!,
            password = "12345"
        )

        diaryDao.insertDiary(diary)
        diaryDao.insertDiary(diary2)
        diaryDao.deleteDiary(diary)


        diaryDao.getAllDiary()
            .collect{

            }

    }
}

