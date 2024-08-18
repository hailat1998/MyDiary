package com.hd1998.mydiary.presentation.auth.component

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.utils.compose.HorizontalDottedProgressBar
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

@Composable
fun LogInScreen(toHome: () -> Unit,
                updateUser:() -> Unit,
                addUser: (User) -> Unit,
                selectedTab: MutableIntState
) {
    val idOld = FirebaseAuth.getInstance().currentUser?.uid
    val scope = rememberCoroutineScope()
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Image(painter = painterResource(id = R.drawable.background), null,
                colorFilter = ColorFilter.tint(Color.Transparent, blendMode = BlendMode.Darken),
                contentScale = ContentScale.FillHeight,
                alpha = 0.8f
            )
        }
        Column(verticalArrangement = Arrangement.Center, modifier = Modifier.fillMaxSize()) {
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
                        loginUser(email.text, password.text) { success, message ->
                            if (success) {
                                if(idOld == null){
                                    Log.i("LOGIN", "ADD")
                                    val newId = FirebaseAuth.getInstance().currentUser?.uid
                                    scope.launch(Dispatchers.IO) {
                                        FirebaseFirestore.getInstance().collection("users").document(newId!!).get()
                                            .addOnSuccessListener {document ->
                                                if(document != null){
                                                    val id = document.getString("id")
                                                    val name = document.getString("name")
                                                    val email = document.getString("email")

                                                    val user = User(id = id!!,
                                                          name = name!!,
                                                        date = Date(),
                                                        email = email!!)

                                                     addUser(user)
                                                }

                                            }
                                    }

                                }else{
                                    Log.i("LOGIN", "UPDATE")
                                    updateUser.invoke()
                                }
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

            val annotatedString = buildAnnotatedString {
                append("or ")
                pushStringAnnotation(tag = "SIGN_UP", annotation = "Sign Up")
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append("Sign Up")
                }
                pop()
                append(" if you have an account")
            }

            Text(
                text = annotatedString,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(
                        onClick = {
                            annotatedString
                                .getStringAnnotations("SIGN_UP", 3, annotatedString.length - 23)
                                .firstOrNull()
                                ?.let { selectedTab.intValue = 1 }
                        }
                    )
            )
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