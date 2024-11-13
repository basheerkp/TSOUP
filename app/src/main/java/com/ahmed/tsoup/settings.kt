package com.ahmed.tsoup

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ahmed.tsoup.ui.theme.LightBlue
import com.ahmed.tsoup.ui.theme.LightGreen
import com.ahmed.tsoup.ui.theme.TSOUPTheme
import kotlin.text.slice


class Settings : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TSOUPTheme {
                Scaffold() { innerPadding ->
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
                    ) {
                        UseVPN()
                        SetDomain(modifier = Modifier.padding(innerPadding))

                    }
                }
            }
        }
    }
}

@Composable
fun SetDomain(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var listItems =
        loadAddress(context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE))
    var default = getDefaultAddress(
        context.getSharedPreferences(
            "app_preferences", Context.MODE_PRIVATE
        )
    )
    Column(
        modifier.padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        CustomRow(listItems.slice(0..2), default!!, modifier.fillMaxWidth())
        CustomRow(listItems.slice(3..5), default, Modifier.fillMaxWidth())
    }
}

@Composable
fun UseVPN() {
    Column(
        Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Use VPN or DNS for blocked domains (domains in blue)")
    }
}

@Composable
fun CustomRow(listItems: List<String>, default: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    LazyRow(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        items(listItems) { domain ->
            val size = when (domain) {
                "https://1337x.to" -> 20
                "https://torrentgalaxy.to" -> 50
                "https://torrentquest.com" -> 40
                "https://knaben.eu" -> 50
                "https://cloudtorrents.com" -> 50
                "https://bitsearch.to" -> 20
                else -> 0
            }
            val color = when (domain) {
                "https://1337x.to" -> LightBlue
                "https://torrentgalaxy.to" -> LightBlue
                "https://torrentquest.com" -> LightGreen
                "https://knaben.eu" -> LightBlue
                "https://cloudtorrents.com" -> LightGreen
                "https://bitsearch.to" -> LightGreen
                else -> Color.Transparent
            }
            val isDefault = domain == default
            Box(modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 3.3f)
                .height(125.dp)
                .background(
                    color = if (isDefault) Color.Transparent else color,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                )
                .clickable(!isDefault) {
                    setDefaultAddress(
                        domain, context.getSharedPreferences(
                            "app_preferences", Context.MODE_PRIVATE
                        )
                    )
                    Toast
                        .makeText(
                            context, "default Domain is ${
                                domain
                                    .slice(8..domain.length - 1)
                                    .split(".")[0].uppercase()
                            }", Toast.LENGTH_SHORT
                        )
                        .show()
                    context.startActivity(
                        Intent(
                            context, MainActivity::class.java
                        ).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    )
                }
                .border(
                    1.dp, Color.White, androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                ), contentAlignment = Alignment.Center) {
                Text(
                    domain.slice(8..domain.length - 1).split(".")[0].uppercase(),
                    color = if (isDefault) Color.White else Color.Black
                )
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .border(
                            1.dp, Color.White, MaterialTheme.shapes.large
                        )
                        .width(40.dp)
                        .height(40.dp)
                        .background(Color.White, MaterialTheme.shapes.large),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        size.toString(), color = Color.Black
                    )
                }
            }
        }
    }
}