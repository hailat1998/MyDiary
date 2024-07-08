package com.hd1998.mydiary.presentation.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.presentation.Destination

@Composable
fun DetailScreen(dairy: MutableState<Dairy?>){
    if(dairy.value == null){
        Box(contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }else{
        val passwordEntered by remember { mutableStateOf(false) }
        if(dairy.value!!.password != null && !passwordEntered ){

        }
    }
}

@Composable
fun PasswordDialog(onPasswordEntered: (String) -> Unit) {
    var password by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = {},
        title = {
            Text(text = "Enter Password")
        },
        text = {
            Column {
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
                onClick = { onPasswordEntered(password) }
            ) {
                Text("Confirm")
            }
        }
    )
}


@Composable
fun DairyDetailContent(dairy: Dairy,
                       onSave: (String, String, Boolean, String?) -> Unit,
                       onDelete: () -> Unit){
    var title by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var encryptWithPassword by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },

            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(30.dp))

        // Details OutlinedTextField with 7 lines
        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Details") },
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp), // Adjust height to show 7 lines approximately
            maxLines = 15        )

        Spacer(modifier = Modifier.height(15.dp))

        // Encrypt with Password Checkbox
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = encryptWithPassword,
                onCheckedChange = { encryptWithPassword = it }
            )
            Text("Encrypt with password")
        }

        // Password TextField (conditional)
        if (encryptWithPassword) {
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Buttons Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(onClick = {
                onSave(title, details, encryptWithPassword, if (encryptWithPassword) password else null)
            }) {
                Text("Save")
            }

            Button(onClick = { onDelete() }) {
                Text("Delete")
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
      password = "samplePassword123" // Optional field, can be null
  )
    DairyDetailContent(dairy = d, onSave = {_,_,_,_ ->}, onDelete = {})
}


