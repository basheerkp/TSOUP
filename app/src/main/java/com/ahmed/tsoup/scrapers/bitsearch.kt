package com.ahmed.tsoup.scrapers


import com.ahmed.tsoup.TorrentVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getBitSearch(url: String): Flow<TorrentVM> = flow {
    try {
        val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(15000).get()

        println(url)

        var links = doc.select("li")

        if (doc.select("li").isEmpty() && doc.text().isNotEmpty()) emit(
            TorrentVM(
                "None", "", "0", "0", "", "", ""
            )
        )
        val newLinks = links.subList(3, links.lastIndex + 1)

        newLinks.forEach { link ->
            val stats = link.select(".stats")
            val currentItem = TorrentVM(
                title = link.select("h5").text(),
                size = (stats.select("div:has(img[alt=Size])").text().split(" ")
                    .getOrNull(1) + stats.select("div:has(img[alt=Size])").text().split(" ")
                    .getOrNull(2)),
                seeds = stats.select("div:has(img[alt=Seeder])").text().split(" ").getOrNull(3)
                    ?: "0",
                leeches = stats.select("div:has(img[alt=Seeder])").text().split(" ").getOrNull(4)
                    ?: "0",
                uploader = "Unknown",
                magnet = link.select("a[href*=magnet]").first()?.absUrl("href").toString(),
                date = (stats.select("div:has(img[alt=Seeder])").text().split(" ")
                    .getOrNull(5) + " "
                        + stats.select("div:has(img[alt=Seeder])").text().split(" ")
                    .getOrNull(6) + " " + stats.select("div:has(img[alt=Seeder])").text()
                    .split(" ")
                    .getOrNull(7))
            )
            emit(currentItem)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "", "0", "0", "", "", ""))
    }
}.flowOn(Dispatchers.IO)