package com.ds_create.listofnotes.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ds_create.listofnotes.database.MainDatabase
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNameItem
import com.ds_create.listofnotes.entities.NoteItem
import kotlinx.coroutines.launch

class MainViewModel(database: MainDatabase): ViewModel() {

    val dao = database.getDao()
    val allNotes: LiveData<List<NoteItem>> = dao.getAllNotes().asLiveData()
    val allListNames: LiveData<List<ListOfNotesNameItem>> = dao.getAllListNames().asLiveData()
    fun getAllItemsFromList(listId: Int): LiveData<List<ListOfNotesItem>> {
        return dao.getAllListItems(listId).asLiveData()
    }

    fun insertNote(note: NoteItem) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun insertListName(listName: ListOfNotesNameItem) = viewModelScope.launch {
        dao.insertListName(listName)
    }

    fun insertListItem(listItem: ListOfNotesItem) = viewModelScope.launch {
        dao.insertListItem(listItem)
    }

    fun updateListItem(item: ListOfNotesItem) = viewModelScope.launch {
        dao.updateListItem(item)
    }

    fun updateNote(note: NoteItem) = viewModelScope.launch {
        dao.updateNote(note)
    }

    fun updateListName(listOfNotesNameItem: ListOfNotesNameItem) = viewModelScope.launch {
        dao.updateListName(listOfNotesNameItem)
    }

    fun deleteNote(id: Int) = viewModelScope.launch {
        dao.deleteNote(id)
    }

    fun deleteListName(id: Int) = viewModelScope.launch {
        dao.deleteListName(id)
    }
}