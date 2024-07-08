package com.hd1998.mydiary.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hd1998.mydiary.dairyList
import com.hd1998.mydiary.domain.model.Dairy
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(
    homeScreenState: HomeScreenState,
    toDairyDetail: (id: String?) -> Unit,
    toSearch: () -> Unit
) {
    val lazyListState = rememberLazyListState()

    Scaffold(
        topBar = { TopBar(toSearch) }
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
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
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
    ) {
        Column {
            Text(text = dairy.title, fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Text(text = formattedDate, fontWeight = FontWeight.Thin, fontSize = 15.sp)
        }
    }
}
@Preview
@Composable
fun DRow(){
  val h = HomeScreenState(list = dairyList)
    HomeScreen(homeScreenState = h, toDairyDetail ={}, toSearch = {} )
}