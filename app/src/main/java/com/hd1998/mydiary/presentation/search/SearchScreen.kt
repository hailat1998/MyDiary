import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hd1998.mydiary.diaryList
import com.hd1998.mydiary.domain.model.Diary
import com.hd1998.mydiary.presentation.theme.MyDiaryTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(list: List<Diary> , toHome :() -> Unit, toDetail: (id : String) -> Unit,
                 search: (query: String) -> Unit) {

    var query by remember { mutableStateOf("") }

    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    Surface {

        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = {
                    TextField(
                        value = query,
                        onValueChange = {
                            query = it
                            search.invoke(query.toString())
                            Log.i("From", "HANDLED SEARCH ${query.toString()}")
                        },
                        placeholder = { Text("Search...") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                        //keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),

                        shape = RoundedCornerShape(20.dp),

                        leadingIcon = { Icon(Icons.Default.Search, null) }
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { toHome.invoke() }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                )

            LazyColumn(modifier = Modifier.padding(16.dp)) {
                itemsIndexed(list, key = { _, item -> item.id }) { _, item ->
                    SearchResultItem(dairy = item) {
                        toDetail.invoke(item.id)
                    }
                }
            }
        }
    }
}

    @Composable
    fun SearchResultItem(
        dairy: Diary,
        toDetail: (id: String) -> Unit,
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .clickable { toDetail.invoke(dairy.id) },
                colors = CardDefaults.cardColors(containerColor = Color.Gray)
        ) {
            Text(
                text = dairy.title,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Start
            )
        }
    }


@Preview
@Composable
fun S() {
    MyDiaryTheme {
        SearchScreen(list = diaryList, toHome = { /*TODO*/ }, toDetail = {}, search = {} )
    }
}