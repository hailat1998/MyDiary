package com.hd1998.mydiary.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd1998.mydiary.dairyList
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.presentation.theme.MyDiaryTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState,
    toDairyDetail: (id: String?) -> Unit,
    toNewDairy: () -> Unit,
    toSearch: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = { TopBar(toSearch) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { toNewDairy() },
                containerColor = Color(0xFF3E2723) // Dark brown background color for FAB
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
                    DairyRow(dairy = item, toDairyDetail = toDairyDetail)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(toSearch: () -> Unit) {
    TopAppBar(
        title = { Text(text = "My Diaries") },
        actions = {
            IconButton(onClick = { toSearch() }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search",
             modifier = Modifier
                   .size(50.dp, 50.dp).padding(end= 10.dp))
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xFF3E2723) // Dark brown color
        )
    )
}

@Composable
fun DairyRow(dairy: Dairy, toDairyDetail: (id: String?) -> Unit) {
    val dateFormat = SimpleDateFormat("dd MM yyyy, HH:mm:ss", Locale.getDefault())
    val formattedDate = dateFormat.format(dairy.date)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { toDairyDetail(dairy.id) }
            .padding(8.dp)
            .background(Color.Gray, RoundedCornerShape(10.dp))
    ) {
        Column {
            Text(text = dairy.title, fontWeight = FontWeight.Bold, fontSize = 20.sp,
                modifier = Modifier.padding(start = 15.dp, top = 5.dp, bottom = 5.dp))
            Text(text = formattedDate, fontWeight = FontWeight.Thin, fontSize = 15.sp,
                modifier = Modifier.padding(start = 15.dp, bottom = 3.dp, ))
        }
    }
}
@Preview
@Composable
fun DRow(){
    val j = HomeScreenState(list = dairyList)
   // MyDiaryTheme {
        HomeScreen(homeScreenState = j, toDairyDetail = {} , toNewDairy = { /*TODO*/ }, toSearch =  {})
   // }

}