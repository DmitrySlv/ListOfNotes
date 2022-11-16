package com.ds_create.listofnotes.utils

import java.text.SimpleDateFormat
import java.util.*

object TimeManager {

    fun getCurrentTime(): String {
        val formatter = SimpleDateFormat(PATTERN_TIME, Locale.getDefault())
        return formatter.format(Calendar.getInstance().time)
    }

    private const val PATTERN_TIME = "hh:mm:ss - yyyy/MM/dd"
}