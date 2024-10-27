package com.ahmed.tsoup

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.ahmed.tsoup.ui.theme.TSOUPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Results(modifier = Modifier.padding(innerPadding), url ="https://1337x.to/search/ghost/1/")
                    Column {
                        TSoup()
                        SearchBar(
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var url = remember { mutableStateOf("") }
    Column(
        modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(value = url.value, onValueChange = { url.value = it }, singleLine = true)
        Spacer(Modifier.height(25.dp))
        TextButton(
            onClick = {
                val intent = Intent(context, SearchResults::class.java)
                url.value.replace(" ", "+")
                intent.putExtra("url", url.value)
                startActivity(context, intent, null)
            },
            Modifier
                .background(shape = MaterialTheme.shapes.extraLarge, color = Color.Gray)
                .defaultMinSize(155.dp, 45.dp),
            enabled = url.value.length > 2
        ) { Text("Search") }
    }
}


@Composable
fun TSoup(modifier: Modifier = Modifier) {
    Row(
        modifier
            .fillMaxWidth()
            .height(250.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            "TSOUP", fontSize = 45.sp, modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
        )
    }
}