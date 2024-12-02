package com.ahmed.tsoup.scrapers


import com.ahmed.tsoup.TorrentVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getKnaben(url: String): Flow<TorrentVM> = flow {
    try {
        println(url)
        val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get()
        println(url)
        var links = doc.select("tr")
        links.removeAt(0)
        println("running scraper")
        if (doc.select("td").isEmpty() && doc.text().isNotEmpty()) emit(
            TorrentVM(
                "None", "", 0, 0, "3", "", ""
            )
        )

        links.forEach { link ->
            val currentItem = TorrentVM(
                title = link.select("a").text(),
                size = link.select("td")[2].text(),
                seeds = link.select("td")[4].text().toInt(),
                leeches = link.select("td")[5].text().toInt(),
                uploader = "Knaben",
                magnet = link.select("a[href^=magnet]").attr("href"),
                date = link.select("td")[3].text()
            )
            emit(currentItem)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "3", 0, 0, "", "", ""))
    }
}.flowOn(Dispatchers.IO)