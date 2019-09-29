package com.karntrehan.talko.messages.landing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MessagesVMF(private val repo: MessagesContract.Repo) :
    ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>) = MessagesVM(repo) as T
}