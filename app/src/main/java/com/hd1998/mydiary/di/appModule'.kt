package com.hd1998.mydiary.di

import androidx.room.Room
import com.hd1998.mydiary.data.local.db.DairyDatabase
import com.hd1998.mydiary.data.repository.RepositoryImp
import com.hd1998.mydiary.domain.repository.Repository
import com.hd1998.mydiary.presentation.detail.Detail
import com.hd1998.mydiary.presentation.detail.DetailViewModel
import com.hd1998.mydiary.presentation.home.HomeScreenViewModel
import com.hd1998.mydiary.presentation.search.SearchViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module
const val DB = "dairyDB"
val appModule = module {
   single{
       Room.databaseBuilder(
           androidApplication(),
           DairyDatabase::class.java,
           DB
       ).build()
   }
    single { get<DairyDatabase>().dairyDao() }
    single <Repository>{ RepositoryImp(get()) }
    viewModel { HomeScreenViewModel(get()) }
    viewModel{ DetailViewModel(get())}
    viewModel{ SearchViewModel(get())}
}