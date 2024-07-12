package com.hd1998.mydiary.presentation.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.Diary

@Composable
fun DiaryNewDetailContent(
    saving: Boolean,
    toHome: () -> Unit,
    onSave: (diary: Diary) -> Unit
                      ) {
    BackHandler {
        toHome.invoke()
    }
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var encryptWithPassword by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var isValidPassword by remember { mutableStateOf(false) }
    var isValidTitle by remember { mutableStateOf(false) }
    var isValidText by remember { mutableStateOf(false) }
    var submitted by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Surface(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            item {
                OutlinedTextField(
                    value = title,
                    onValueChange = {
                        title = it
                        isValidTitle = it.isNotEmpty()
                    },
                    label = { Text("Title") },

                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    isError = submitted && !isValidTitle,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF74504A))
                )

                Spacer(modifier = Modifier.height(50.dp))


                OutlinedTextField(
                    value = details,
                    onValueChange = {
                        details = it
                        isValidText = it.isNotEmpty()
                    },
                    label = { Text("Details") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(400.dp),
                    maxLines = 15,
                    textStyle = TextStyle(
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp
                    ),
                    isError = submitted && !isValidText,
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF74504A))
                )

                Spacer(modifier = Modifier.height(15.dp))


                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = encryptWithPassword,
                        onCheckedChange = { encryptWithPassword = it }
                    )
                    Text("Encrypt with password")
                }

                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = encryptWithPassword) {
                    Column {
                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            isError = submitted && !isValidPassword && encryptWithPassword,
                            shape = RoundedCornerShape(20.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_key_24),
                                    null
                                )
                            }
                        )
                        TextField(
                            value = password2,
                            onValueChange = {
                                password2 = it
                                isValidPassword = password == password2
                            },
                            label = { Text("Confirm password") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            isError = submitted && !isValidPassword && encryptWithPassword,
                            shape = RoundedCornerShape(20.dp),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.baseline_key_24),
                                    null
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            submitted = true
                            if (isValidText && (isValidPassword || !encryptWithPassword) && isValidTitle) {
                                onSave.invoke(
                                    Diary(
                                        title = title,
                                        text = details,
                                        password = if (encryptWithPassword) password else null
                                    )
                                )
                                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "fill everything right", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B8D14)),
                        enabled = isValidText && isValidTitle && (isValidPassword || !encryptWithPassword),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (saving) {
                            CircularProgressIndicator(color = Color(0xFF74504A))
                        } else {
                            Text("Save", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        }
                    }
                }
            }
        }
    }
}

