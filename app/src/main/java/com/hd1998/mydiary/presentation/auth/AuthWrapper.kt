package com.hd1998.mydiary.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.presentation.auth.component.LogInScreen
import com.hd1998.mydiary.presentation.auth.component.SignupScreen
import com.hd1998.mydiary.utils.isInternetAvailable
import kotlinx.coroutines.delay

private const val SplashWaitTime: Long = 2000

@Composable
fun AuthWrapper(
    shouldLogIn: State<Boolean>,
    addUser: (user: User) -> Unit,
    toHome: () -> Unit
) {
    var wait by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        wait = true
        delay(SplashWaitTime)
        wait = false
    }

    if (wait) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            SplashScreen()
        }
    } else {
        val context = LocalContext.current
        if (isInternetAvailable(context)) {
            var selectedTabIndex by remember { mutableStateOf(0) }
            val tabs = listOf("Login", "Sign Up")

            Scaffold(
                topBar = {
                    TabRow(selectedTabIndex = selectedTabIndex) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTabIndex == index,
                                onClick = { selectedTabIndex = index },
                                text = { Text(title) }
                            )
                        }
                    }
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
                            LogInScreen(toHome = toHome)
                        } else {
                            toHome.invoke()
                        }
                    } else {
                        if (selectedTabIndex == 0) {
                            LogInScreen(toHome = toHome)
                        } else {
                            SignupScreen(toHome = toHome, addUser = addUser)
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

