package com.hd1998.mydiary.di

import androidx.room.Room
import com.hd1998.mydiary.data.local.db.DiaryDatabase
import com.hd1998.mydiary.data.local.db.MIGRATION_1_2
import com.hd1998.mydiary.data.local.db.MIGRATION_2_3
import com.hd1998.mydiary.data.repository.RepositoryImp
import com.hd1998.mydiary.domain.repository.Repository
import com.hd1998.mydiary.presentation.detail.DetailViewModel
import com.hd1998.mydiary.presentation.home.HomeScreenViewModel
import com.hd1998.mydiary.presentation.search.SearchViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

const val DB = "diaryDB"
val appModule = module {
   single{
       Room.databaseBuilder(
           androidApplication(),
           DiaryDatabase::class.java,
           DB
       ).addMigrations( MIGRATION_1_2 ,MIGRATION_2_3).build()
   }
    single { get<DiaryDatabase>().dairyDao() }
    single <Repository>{ RepositoryImp(get()) }
    single <CoroutineDispatcher>(named("IO")){ Dispatchers.IO }
    viewModel { HomeScreenViewModel(get(), get(named("IO"))) }
    viewModel{ DetailViewModel(get(), get(named("IO")))}
    viewModel{ SearchViewModel(get(), get(named("IO")))}
}