package com.ds_create.listofnotes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity (tableName = "list_of_notes_names")
data class ListOfNotesNameItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo (name = "name")
    val name: String,

    @ColumnInfo (name = "time")
    val time: String,

    @ColumnInfo (name = "allItemCounter")
    val allItemCounter: Int,

    @ColumnInfo (name = "checkedItemsCounter")
    val checkedItemsCounter: Int,

    @ColumnInfo (name = "itemsIds")
    val itemsIds: String

): Serializable
