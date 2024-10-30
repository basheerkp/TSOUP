package com.ahmed.tsoup

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import java.io.IOException

suspend fun getResults(url: String): List<TorrentVM> {
    val returnList = mutableListOf<TorrentVM>()
    return withContext(Dispatchers.IO) {
        val job1 = CoroutineScope(Dispatchers.IO).launch {
            try {
                val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(15000).get()
                var links = doc.select("a[href^=/torrent]")
                println("running scraper")
                links.forEach { link ->
                    val currentItem = TorrentVM(
                        title = "",
                        size = "",
                        seeds = 0,
                        leeches = 0,
                        uploader = "",
                        magnet = "",
                        date = ""
                    )
                    async(Dispatchers.IO) {
                        currentItem.title = link.text()
                        val linkUrl = link.absUrl("href")
                        try {
                            async(Dispatchers.IO) {
                                val detailsPage =
                                    Jsoup.connect(linkUrl).userAgent("Mozilla/5.0").timeout(15000)
                                        .get()
                                currentItem.magnet =
                                    detailsPage.select("a[href*=magnet]").first()?.absUrl("href")
                                        .toString()
                                val listItems = detailsPage.select("li")
                                for (item: Element in listItems) {
                                    if (item.select("strong").text().contains("Total size")) {
                                        currentItem.size = item.select("span").text()
                                    }
                                    if (item.select("strong").text().contains("Seeders")) {
                                        currentItem.seeds = item.select("span").text().toInt()
                                    }
                                    if (item.select("strong").text().contains("Leechers")) {
                                        currentItem.leeches = item.select("span").text().toInt()
                                    }
                                    if (item.select("strong").text().contains("Uploaded By")) {
                                        currentItem.uploader = item.select("span").text()
                                    }
                                    if (item.select("strong").text().contains("Date uploaded")) {
                                        currentItem.date = item.select("span").text()
                                    }
                                }
                                withContext(Dispatchers.IO) {}
                            }
                        } catch (e: IOException) {
                            e.printStackTrace()
                            returnList.add(TorrentVM(title = "TimeOUT", "", 0, 0, "", "", ""))
                        }
                    }
                    returnList.add(currentItem)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                returnList.add(TorrentVM(title = "TimeOUT", "", 0, 0, "", "", ""))
            }
        }
        job1.join()
        println("scraping done")
        if (returnList.isEmpty()) {
            returnList.add(TorrentVM(title = "None", "", 0, 0, "", "", ""))
        }
        returnList

    }

}