package com.ahmed.tsoup

import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ahmed.tsoup.scrapers.get1337x
import com.ahmed.tsoup.scrapers.getBitSearch
import com.ahmed.tsoup.scrapers.getCloudTorrents
import com.ahmed.tsoup.scrapers.getKnaben
import com.ahmed.tsoup.scrapers.getTorrentGalaxy
import com.ahmed.tsoup.scrapers.getTorrentQuest
import kotlinx.coroutines.launch

data class TorrentVM(
    var title: String,
    var size: String,
    var seeds: String,
    var leeches: String,
    var uploader: String,
    var magnet: String,
    var date: String
)

class TorrentItems : ViewModel() {
    private val _torrentItems = mutableStateListOf<TorrentVM>()
    val torrentItems: SnapshotStateList<TorrentVM> = _torrentItems
    val size = mutableIntStateOf(_torrentItems.size)


    fun loadItems(domain: String, query: String, domainSize: Int) {
        viewModelScope.launch {
            launch {
                val result = formatURL(domain, query, 1)
                when (domain) {
                    "https://1337x.to" -> get1337x(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentgalaxy.to" -> getTorrentGalaxy(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentquest.com" -> getTorrentQuest(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://knaben.eu" -> getKnaben(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://cloudtorrents.com" -> getCloudTorrents(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://bitsearch.to" -> getBitSearch(result).collect { item ->
                        _torrentItems.add(item)
                    }
                }
            }.join()
            launch {
                val result = formatURL(domain, query, 2)
                when (domain) {
                    "https://1337x.to" -> get1337x(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentgalaxy.to" -> getTorrentGalaxy(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentquest.com" -> getTorrentQuest(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://knaben.eu" -> getKnaben(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://cloudtorrents.com" -> getCloudTorrents(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://bitsearch.to" -> getBitSearch(result).collect { item ->
                        _torrentItems.add(item)
                    }
                }
            }.join()
            launch {
                val result = formatURL(domain, query, 3)
                when (domain) {
                    "https://1337x.to" -> get1337x(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentgalaxy.to" -> getTorrentGalaxy(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentquest.com" -> getTorrentQuest(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://knaben.eu" -> getKnaben(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://cloudtorrents.com" -> getCloudTorrents(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://bitsearch.to" -> getBitSearch(result).collect { item ->
                        _torrentItems.add(item)
                    }
                }
            }.join()
            launch {
                val result = formatURL(domain, query, 4)
                when (domain) {
                    "https://1337x.to" -> get1337x(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentgalaxy.to" -> getTorrentGalaxy(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://torrentquest.com" -> getTorrentQuest(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://knaben.eu" -> getKnaben(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://cloudtorrents.com" -> getCloudTorrents(result).collect { item ->
                        _torrentItems.add(item)
                    }

                    "https://bitsearch.to" -> getBitSearch(result).collect { item ->
                        _torrentItems.add(item)
                    }
                }
            }.join()
        }
    }
}

fun formatURL(domain: String, query: String, page: Int): String {


    var query = when (domain) {

        "https://1337x.to" -> "$domain/search/$query/$page/"

        "https://torrentgalaxy.to" -> "$domain/torrents.php?search=$query&sort=id&page=${page - 1}&sort=seeders&order=desc"

        "https://torrentquest.com" -> "$domain/p/$query/se/desc/$page/"

        "https://knaben.eu" -> "$domain/search/$query/0/$page/seeders"

        "https://cloudtorrents.com" -> "$domain/search?offset=${(page - 1) * 50}&query=$query&ordering=-se"

        "https://bitsearch.to" -> "$domain/search?q=$query&page=$page&sort=seeders"
        else -> ""
    }
    return query
}

