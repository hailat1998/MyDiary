package com.hd1998.mydiary.presentation.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.presentation.auth.component.LogIn
import com.hd1998.mydiary.presentation.auth.component.SignupScreen
import com.hd1998.mydiary.utils.isInternetAvailable
import kotlinx.coroutines.delay
import androidx.compose.runtime.setValue
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.User

private const val SplashWaitTime: Long = 2000

@Composable
fun AuthWrapper(shouldLogIn: State<Boolean>, addUser:(user: User) -> Unit, toHome: () -> Unit) {

    var wait by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        wait = true
        delay(SplashWaitTime)
        wait = false
    }
    if (wait) {
        Box(contentAlignment = Alignment.Center) {
           SplashScreen()
        }
    } else {

        val context = LocalContext.current
        if (isInternetAvailable(context)) {
            val firebaseUser = FirebaseAuth.getInstance()
            if (firebaseUser.currentUser != null && shouldLogIn.value) {
                LogIn(toHome = toHome)
            } else if (firebaseUser.currentUser != null && !shouldLogIn.value) {
                toHome.invoke()
            } else {
                SignupScreen(toHome = toHome, addUser = addUser)
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

