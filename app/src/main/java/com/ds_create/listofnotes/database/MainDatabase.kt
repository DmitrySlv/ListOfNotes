package com.ds_create.listofnotes.database

import android.app.Application
import androidx.room.AutoMigration
import androidx.room.Database
import androidx.room.DeleteColumn
import androidx.room.DeleteTable
import androidx.room.RenameColumn
import androidx.room.RenameTable
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.AutoMigrationSpec
import com.ds_create.listofnotes.entities.*

@Database(entities = [LibraryItem::class, NoteItem::class, ListOfNotesItem::class,
    ListOfNotesNameItem::class], version = 6, exportSchema = true,
    autoMigrations = [
        AutoMigration(from = 5, to = 6, spec = MainDatabase.SpecMigration::class)
    ]
)
abstract class MainDatabase: RoomDatabase() {

    @RenameTable ( fromTableName = "library", toTableName = "help")
    class SpecMigration: AutoMigrationSpec

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