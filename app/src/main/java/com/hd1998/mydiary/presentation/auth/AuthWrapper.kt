package com.hd1998.mydiary.presentation.auth

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.presentation.auth.component.LogInScreen
import com.hd1998.mydiary.presentation.auth.component.SignupScreen
import com.hd1998.mydiary.utils.isInternetAvailable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthWrapper(
    shouldLogIn: State<Boolean>,
    addUser: (user: User) -> Unit,
    updateUser: () -> Unit,
    toHome: () -> Unit
) {
        val context = LocalContext.current
        if (isInternetAvailable(context)) {
            var selectedTabIndex = remember { mutableIntStateOf(0) }
            val tabs = listOf("Login", "Sign Up")

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
                            AnimatedVisibility(visible = shouldLogIn.value,
                                enter = slideInHorizontally(
                                    initialOffsetX = { -it },
                                    animationSpec = tween(durationMillis = 500)
                                ),
                                ) {
                                LogInScreen(toHome = toHome, updateUser= updateUser, addUser= addUser, selectedTab = selectedTabIndex)
                            }

                        } else {
                            toHome.invoke()
                        }
                    } else {
                        AnimatedVisibility(visible = selectedTabIndex.intValue == 0,
                            enter = slideInHorizontally(
                                initialOffsetX = { -it },
                                animationSpec = tween(durationMillis = 500)
                            ),
                        ) {
                            LogInScreen(toHome = toHome, updateUser, addUser, selectedTabIndex)
                        }
                        AnimatedVisibility(visible = selectedTabIndex.intValue == 1,
                            enter = slideInHorizontally(
                                initialOffsetX = { it },
                                animationSpec = tween(durationMillis = 500)
                            ),
                        ){
                            SignupScreen(toHome = toHome, addUser = addUser, selectedTab = selectedTabIndex)
                        }
                    }
                }
            }
        } else {
            Toast.makeText(context, "Using offline", Toast.LENGTH_SHORT).show()
            toHome.invoke()
        }
    }



