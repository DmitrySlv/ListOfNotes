package com.ds_create.listofnotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ds_create.listofnotes.database.MainDatabase
import com.ds_create.listofnotes.entities.ListOfNotesName
import com.ds_create.listofnotes.entities.NoteItem
import kotlinx.coroutines.launch

class MainViewModel(database: MainDatabase): ViewModel() {

    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allListNames: LiveData<List<ListOfNotesName>> = dao.getAllListNames().asLiveData()

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertListName(listName: ListOfNotesName) = viewModelScope.launch {
        dao.insertListName(listName)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteListName(id: Int) = viewModelScope.launch {
        dao.deleteListName(id)
    }
}