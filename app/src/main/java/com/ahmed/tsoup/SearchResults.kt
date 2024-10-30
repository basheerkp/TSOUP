package com.ahmed.tsoup

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ahmed.tsoup.ui.theme.LightBlue
import com.ahmed.tsoup.ui.theme.TSOUPTheme

class SearchResults : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                val page = remember { mutableStateOf(false) }
                Scaffold(modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 35.dp),
                    floatingActionButton = {
                        PageSwitcher(page2 = page.value, switch = { page.value = !page.value })

                    }) { innerPadding ->

                    val config = LocalConfiguration.current
                    val width = config.screenWidthDp
                    val height = config.screenHeightDp
                    if (!page.value) Results(
                        modifier = Modifier.padding(innerPadding),
                        "https://1337x.to/search/${intent.getStringExtra("url").toString()}/1/",
                        height = height.dp,
                        width.dp, page = page.value
                    )
                    else Results(
                        modifier = Modifier.padding(innerPadding),
                        "https://1337x.to/search/${intent.getStringExtra("url").toString()}/2/",
                        height = height.dp,
                        width.dp, page = page.value
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
    page: Boolean
) {
    if (viewModel.torrentItems.isEmpty()) {
        LaunchedEffect(Unit) {
            viewModel.loadItems(url)
        }
    }

    val items = viewModel.torrentItems
    val isListEmpty = items.isEmpty()


    Column(
        modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (isListEmpty) {
            Row {
                Text("Loading ${if (!page) "page 1" else "page 2"} for ${url.slice(24..url.length - 4)}")
                Icon(
                    imageVector = Icons.Outlined.Refresh, null, Modifier.size(25.dp)
                )
            }
        } else if (items[0].title == "TimeOUT") {
            val context = LocalContext.current

            Toast.makeText(
                context, "VPN/DNS not Enabled", Toast.LENGTH_SHORT
            ).show()

            Text("ENABLE DNS/VPN")

            TextButton(onClick = {
                val intent = Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=com.cloudflare.onedotonedotonedotone")
                )
                Toast.makeText(
                    context, "Opening warp on PlayStore", Toast.LENGTH_SHORT
                ).show()
                context.startActivity(intent)
            }) {
                Text("Download WARP")
            }
        } else if (items[0].title == "None") {
            val context = LocalContext.current
            Toast.makeText(
                context, "No Items Found", Toast.LENGTH_SHORT
            ).show()

            Text("THERE WERE NO RESULTS FOR THE QUERY")

        } else Column(
            Modifier.width(width), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val boxWidth = width * 0.88f
            val textWidth = width * 0.7f
            val iconWidth = width * 0.2f
            val boxPadding = height * 0.01f

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
                items(items.size) { item ->
                    val context = LocalContext.current

                    Box(
                        modifier = Modifier
                            .border(
                                width = 1.dp,
                                shape = MaterialTheme.shapes.extraLarge,
                                color = MaterialTheme.colorScheme.surfaceBright
                            )
                            .padding(10.dp, 5.dp)
                            .width(boxWidth),
                        contentAlignment = Alignment.Center
                    ) {

                        Row(
                            horizontalArrangement = Arrangement.Absolute.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column(verticalArrangement = Arrangement.Center) {

                                Text(items[item].title, Modifier.width(textWidth))

                                Spacer(Modifier.height(15.dp))

                                Row(
                                    horizontalArrangement = Arrangement.Start,
                                ) {
                                    Text(text = "Seeds: ${items[item].seeds}", color = Color.Green)

                                    Spacer(Modifier.width(20.dp))

                                    Text("Leeches: ${items[item].leeches}", color = Color.Red)
                                }
                                Spacer(Modifier.height(15.dp))

                                Row {
                                    Text(items[item].date, color = Color(0xFF5A9BD8))

                                    Text(" BY ", color = Color.White)

                                    Text(items[item].uploader, color = Color(0xFF5A9BD8))
                                }
                            }
                            Column(Modifier.fillMaxHeight(), Arrangement.Center) {

                                IconButton(onClick = {
                                    try {
                                        val intent = Intent(
                                            Intent.ACTION_VIEW, Uri.parse(items[item].magnet)
                                        )
                                        context.startActivity(intent)
                                    } catch (e: Exception) {
                                        Toast.makeText(
                                            context,
                                            "YOU DON'T HAVE A TORRENT CLIENT INSTALLED",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }, Modifier.padding()) {

                                    Icon(
                                        painterResource(R.drawable.ic_launcher_foreground),
                                        "downloader",
                                        Modifier
                                            .width(iconWidth)
                                            .rotate(-45f)
                                    )
                                }

                                Text(
                                    text = items[item].size.slice(0..items[item].size.length - 2),
                                    color = Color.Green
                                )
                            }
                        }
                    }
                    Spacer(Modifier.padding(vertical = height * 0.01f))
                }
            }
        }
    }
}


@Composable
fun PageSwitcher(page2: Boolean = false, switch: () -> Unit) {
    IconButton(onClick = switch, Modifier.background(LightBlue, MaterialTheme.shapes.extraLarge)) {
        if (!page2) Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "next"
        )
        else Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
    }
}