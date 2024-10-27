package com.ahmed.tsoup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.integerResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmed.tsoup.ui.theme.TSOUPTheme

class SearchResults : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 35.dp)
                ) { innerPadding ->
                    val config = LocalConfiguration.current
                    val width = config.screenWidthDp
                    val height = config.screenHeightDp


                    Results(
                        modifier = Modifier.padding(innerPadding),
                        "https://1337x.to/search/${intent.getStringExtra("url").toString()}/1/",
                        height = height.dp,
                        width.dp
                    )
                }
            }
        }
    }
}


@Composable
fun Results(
    modifier: Modifier,
    url: String,
    height: Dp,
    width: Dp,
    viewModel: TorrentItems = TorrentItems(),

    ) {

    LaunchedEffect(Unit) {
        viewModel.loadItems(url)
    }
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
        } else if (items[0].title == "TimeOUT") {
            val context = LocalContext.current
            val intent = Intent(context, MainActivity::class.java)
            Toast.makeText(
                context,
                "Slow Internet Connection or VPN/DNS not Enabled ${integerResource(R.integer.device_height)}",
                Toast.LENGTH_SHORT
            ).show()
            context.startActivity(intent)

        } else Column(
            Modifier.width(width), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val boxWidth = width * 0.9f
            val textWidth = width * 0.7f
            val iconWidth = width * 0.2f
            val boxPadding = height * 0.03f
            Text(
                "Showing ${items.size} results for ${url.slice(24..url.length - 4)}",
                fontSize = 20.sp,
                fontStyle = FontStyle.Italic
            )
            LazyColumn(
                Modifier
                    .padding(vertical = boxPadding)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                items(items) { item ->
                    val context = LocalContext.current
                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                shape = MaterialTheme.shapes.extraLarge,
                                color = MaterialTheme.colorScheme.surfaceBright
                            )
                            .padding(20.dp, 15.dp)
                            .width(boxWidth)
                            .wrapContentSize(Alignment.Center, false),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Absolute.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(verticalArrangement = Arrangement.Center) {
                                Text(item.title, Modifier.width(textWidth))
                                Spacer(Modifier.height(15.dp))
                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Text(text = "Seeds: ${item.seeds}", color = Color.Green)
                                    Spacer(Modifier.width(20.dp))
                                    Text("Leechers: ${item.leeches}", color = Color.Red)
                                }
                                Spacer(Modifier.height(15.dp))
                                Row {
                                    Text("Date: ${item.date}")
                                    Spacer(Modifier.width(20.dp))
                                    Text(
                                        "uploader: ${item.uploader}", color = Color.Blue
                                    )
                                }
                            }
                            Column {
                                IconButton(onClick = {
                                    try {
                                        val intent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(item.magnet))
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "YOU DON'T HAVE A TORRENT CLIENT INSTALLED",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, Modifier.padding(top = 50.dp)) {
                                    Icon(
                                        painterResource(R.drawable.ic_launcher_foreground),
                                        "downloader",
                                        Modifier
                                            .width(iconWidth)
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
}
