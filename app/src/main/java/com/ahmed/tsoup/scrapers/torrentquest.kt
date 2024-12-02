package com.ahmed.tsoup.scrapers


import com.ahmed.tsoup.TorrentVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document

fun getTorrentQuest(url: String): Flow<TorrentVM> = flow {
    try {
        println(url)
        val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000).get()
        var links = doc.select("tr")
        println("running scraper")
        if (doc.select("td").isEmpty() && doc.text().isNotEmpty()) emit(
            TorrentVM(
                "None", "", 0, 0, "6", "", ""
            )
        )
        if (links.size > 2)
            links.removeAt(links.size - 2)
        else
            return@flow
        links.forEach { link ->
            if (link.select("td").text().isNotEmpty()) {
                val currentItem = TorrentVM(
                    title = link.select("td").text(),
                    size = link.select("td")[5].text(),
                    seeds = link.select("td")[6].text().toInt(),
                    leeches = link.select("td")[7].text().toInt(),
                    uploader = "TorrentQuest",
                    magnet = link.select("a[href^=magnet]").attr("href"),
                    date = link.select("td")[2].text()
                )
                emit(currentItem)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "", 0, 0, "6", "", ""))

    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "", 0, 0, "6", "", ""))
    }
}.flowOn(Dispatchers.IO)