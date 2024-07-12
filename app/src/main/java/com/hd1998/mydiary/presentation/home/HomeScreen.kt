package com.hd1998.mydiary.presentation.home

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd1998.mydiary.R
import com.hd1998.mydiary.domain.model.Diary
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState,
    toDiaryDetail: (id: String?) -> Unit,
    toNewDiary: () -> Unit,
    toSearch: () -> Unit
) {
    val lazyListState = rememberLazyListState()
    val context = LocalContext.current as Activity

    BackHandler {
        context.finish()
    }
    Scaffold(
        topBar = { TopBar(toSearch) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { toNewDiary() },
                containerColor = Color(0xFF3E2723)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = "Add Diary",
                    modifier = Modifier
                        .size(68.dp)
                        .padding(10.dp)
                )
            }
        }
    ) { padding ->
        Box(
            Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            LazyColumn(state = lazyListState) {
                itemsIndexed(homeScreenState.list, key = { _, item -> item.id }) { _, item ->
                    DiaryRow(diary = item, toDiaryDetail = toDiaryDetail)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(toSearch: () -> Unit) {
    TopAppBar(
        title = { Text(text = "My Diaries", fontWeight = FontWeight.Bold) },
        actions = {
            Button(onClick = { toSearch() }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search",
             modifier = Modifier
                 .size(65.dp)
                 .padding(end = 18.dp),
                    tint = Color.White
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF74504A) // Dark brown color
        )
    )
}

@Composable
fun DiaryRow(diary: Diary, toDiaryDetail: (id: String?) -> Unit) {
    val dateFormat = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormat.format(diary.date)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { toDiaryDetail(diary.id) }
            .padding(8.dp)
            .background(Color.Gray, RoundedCornerShape(10.dp)),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = diary.title, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp))
            Text(text = formattedDate, fontWeight = FontWeight.Thin, fontSize = 15.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 3.dp, ))
        }
        if(diary.password != null){
            Icon(painter = painterResource(id = R.drawable.lock_24px), null,
                modifier = Modifier.padding(top = 15.dp, end= 10.dp))
        }
    }
}
