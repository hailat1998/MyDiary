package com.hd1998.mydiary.presentation

import SearchScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.hd1998.mydiary.presentation.detail.DairyNewDetailContent
import com.hd1998.mydiary.presentation.detail.DetailScreen
import com.hd1998.mydiary.presentation.detail.DetailViewModel
import com.hd1998.mydiary.presentation.home.HomeScreen
import com.hd1998.mydiary.presentation.home.HomeScreenViewModel
import com.hd1998.mydiary.presentation.search.SearchViewModel
import org.koin.androidx.compose.koinViewModel

sealed class Destination(val route: String){
    data object Home: Destination("home")
    data object Detail: Destination("detail")
    data object Search : Destination("search")
    data object NewDetail: Destination("newDetail")
}


@Composable
fun App(navController: NavHostController){
  NavHost(navController = navController, startDestination = Destination.Home.route){
    composable(Destination.Home.route) {
        val viewModel = koinViewModel<HomeScreenViewModel>()
        val homeScreenState by viewModel.screenState.collectAsState()
        HomeScreen(homeScreenState = homeScreenState,
            toDairyDetail = { id ->
        navController.navigateSingleTopTo(Destination.Detail.route+"/${id}")
                                 },
            toNewDairy = {
                navController.navigateSingleTopTo(Destination.NewDetail.route)
            },
            toSearch = {
            navController.navigateSingleTopTo(Destination.Search.route)
            })
    }
      composable(Destination.Detail.route,
          arguments = listOf(navArgument("id") { type = NavType.StringType })){ backStackEntry ->
          val id = backStackEntry.arguments?.getString("id")
          val viewModel = koinViewModel<DetailViewModel>()
          viewModel.getDairy(id!!)
          val dairy = remember {
              mutableStateOf(viewModel.dairyState)
          }
          DetailScreen(dairy = dairy, onSave = viewModel::saveDairy, onDelete = viewModel::deleteDairy, toHome =  {
              navController.navigateSingleTopTo(Destination.Home.route)
          }  )
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
      DairyNewDetailContent(onSave = viewModel::saveDairy , toHome = {
          navController.navigateSingleTopTo(Destination.Home.route)
      })
      }
  }
}


fun NavHostController.navigateSingleTopTo(route: String) =
    this.navigate(route) { launchSingleTop = true }