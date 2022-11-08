package com.ds_create.listofnotes.database

import androidx.room.Insert
import androidx.room.Query
import com.ds_create.listofnotes.entities.NoteItem
import kotlinx.coroutines.flow.Flow

@androidx.room.Dao
interface Dao {

    @Query ("SELECT * FROM note_list")
    fun getAllNotes(): Flow<List<NoteItem>>

    @Insert
    suspend fun insertNote(note: NoteItem)
}