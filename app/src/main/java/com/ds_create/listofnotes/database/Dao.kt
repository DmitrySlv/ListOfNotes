package com.ds_create.listofnotes.database

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNameItem
import com.ds_create.listofnotes.entities.NoteItem
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Query ("SELECT * FROM list_of_notes_names")
    fun getAllListNames(): Flow<List<ListOfNotesNameItem>>

    @Query ("SELECT * FROM list_of_note_item WHERE listId LIKE :listId")
    fun getAllListItems(listId: Int): Flow<List<ListOfNotesItem>>

    @Query ("DELETE FROM note_list WHERE id IS :id")
    suspend fun deleteNote(id: Int)

    @Query ("DELETE FROM list_of_notes_names WHERE id IS :id")
    suspend fun deleteListName(id: Int)

    @Insert
    suspend fun insertNote(note: NoteItem)

    @Insert
    suspend fun insertListItem(listItem: ListOfNotesItem)

    @Insert
    suspend fun insertListName(nameItem: ListOfNotesNameItem)

    @Update
    suspend fun updateNote(note: NoteItem)

    @Update
    suspend fun updateListName(listOfNotesNameItem: ListOfNotesNameItem)
}