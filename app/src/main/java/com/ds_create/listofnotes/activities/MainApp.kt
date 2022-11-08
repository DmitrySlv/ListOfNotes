package com.ds_create.listofnotes.activities

import android.app.Application
import com.ds_create.listofnotes.database.MainDatabase

class MainApp: Application() {
    val database by lazy { MainDatabase.getDatabase(this) }
}