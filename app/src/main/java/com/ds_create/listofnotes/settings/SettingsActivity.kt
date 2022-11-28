package com.ds_create.listofnotes.settings

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.preference.PreferenceManager
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.fragments.SettingsFragment

class SettingsActivity : AppCompatActivity() {

    private lateinit var defPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(R.layout.activity_settings)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.placeHolder, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) finish()
        return super.onOptionsItemSelected(item)
    }

    private fun getSelectedTheme(): Int {
        return if (defPreferences.getString( THEME_KEY_FROM_SETTINGS_PREF,
                THEME_COLOR_FROM_SETTINGS_PREF) == THEME_COLOR_FROM_SETTINGS_PREF
        ) {
            R.style.Theme_ListOfNotes_Blue
        } else {
            R.style.Theme_ListOfNotes_Green
        }
    }

    companion object {
        private const val THEME_KEY_FROM_SETTINGS_PREF = "theme_key"
        private const val THEME_COLOR_FROM_SETTINGS_PREF = "голубая"
    }
}