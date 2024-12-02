package com.ahmed.tsoup.scrapers


import com.ahmed.tsoup.TorrentVM
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManager
import javax.net.ssl.X509TrustManager

fun getCloudTorrents(url: String): Flow<TorrentVM> = flow {
    try {
        val trustAllCertificates = object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? = null
            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        }

        val sslContext = SSLContext.getInstance("TLS")
        sslContext.init(
            null, arrayOf<TrustManager>(trustAllCertificates), java.security.SecureRandom()
        )
        val doc: Document = Jsoup.connect(url).userAgent("Mozilla/5.0").timeout(5000)
            .sslSocketFactory(sslContext.socketFactory).get()
        println(url)
        var links = doc.select("tr")
        links.removeAt(0)
        if (links.first()?.text()
                ?.contains("Your query returned no torrents.") == true && doc.text().isNotEmpty()
        ) emit(TorrentVM("None", "", 0, 0, "4", "", ""))

        links.forEach { link ->
            val currentItem = TorrentVM(
                title = link.select("td")[0].text(),
                size = link.select("td")[1].text(),
                seeds = link.select("td")[3].text().toInt(),
                leeches = link.select("td")[4].text().toInt(),
                uploader = "CloudTorrents",
                magnet = link.select("a[href^=magnet]").attr("href"),
                date = link.select("td")[2].text()
            )
            emit(currentItem)
        }

    } catch (e: Exception) {
        e.printStackTrace()
        emit(TorrentVM("None", "", 0, 0, "4", "", ""))
    }
}.flowOn(Dispatchers.IO)