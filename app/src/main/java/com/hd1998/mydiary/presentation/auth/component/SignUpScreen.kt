package com.hd1998.mydiary.presentation.auth.component

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
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.domain.model.User
import com.hd1998.mydiary.utils.compose.HorizontalDottedProgressBar
import java.util.Date

@Composable
fun SignupScreen(
    addUser:(user: User) -> Unit,
    toHome:() -> Unit,
    selectedTab: MutableIntState
) {
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

            var name by remember { mutableStateOf(TextFieldValue("")) }
            var email by remember { mutableStateOf(TextFieldValue("")) }
            var password by remember { mutableStateOf(TextFieldValue("")) }
            var password2 by remember { mutableStateOf(TextFieldValue("")) }
            var hasError by remember { mutableStateOf(false) }
            var passwordVisualTransformation by remember {
                mutableStateOf<VisualTransformation>(
                    PasswordVisualTransformation()
                )
            }

            val context = LocalContext.current

            val passwordInteractionState = remember { MutableInteractionSource() }
            val emailInteractionState = remember { MutableInteractionSource() }

            OutlinedTextField(
                value = name,
                leadingIcon = {
                    Icon(painterResource(id = R.drawable.baseline_person_24), null)
                },
                maxLines = 1,
                isError = hasError,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Next
                ),
                colors = TextFieldDefaults.colors(),
                label = { Text(text = "Full name") },
                placeholder = { Text(text = "name") },
                onValueChange = {
                    name = it
                },
                interactionSource = emailInteractionState,
            )

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

            OutlinedTextField(
                value = password2,
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
                isError = password != password2 ,
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                label = { Text(text = "Confirm Password") },
                placeholder = { Text(text = "12334444") },
                onValueChange = {
                    password2 = it
                },
                interactionSource = passwordInteractionState,
                visualTransformation = passwordVisualTransformation,
            )


            var loading by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if (invalidInput(name.text, email.text, password.text, password2.text)) {
                        hasError = true
                        loading = false
                    } else {
                        loading = true
                        hasError = false

                        val userData = mutableMapOf(
                            "name" to name.text,
                            "email" to email.text,
                            "diaries" to listOf<Diary>()
                        )

                        signUpAndAddUserToFirestore(
                            email = email.text,
                            password = password.text,
                            userData = userData
                        ) { success, message ->
                            if (success) {
                                val id = FirebaseAuth.getInstance().currentUser?.uid
                                val user = User(
                                    id = id!!,
                                    name = name.text,
                                    email = email.text,
                                    date = Date()
                                )
                                addUser.invoke(user)
                                toHome.invoke()
                            } else {
                                loading = false
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
                    Text(text = "Sign up")
                }
            }

            val annotatedString = buildAnnotatedString {
                append("or ")
                pushStringAnnotation(tag = "LOG_IN", annotation = "Login")
                withStyle(
                    style = SpanStyle(
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline
                    )
                ) {
                    append(" Login")
                }
                append(" if you have an account")
            }

            Text(
                text = annotatedString,
                modifier = Modifier
                    .padding(16.dp)
                    .clickable(
                        onClick = {
                            annotatedString.getStringAnnotations("LOG_IN", 0, annotatedString.length)
                                .firstOrNull()?.let { selectedTab.intValue = 0 }
                        }
                    )
                )
        }
    }
}

    fun invalidInput(name: String, email: String, password: String, password2: String) =
       ( email.isBlank() || password.isBlank() || name.isBlank() ) && password2 == password


    fun signUpAndAddUserToFirestore(
        email: String,
        password: String,
        userData: MutableMap<String, Any>,
        onResult: (Boolean, String?) -> Unit
    ) {
        val auth = FirebaseAuth.getInstance()
        val db = FirebaseFirestore.getInstance()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid

                    if (userId != null) {
                        userData.put("id", userId)
                        db.collection("users").document(userId)
                            .set(userData)
                            .addOnSuccessListener {
                                onResult(true, null)
                            }
                            .addOnFailureListener { e ->
                                onResult(false, e.message)
                            }
                    } else {
                        onResult(false, "User ID is null")
                    }
                } else {
                    onResult(false, task.exception?.message) // Auth error
                }
            }

    }
