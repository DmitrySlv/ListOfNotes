package com.ds_create.listofnotes.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ds_create.listofnotes.entities.LibraryItem
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNames
import com.ds_create.listofnotes.entities.NoteItem

@Database(entities = [LibraryItem::class, NoteItem::class, ListOfNotesItem::class,
    ListOfNotesNames::class], version = 1)
abstract class MainDatabase: RoomDatabase() {

    abstract fun getDao(): Dao

    companion object {
        @Volatile
        private var INSTANCE: MainDatabase? = null
        private const val DB_NAME = "list_Of_Notes.db"

        fun getDatabase(application: Application): MainDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    application,
                    MainDatabase::class.java,
                    DB_NAME
                    ).build()
                instance
            }
        }
    }
}