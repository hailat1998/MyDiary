package com.hd1998.mydiary.presentation.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.presentation.SplashScreen
import com.hd1998.mydiary.presentation.auth.component.LogInScreen
import com.hd1998.mydiary.presentation.auth.component.SignupScreen
import com.hd1998.mydiary.utils.SplashWaitTime
import com.hd1998.mydiary.utils.isInternetAvailable
import kotlinx.coroutines.delay


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthWrapper(
    shouldLogIn: State<Boolean>,
    addUser: (user: User) -> Unit,
    updateUser: () -> Unit,
    toHome: () -> Unit
) {
    val context = LocalContext.current
    val id = FirebaseAuth.getInstance().currentUser?.uid
    var wait by remember { mutableStateOf(true) }
    LaunchedEffect(Unit) {
        delay(SplashWaitTime)
        wait = false
    }

    if (wait) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            SplashScreen()
        }
    } else {
        if (id != null && !shouldLogIn.value) {
            toHome.invoke()
        }
        if (isInternetAvailable(context)) {
            var selectedTabIndex = remember { mutableIntStateOf(0) }

            Scaffold(
                topBar = {

                }
            ) { paddingValues ->
                Box(
                    modifier = Modifier
                        .padding(paddingValues)
                        .fillMaxSize()
                ) {
                    val firebaseUser = FirebaseAuth.getInstance().currentUser
                    if (firebaseUser != null) {
                        if (shouldLogIn.value) {
                            AnimatedVisibility(
                                visible = shouldLogIn.value,
                                enter = slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 500)
                                ),
                            ) {
                                LogInScreen(
                                    toHome = toHome,
                                    updateUser = updateUser,
                                    addUser = addUser,
                                    selectedTab = selectedTabIndex
                                )
                            }

                        } else {
                            toHome.invoke()
                        }
                    } else {
                        AnimatedVisibility(
                            visible = selectedTabIndex.intValue == 0,
                            enter = slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(durationMillis = 500)
                            ),
                        ) {
                            LogInScreen(toHome = toHome, updateUser, addUser, selectedTabIndex)
                        }
                        AnimatedVisibility(
                            visible = selectedTabIndex.intValue == 1,
                            enter = slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 500)
                            ),
                        ) {
                            SignupScreen(
                                toHome = toHome,
                                addUser = addUser,
                                selectedTab = selectedTabIndex
                            )
                        }
                    }
                }
            }
        } else {
            Toast.makeText(context, "Using offline", Toast.LENGTH_SHORT).show()
            toHome.invoke()
        }
    }
}



