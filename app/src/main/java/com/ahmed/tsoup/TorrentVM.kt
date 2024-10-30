package com.ahmed.tsoup

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
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
    private val _torrentItems = mutableStateListOf<TorrentVM>()
    val torrentItems: SnapshotStateList<TorrentVM> = _torrentItems


    fun loadItems(url: String) {
        viewModelScope.launch {
            try {
                 getResults(url).forEach{
                     _torrentItems.add(it)
                 }

            } catch (_: Exception) {
            }
        }

    }

}