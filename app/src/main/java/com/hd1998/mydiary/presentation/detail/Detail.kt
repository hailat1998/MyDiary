package com.hd1998.mydiary.presentation.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.hd1998.mydiary.domain.model.Dairy
import  com.hd1998.mydiary.R
@Composable
fun DetailScreen(dairy: MutableState<Dairy?>,
                 saving : Boolean,
                 deleting: Boolean,
                 toHome: () -> Unit,
                 onSave: (dairy: Dairy) -> Unit,
                 onDelete: (dairy: Dairy) -> Unit){
    BackHandler {
        toHome.invoke()
    }
    if(dairy.value == null){
        Box(contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }else{
        val passwordEntered = remember { mutableStateOf(false) }
        if(dairy.value!!.password != null && !passwordEntered.value ){
     PasswordDialog(dairy = dairy.value!!, passwordEntered = passwordEntered)
        }else{
            DairyDetailContent(dairy.value!!,  saving = saving, deleting = deleting,
                onSave = onSave, onDelete = onDelete , toHome = toHome)
        }
    }
}

@Composable
fun PasswordDialog( dairy: Dairy, passwordEntered: MutableState<Boolean> ) {
    var password by remember { mutableStateOf("") }
     val context = LocalContext.current



    AlertDialog(
        onDismissRequest = {},
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
                    if (password == dairy.password) {
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


@Composable
fun DairyDetailContent(dairy: Dairy,
                       saving : Boolean,
                       deleting: Boolean,
                       onSave: (dairy: Dairy) -> Unit,
                       onDelete: (dairy: Dairy) -> Unit,
                       toHome: () -> Unit){
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var encryptWithPassword by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var password2 by remember { mutableStateOf("") }
    var isValidPassword by remember { mutableStateOf(false) }
    var isValidTitle by remember { mutableStateOf(false) }
    var isValidText by remember { mutableStateOf(false) }
    val context = LocalContext.current


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it
                isValidTitle = it.isNotEmpty()
            },
            label = { Text("Title") },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))


        OutlinedTextField(
            value = details,
            onValueChange = { details = it
                isValidText = it.isNotEmpty()},
            label = { Text("Details") },
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            maxLines = 15        )

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
        AnimatedVisibility(visible = encryptWithPassword ) {
            Column {
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = password2,
                    onValueChange = { password2 = it
                        isValidPassword = password == password2},
                    label = { Text("Confirm password") },
                    visualTransformation = PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth()
                )

            }
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                onSave.invoke(Dairy(title = title, text = details, password = if (encryptWithPassword) password else null))
                Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show()
            },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B8D14)),
                enabled = isValidText && isValidTitle && (isValidPassword || !encryptWithPassword),
                shape = RoundedCornerShape(10.dp)
            ) {
                if(saving){
                  CircularProgressIndicator()
                }else{
                     Text("Save", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                 }
             }
            Button(onClick = { onDelete.invoke(dairy)
                             toHome.invoke()},
                colors = ButtonDefaults.buttonColors(containerColor =Color(0xFF75221A)),
                shape = RoundedCornerShape(10.dp)) {
               if(deleting){
                   CircularProgressIndicator()
               }else{
                    Text("Delete", fontWeight = FontWeight.Bold, fontSize = 20.sp)
                }
              }
        }
    }
}



@Preview
@Composable
fun Detail(){
  val d = Dairy(
      title = "Sample Title",
      text = "This is a sample text for the dairy entry.",
      password = null // Optional field, can be null
  )
   DetailScreen(dairy = mutableStateOf(d), true, true, {},{},{})
}


