package com.hd1998.mydiary.presentation

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHost
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.hd1998.mydiary.presentation.home.HomeScreen
import com.hd1998.mydiary.presentation.home.HomeScreenViewModel

sealed class Destination(val route: String){
    data object Home: Destination("home")
    data object Detail: Destination("detail")
    data object Search : Destination("search")
}


@Composable
fun App(navController: NavHostController){
  NavHost(navController = navController, startDestination = Destination.Home.route){
    composable(Destination.Home.route){
        val viewModel : HomeScreenViewModel = koin
        HomeScreen(homeScreenState = , toDairyDetail = ) {

        }
    }
  }
}