package com.hd1998.mydiary.presentation.auth.component

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.hd1998.mydiary.R
import com.hd1998.mydiary.utils.compose.HorizontalDottedProgressBar

@Composable
fun LogIn(toHome: () -> Unit) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {

        var email by remember { mutableStateOf(TextFieldValue("")) }
        var password by remember { mutableStateOf(TextFieldValue("")) }
        var hasError by remember { mutableStateOf(false) }
        var passwordVisualTransformation by remember {
            mutableStateOf<VisualTransformation>(
                PasswordVisualTransformation()
            )
        }
        val passwordInteractionState = remember { MutableInteractionSource() }
        val emailInteractionState = remember { MutableInteractionSource() }
        var loading by remember { mutableStateOf(false) }

        val context = LocalContext.current

        OutlinedTextField(
            value = email,
            leadingIcon = {
                Icon(painterResource(id = R.drawable.baseline_email_24), null)
            },
            maxLines = 1,
            isError = hasError,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Next
            ),
            colors = TextFieldDefaults.colors(),
            label = { Text(text = "Email address") },
            placeholder = { Text(text = "abc@gmail.com") },
            onValueChange = {
                email = it
            },
            interactionSource = emailInteractionState,
        )

        OutlinedTextField(
            value = password,
            leadingIcon = {
                Icon(painterResource(id = R.drawable.baseline_key_24), null)
            },
            trailingIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.visibility_off_24px),
                    tint = Color.Black,
                    modifier = Modifier.clickable {
                        passwordVisualTransformation =
                            if (passwordVisualTransformation != VisualTransformation.None) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            }
                    },
                    contentDescription = null
                )
            },
            colors = TextFieldDefaults.colors(),
            maxLines = 1,
            isError = hasError,
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            label = { Text(text = "Password") },
            placeholder = { Text(text = "12334444") },
            onValueChange = {
                password = it
            },
            interactionSource = passwordInteractionState,
            visualTransformation = passwordVisualTransformation,
        )


        Button(
            onClick = {
                if (invalidInput(email.text, password.text)) {
                    hasError = true
                    loading = false
                } else {
                    loading = true
                    hasError = false
                  loginUser(email.text, password.text){ success, message ->
                      if (success) {
                         toHome.invoke()
                      } else {
                          Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                      }

                  }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(50.dp)
                .clip(CircleShape)
        ) {
            if (loading) {
                HorizontalDottedProgressBar()
            } else {
                Text(text = "Log In")
            }
        }
    }
}


fun invalidInput(email: String, password: String) =
    email.isBlank() || password.isBlank()


fun loginUser(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
    val auth = FirebaseAuth.getInstance()
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                onResult(true, null) // Login successful
            } else {
                onResult(false, task.exception?.message) // Login failed
            }
        }
}