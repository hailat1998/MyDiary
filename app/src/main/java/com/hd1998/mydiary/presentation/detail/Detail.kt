package com.hd1998.mydiary.presentation.detail

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.firestore.FirebaseFirestore
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.Diary
import kotlinx.coroutines.tasks.await
import org.koin.compose.getKoin
import org.koin.compose.koinInject
import java.util.Date

@Composable
fun DetailScreen(
    dairy: Diary?,
    saving: Boolean,
    deleting: Boolean,
    toHome: () -> Unit,
    onSave: (dairy: Diary) -> Unit,
    onDelete: (dairy: Diary) -> Unit) {
    BackHandler {
        toHome.invoke()
    }
    Surface(modifier = Modifier.fillMaxSize()) {
        if (dairy == null) {
            Box(contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            val passwordEntered = remember { mutableStateOf(false) }
            var first by remember { mutableIntStateOf(0) }
            if (dairy.password != null && !passwordEntered.value && first ==0 ) {
                PasswordDialog(diary = dairy, passwordEntered = passwordEntered, toHome )
            } else {
                first = 1
                DairyDetailContent(
                    dairy , saving = saving, deleting = deleting,
                    onSave = onSave, onDelete = onDelete, toHome = toHome
                )
            }
        }
    }
}
@Composable
fun PasswordDialog( diary: Diary, passwordEntered: MutableState<Boolean> ,  toHome: () -> Unit) {
    var password by remember { mutableStateOf("") }
     val context = LocalContext.current

    AlertDialog(
        onDismissRequest = {
           toHome.invoke()
        },
        title = {
            Text(text = "Enter Password")
        },
        text = {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
              Image(painter = painterResource(id = R.drawable._99105_lock_icon), null,
                  modifier = Modifier.size(100.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (password == diary.password) {
                        passwordEntered.value = true
                    }else{
                       Toast.makeText(context, "Password Doesn't match", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Text("Confirm")
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DairyDetailContent(diary: Diary,
                       saving : Boolean,
                       deleting: Boolean,
                       onSave: (dairy: Diary) -> Unit,
                       onDelete: (dairy: Diary) -> Unit,
                       toHome: () -> Unit) {
    var title by remember { mutableStateOf(diary.title) }
    var details by remember { mutableStateOf(diary.text) }
    var encryptWithPassword by remember { mutableStateOf(diary.password != null) }
    var password by remember { mutableStateOf(diary.password ?: "") }
    var password2 by remember { mutableStateOf(diary.password ?: "") }
    var isValidPassword by remember { mutableStateOf(diary.password != null) }
    var isValidTitle by remember { mutableStateOf(diary.title.isNotEmpty()) }
    var isValidText by remember { mutableStateOf(diary.text.isNotEmpty()) }
    var submitted by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current


    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }


    Scaffold(
        topBar = { TopAppBar(title = {Text("Diary Details", fontWeight = FontWeight.Bold)
        },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color(0xFF6F5753) // Dark brown color
            )
        )
        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .padding(it)){

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
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

                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
                isError = submitted && !isValidTitle,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF74504A)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
            )

            Spacer(modifier = Modifier.height(15.dp))

            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (isValidText && isValidTitle) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_content_copy_24),
                        contentDescription = null,
                        modifier = Modifier
                            .clickable {
                                val annotatedString = buildAnnotatedString {
                                    withStyle(style = SpanStyle(textDecoration = TextDecoration.None)) {
                                        append(title + "\n\n" + details)
                                    }
                                }
                                clipboardManager.setText(annotatedString)
                            }
                            .padding(end = 8.dp) // Padding between icons
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_share_24),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            val shareText = Intent(Intent.ACTION_SEND).apply {
                                type = "text/plain"
                                putExtra(Intent.EXTRA_TEXT, title + "\n\n" + details)
                            }
                            val chooserIntent = Intent.createChooser(shareText, "My Diary")
                            context.startActivity(chooserIntent)
                        }
                    )
                }
            }

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
                textStyle = TextStyle(
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                ),
                maxLines = 20,
                isError = submitted && !isValidText,
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF74504A)),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
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
                        onValueChange = { password = it
                            isValidPassword = password == password2
                                        },
                        label = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
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
                            diary.title = title
                            diary.text = details
                            diary.password = if(encryptWithPassword) password else null
                            diary.date = Date()
                            onSave.invoke(
                                diary
                            )
                            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
                            toHome.invoke()
                        } else {
                            Toast.makeText(context, "fill everything right", Toast.LENGTH_SHORT)
                                .show()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B8D14)),
                    enabled = isValidText && isValidTitle && (isValidPassword || !encryptWithPassword),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(start = 20.dp, bottom = 50.dp)
                ) {
                    if (saving) {
                        CircularProgressIndicator(color = Color(0xFF74504A))
                    } else {
                        Text("Save", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
                Button(
                    onClick = {
                        onDelete.invoke(diary)
                        toHome.invoke()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF75221A)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.padding(end = 20.dp, bottom = 50.dp)
                ) {
                    if (deleting) {
                        CircularProgressIndicator(color = Color(0xFF74504A))
                    } else {
                        Text("Delete", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    }
                }
            }
        }
    }
 }
    }

}


@Composable
fun SaveToFirebase(diary: Diary, firestore: FirebaseFirestore = koinInject()){
    firestore.collection("diary-hd").add(diary)
}


@Composable
fun DeleteFirebase(diary: Diary, firestore: FirebaseFirestore = koinInject()){
    firestore.collection("diary-hd").document(diary.id)
        .delete()
}

@Composable
fun UpdateFirebase(diary: Diary, firestore: FirebaseFirestore = koinInject()){
    val query = firestore.collection("diary-hd").document(diary.id)

    val password = if (diary.password != null) diary.password else ""

    val updates = hashMapOf<String, Any>(
        "password" to password!!,
        "text" to diary.text,
        "title" to diary.title,
        "date" to diary.date.time
    )
    query.update(updates)
}
