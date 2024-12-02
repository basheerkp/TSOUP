package com.ahmed.tsoup

import android.annotation.SuppressLint
import android.app.appsearch.SearchResults
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
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
                val context = LocalContext.current
                val prefs = context.getSharedPreferences("app_preferences", MODE_PRIVATE)
                if (prefs.getBoolean("first_run", true)) {
                    saveAddress(
                        listOf(
                            DomainItem("https://1337x.to", true, 20),
                            DomainItem("https://bitsearch.to", true, 20),
                            DomainItem("https://cloudtorrents.com", true, 50),
                            DomainItem("https://knaben.eu", true, 50),
                            DomainItem("https://torrentgalaxy.to", true, 50),
                            DomainItem("https://torrentquest.com", true, 40),
                        ), context.getSharedPreferences("app_preferences", MODE_PRIVATE)
                    )
                    prefs.edit().putBoolean("first_run", false).apply()
                }
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        horizontalAlignment = AbsoluteAlignment.Right,
                        modifier = Modifier.padding(innerPadding),
                        verticalArrangement = Arrangement.SpaceEvenly
                    ) {
                        IconButton(
                            onClick = {
                                val intent = Intent(context, Settings::class.java)
                                context.startActivity(intent)
                            }, modifier = Modifier.size(45.dp)
                        ) {
                            Icon(
                                Icons.AutoMirrored.Filled.List,
                                null,
                                modifier = Modifier.size(75.dp)
                            )
                        }
                        val urlOld = intent.getStringExtra("url")
                        TSoup()
                        Row(
                            Modifier.fillMaxWidth(),
                        ) {
                            SearchBar(
                                modifier = Modifier.padding(innerPadding),
                                url = if (urlOld == null) "" else urlOld
                            )
                        }
                        Spacer(Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@SuppressLint("NewApi")
@Composable
fun SearchBar(modifier: Modifier = Modifier, url: String) {
    val context = LocalContext.current
    var url = remember { mutableStateOf(url) }
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            TextField(value = url.value, onValueChange = { url.value = it }, singleLine = true)
            SetSorter(Modifier.wrapContentWidth())
        }
        Spacer(Modifier.height(25.dp))
        TextButton(
            onClick = {
                val intent = Intent(context, SearchResults::class.java)
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
        Modifier
            .fillMaxWidth()
            .height(250.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("T", color = Green, fontSize = 45.sp)
        Text(" - SOUP", fontSize = 45.sp)
    }
}

@Composable
fun SetSorter(modifier: Modifier) {
    val expanded = remember { mutableStateOf(true) }
    DropdownMenu(
        expanded.value,
        onDismissRequest = { expanded.value = !expanded.value }, modifier
    ) {
        DropdownMenuItem({ Text("Seeds") }, { expanded.value = !expanded.value })
        DropdownMenuItem({ Text("Leeches") }, { expanded.value = !expanded.value })
        DropdownMenuItem({ Text("Size") }, { expanded.value = !expanded.value })
    }
}