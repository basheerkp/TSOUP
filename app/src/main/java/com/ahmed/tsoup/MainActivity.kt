package com.ahmed.tsoup

import android.content.Intent
import android.net.Uri
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
import com.ahmed.tsoup.ui.theme.Green
import com.ahmed.tsoup.ui.theme.TSOUPTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {
                        val urlOld = intent.getStringExtra("url")
                        TSoup()
                        SearchBar(
                            modifier = Modifier.padding(innerPadding),
                            url = if (urlOld== null) "" else urlOld
                        )

                        UseClient()
                        UseVPN()
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier, url: String) {
    val context = LocalContext.current
    var url = remember { mutableStateOf(url) }
    Column(
        Modifier.fillMaxWidth(),
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
        Modifier
            .fillMaxWidth()
            .height(350.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text("T", color = Green, fontSize = 45.sp)
        Text(" - SOUP", fontSize = 45.sp)
    }
}

@Composable
fun UseVPN() {
    val context = LocalContext.current
    Row(
        Modifier
            .fillMaxWidth()
            .height(200.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Use VPN or DNS for searching ")
        TextButton(onClick = {
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.cloudflare.onedotonedotonedotone&hl=en_IN")
            )
            startActivity(context, intent, null)
        }) {
            Text(" ( WARP ) ")
        }
    }
}

@Composable
fun UseClient() {
    val context = LocalContext.current
    Column(
        Modifier
            .fillMaxWidth()
            .height(200.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Use one of these clients")
        Row {
            TextButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=idm.internet.download.manager&hl=en_IN")
                )
                startActivity(context, intent, null)
            }) {
                Text("  1DM  ")
            }
            TextButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=org.proninyaroslav.libretorrent&hl=en_IN")
                )
                startActivity(context, intent, null)
            }) {
                Text("  LibTorrent  ")
            }
        }
    }
}