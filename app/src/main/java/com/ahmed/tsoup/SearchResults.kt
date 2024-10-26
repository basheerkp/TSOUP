package com.ahmed.tsoup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ahmed.tsoup.ui.theme.TSOUPTheme

class SearchResults : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Results(
                        modifier = Modifier.padding(innerPadding),
                        "https://1337x.to/search/${intent.getStringExtra("url").toString()}/1/"
                    )
                }
            }
        }
    }
}


@Composable
fun Results(modifier: Modifier, url: String, viewModel: TorrentItems = TorrentItems()) {
    LaunchedEffect(Unit) { viewModel.loadItems(url) }
    val items by viewModel.torrentItems.observeAsState(emptyList())

    val isListEmpty = items.isEmpty()

    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isListEmpty) {
            Row {
                Text("Loading results for ${url.slice(24..url.length - 4)}")
                Icon(
                    imageVector = Icons.Outlined.Refresh, null, Modifier.size(25.dp)
                )
            }
        } else LazyColumn(Modifier.padding(top = 25.dp)) {
            items(items) { item ->
                val context = LocalContext.current
                Box(
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            shape = MaterialTheme.shapes.extraLarge,
                            color = MaterialTheme.colorScheme.surfaceBright
                        )
                        .padding(40.dp, 15.dp)
                        .wrapContentSize(Alignment.Center, false),
                    contentAlignment = Alignment.Center
                ) {
                    Row(horizontalArrangement = Arrangement.Absolute.Center) {
                        Column(verticalArrangement = Arrangement.Center) {
                            Text(item.title, Modifier.width(425.dp))
                            Spacer(Modifier.height(15.dp))
                            Row(
                                Modifier.width(425.dp),
                                horizontalArrangement = Arrangement.Start,
                            ) {
                                Text(text = "Seeds: ${item.seeds}", color = Color.Green)
                                Spacer(Modifier.width(40.dp))
                                Text("Leechers: ${item.leeches}", color = Color.Red)
                            }
                            Spacer(Modifier.height(15.dp))
                            Row {
                                Text("Date : ${item.date}")
                                Spacer(Modifier.width(40.dp))
                                Text(
                                    "uploaded by : ${item.uploader}", color = Color.Blue
                                )
                            }
                        }
                        Column {
                            IconButton(onClick = {
                                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(item.magnet))
                                context.startActivity(intent)
                            }, Modifier.padding(top = 50.dp)) {
                                Icon(
                                    painterResource(R.drawable.ic_launcher_foreground),
                                    "downloader",
                                    Modifier
                                        .size(90.dp)
                                        .rotate(-45f)
                                )
                            }
                            Text(
                                text = item.size.slice(0..item.size.length - 2),
                                color = Color.Green
                            )
                        }
                    }
                }
            }
        }
    }
}
