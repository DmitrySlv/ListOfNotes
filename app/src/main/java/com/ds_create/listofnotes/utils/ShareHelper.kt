package com.ds_create.listofnotes.utils

import android.content.Intent
import com.ds_create.listofnotes.entities.ListOfNotesItem

object ShareHelper {
    fun shareShopList(noteList: List<ListOfNotesItem>, listName: String): Intent {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plane"
        intent.apply {
            putExtra(Intent.EXTRA_TEXT, makeShareText(noteList, listName))
        }
        return intent
    }

    private fun makeShareText(noteList: List<ListOfNotesItem>, listName: String): String {
        val stringBuilder = StringBuilder()
        stringBuilder.append("<<$listName>>")
        stringBuilder.append("\n")
        var counter = 0
        noteList.forEach {
            stringBuilder.append("${++counter} - ${it.name} (${it.itemInfo})")
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }
}