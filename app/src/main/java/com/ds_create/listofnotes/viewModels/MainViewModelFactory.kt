package com.ds_create.listofnotes.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ds_create.listofnotes.database.MainDatabase

class MainViewModelFactory(private val database: MainDatabase): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java))
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(database) as T
        throw java.lang.IllegalArgumentException("Unknown ViewModelClass")
    }
}