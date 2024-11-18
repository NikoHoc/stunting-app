package com.dicoding.stunting.ui.notifications

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.stunting.data.remote.DataRepository

class NotificationsViewModel(private val repository: DataRepository) : ViewModel()  {

    private val _text = MutableLiveData<String>().apply {
        value = "This is notifications Fragment"
    }
    val text: LiveData<String> = _text


}