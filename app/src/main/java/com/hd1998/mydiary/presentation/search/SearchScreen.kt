import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hd1998.mydiary.dairyList
import com.hd1998.mydiary.domain.model.Dairy
import com.hd1998.mydiary.presentation.theme.MyDiaryTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(list: List<Dairy> , toHome :() -> Unit, toDetail: (id : String) -> Unit,
                 search: (query: String) -> Unit) {
    var query by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = {
                TextField(
                    value = query,
                    onValueChange = { query = it
                                    search.invoke(query.toString())},
                    placeholder = { Text("Search...") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Text),
                )
            },
            navigationIcon = {
                IconButton(onClick = { toHome.invoke()}) {
                    Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                }
            },

        )

        LazyColumn(modifier = Modifier.padding(16.dp)) {
           itemsIndexed(list, key = {_, item -> item.id}){_ , item ->
               SearchResultItem(dairy = item){
                   toDetail.invoke(item.id)
               }
           }
        }
    }
}

@Composable
fun SearchResultItem(
    dairy: Dairy,
    toDetail: (id: String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clickable { toDetail.invoke(dairy.id) }
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


    }

    fun searchList(list: MutableState<List<Dairy>>, query: String) {
        list.value = list.value.filter { it.title.contains(query) }
    }
}