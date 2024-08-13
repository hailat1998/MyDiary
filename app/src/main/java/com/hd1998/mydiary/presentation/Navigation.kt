package com.hd1998.mydiary.presentation

import SearchScreen
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.R
import com.hd1998.mydiary.presentation.auth.AuthViewmodel
import com.hd1998.mydiary.presentation.auth.AuthWrapper
import com.hd1998.mydiary.presentation.detail.DetailScreen
import com.hd1998.mydiary.presentation.detail.DetailViewModel
import com.hd1998.mydiary.presentation.detail.DiaryNewDetailContent
import com.hd1998.mydiary.presentation.home.HomeScreen
import com.hd1998.mydiary.presentation.home.HomeScreenViewModel
import com.hd1998.mydiary.presentation.search.SearchViewModel
import com.hd1998.mydiary.utils.SplashWaitTime
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

sealed class Destination(val route: String){
    data object Auth: Destination("auth")
    data object Home: Destination("home")
    data object Detail: Destination("detail")
    data object Search : Destination("search")
    data object NewDetail: Destination("newDetail")
}



@Composable
fun App(navController: NavHostController){
  NavHost(navController = navController, startDestination = Destination.Auth.route){

      composable(Destination.Auth.route){
          val viewModel = koinViewModel<AuthViewmodel>()
          val shouldLogIn = viewModel.shouldLogIn.collectAsState()
          AuthWrapper(shouldLogIn = shouldLogIn, addUser = viewModel::addUser, updateUser = viewModel::updateUser) {
              navController.navigateSingleTopTo(Destination.Home.route)
          }
         }

      composable(Destination.Home.route) {
        val viewModel = koinViewModel<HomeScreenViewModel>()
         val diaries = viewModel.loadWithPaging().collectAsLazyPagingItems()
        HomeScreen(diaries = diaries,
            toDiaryDetail = { id ->
                Log.i("NAV", id!!)
        navController.navigateSingleTopTo(Destination.Detail.route.plus("/${id}"))
                            },
            toNewDiary = {
                navController.navigateSingleTopTo(Destination.NewDetail.route)
            },
            toSearch = {
            navController.navigateSingleTopTo(Destination.Search.route)
            },
            refresh = viewModel::refresh,
            loadNext = viewModel::loadNext
              )
      }

      composable(Destination.Detail.route.plus("/{id}"),
          arguments = listOf(navArgument("id") { type = NavType.StringType })){ backStackEntry ->
          val id = backStackEntry.arguments?.getString("id")
          val viewModel = koinViewModel<DetailViewModel>()
          id?.let { viewModel.getDiary(it) }
          val dairy by viewModel.dairyState.collectAsState()
          DetailScreen(dairy = dairy, saving = viewModel.saving, deleting = viewModel.deleting,
              onSave = viewModel::updateDiary, onDelete = viewModel::deleteDiary, toHome =  {
              navController.navigateSingleTopTo(Destination.Home.route)
          }
          )
         }

      composable(Destination.Search.route){
          val viewModel = koinViewModel<SearchViewModel>()
          val list = viewModel.searchResult
              SearchScreen(list = list, toHome = { navController.navigateSingleTopTo(Destination.Home.route) },
                  toDetail = {id ->
                      navController.navigateSingleTopTo(Destination.Detail.route + "/${id}")}, search = viewModel::search)
              }

      composable(Destination.NewDetail.route){
          val viewModel= koinViewModel<DetailViewModel>()
      DiaryNewDetailContent(onSave = viewModel::saveDiary , saving = viewModel.saving, toHome = {
          navController.navigateSingleTopTo(Destination.Home.route)
             }
          )
       }
     }
   }


fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier.wrapContentSize()
        )
    }
}