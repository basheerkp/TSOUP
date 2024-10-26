package com.ahmed.tsoup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

data class TorrentVM(
    var title: String,
    var size: String,
    var seeds: Int,
    var leeches: Int,
    var uploader: String,
    var magnet: String,
    var date: String
)

class TorrentItems : ViewModel() {
    private val _torrentItems = MutableLiveData<List<TorrentVM>>(emptyList())
    val torrentItems: LiveData<List<TorrentVM>> = _torrentItems


    fun loadItems(url: String) {
        viewModelScope.launch {
            try {
                val data = getResults(url)
                _torrentItems.postValue(data)
            } catch (_: Exception) {
            }
        }

    }

}