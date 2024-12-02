package com.ahmed.tsoup

import android.content.Context
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.ahmed.tsoup.ui.theme.DarkSlateGray
import com.ahmed.tsoup.ui.theme.LightBlue
import com.ahmed.tsoup.ui.theme.LightGreen
import com.ahmed.tsoup.ui.theme.RoyalBlue
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
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        SetSorter(Modifier)
                        Spacer(Modifier.height(64.dp))
                        EnableHint()
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
    var listItems = loadAddress(
        context.getSharedPreferences(
            "app_preferences", Context.MODE_PRIVATE
        )
    ).toMutableStateList()
    Column(
        modifier.padding(16.dp), verticalArrangement = Arrangement.Center
    ) {
        val firstHalf = listItems.slice(0..2)
        val secondHalf = listItems.slice(3..5)
        CustomRow(
            firstHalf, modifier.fillMaxWidth()
        ) { item, position ->
            listItems[position] = item
            saveAddress(
                listItems.toList(),
                context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            )
        }
        CustomRow(
            secondHalf, Modifier.fillMaxWidth()
        ) { item, position ->
            listItems[position + 3] = item
            saveAddress(
                listItems.toList(),
                context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
            )
        }
    }
}

@Composable
fun CustomRow(
    listItems: List<DomainItem>,
    modifier: Modifier = Modifier,
    saveAddress: (domain: DomainItem, position: Int) -> Unit
) {
    val context = LocalContext.current
    LazyRow(modifier, horizontalArrangement = Arrangement.SpaceBetween) {
        items(listItems) { item ->
            val name = item.domain
            val enabled = item.enabled
            val size = item.querySize

            val boxColor = if (enabled) LightGreen else LightBlue
            val textColor = if (enabled) DarkSlateGray else RoyalBlue
            val borderColor = if (enabled) Color.White else Color.Black

            Box(modifier = Modifier
                .width(LocalConfiguration.current.screenWidthDp.dp / 3.3f)
                .height(125.dp)
                .background(
                    color = boxColor,
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                )
                .clickable(true) {
                    Toast
                        .makeText(
                            context, if (enabled) "disabled ${
                                name
                                    .slice(8..name.length - 1)
                                    .split(".")[0].uppercase()
                            }" else "enabled ${
                                name
                                    .slice(8..name.length - 1)
                                    .split(".")[0].uppercase()
                            }", Toast.LENGTH_SHORT
                        )
                        .show()
                    saveAddress(
                        DomainItem(name, !enabled, size), listItems.indexOf(item)
                    )
                }
                .border(
                    1.dp,
                    borderColor,
                    androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                )
                .padding(8.dp), contentAlignment = Alignment.Center) {
                Text(
                    name.slice(8..name.length - 1).split(".")[0].uppercase(),
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Box(
                    Modifier
                        .align(Alignment.BottomEnd)
                        .width(40.dp)
                        .height(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        size.toString(), color = textColor
                    )
                }
            }
        }
    }
}

@Composable
fun EnableHint() {
    Row(horizontalArrangement = Arrangement.SpaceEvenly, modifier = Modifier.fillMaxWidth()) {
        Text("Enabled", color = LightGreen)
        Text("Disabled", color = LightBlue)
    }
}


@Composable
fun SetSorter(modifier: Modifier) {
    val context = LocalContext.current
    val prefs = context.getSharedPreferences("app_preferences", Context.MODE_PRIVATE)
    val expanded = remember { mutableStateOf(false) }
    val text = remember { mutableStateOf(prefs.getString("sorter", "Seeds")) }
    Column {
        TextButton(
            onClick = { expanded.value = !expanded.value },
        ) { Text("Sort by : ${text.value}") }

        DropdownMenu(
            expanded.value, onDismissRequest = { expanded.value = !expanded.value }, modifier
        ) {
            DropdownMenuItem({ Text("Seeds") }, {
                prefs.edit().putString("sorter", "Seeds").apply()
                expanded.value = !expanded.value
                text.value = "Seeds"
            })
            DropdownMenuItem({ Text("Leeches") }, {
                prefs.edit().putString("sorter", "Leeches").apply()
                text.value = "Leeches"
                expanded.value = !expanded.value
            })
            DropdownMenuItem({
                Row {
                    Text("Size desc")
                    Icon(imageVector = Icons.Filled.KeyboardArrowDown, null)
                }
            }, onClick = {
                prefs.edit().putString("sorter", "Sizedesc").apply()
                text.value = "Size desc"
            })
            DropdownMenuItem({
                Row {
                    Text("Size asc")
                    Icon(imageVector = Icons.Filled.KeyboardArrowUp, null)
                }
            }, onClick = {
                prefs.edit().putString("sorter", "Sizeasc").apply()
                text.value = "Size asc"
            })
        }
    }
}
