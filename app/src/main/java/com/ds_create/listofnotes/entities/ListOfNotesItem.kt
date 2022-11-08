package com.ds_create.listofnotes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_of_note_item")
data class ListOfNotesItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "itemInfo")
    val itemInfo: String?,

    @ColumnInfo(name = "itemChecked")
    val itemChecked: Int = 0,

    @ColumnInfo(name = "listId")
    val listId: Int,

    @ColumnInfo(name = "itemType")
    val itemType: String = "item"

)
