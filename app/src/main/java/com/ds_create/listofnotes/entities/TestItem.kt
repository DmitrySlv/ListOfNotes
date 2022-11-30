package com.ds_create.listofnotes.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "test")
data class TestItem(

    @PrimaryKey(autoGenerate = true)
    val id: Int?,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "price", defaultValue = "")
    val price: String,

)
