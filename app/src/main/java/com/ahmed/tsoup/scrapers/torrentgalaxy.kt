package com.ahmed.tsoup.scrapers


import com.ahmed.tsoup.TorrentVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getTorrentGalaxy(url: String): Flow<TorrentVM> = flow {
    try {
        val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get()
        println(url)
        var links = doc.select("div.tgxtablerow")
        println(links)
        if (links.isEmpty() && doc.text().isNotEmpty())
            emit(TorrentVM("None", "", 0, 0, "", "", ""))


        links.forEach { link ->
            if (link.text().isNotEmpty()) {
                val health = link.select("td")[3].text().split("/")

                val currentItem = TorrentVM(
                    title = link.select("a[href^=/torrent/]").text(),
                    size = link.select("td")[2].text(),
                    seeds = health.first().slice(8..health.first().length - 1).toInt(),
                    leeches = health[1].slice(0..health[1].length - 2).toInt(),
                    uploader = "TorrentGalaxy",
                    magnet = link.select("a[href^=magnet:]").attr("href").toString(),
                    date = link.select("td")[4].text().slice(0..14)
                )
                emit(currentItem)
            }
        }

    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "", 0, 0, "", "", ""))
    }
}.flowOn(Dispatchers.IO)