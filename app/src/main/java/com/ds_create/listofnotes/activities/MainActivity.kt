package com.ds_create.listofnotes.activities

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.preference.PreferenceManager
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ActivityMainBinding
import com.ds_create.listofnotes.fragments.FragmentManager
import com.ds_create.listofnotes.fragments.NoteFragment
import com.ds_create.listofnotes.fragments.ListNamesFragment
import com.ds_create.listofnotes.settings.SettingsActivity
import com.ds_create.listofnotes.utils.dialogs.NewListDialog

class MainActivity : AppCompatActivity(), NewListDialog.Listener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentMenuItemId = R.id.list_of_notes
    private lateinit var defPreferences: SharedPreferences
    private var currentTheme = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPreferences.getString(THEME_KEY_FROM_SETTINGS_PREF,
            THEME_COLOR_FROM_SETTINGS_PREF).toString()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FragmentManager.setFragment(ListNamesFragment.newInstance(), this)
        setBottomNavListener()
    }

    override fun onResume() {
        super.onResume()
        binding.bottomNavigation.selectedItemId = currentMenuItemId
        if (defPreferences.getString(
                THEME_KEY_FROM_SETTINGS_PREF,
                THEME_COLOR_FROM_SETTINGS_PREF) != currentTheme
        ) {
            recreate()
        }
    }

    private fun setBottomNavListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                   startActivity(Intent(this, SettingsActivity::class.java))
                }
                R.id.notes -> {
                    currentMenuItemId = R.id.notes
                    FragmentManager.setFragment(NoteFragment.newInstance(), this)
                }
                R.id.list_of_notes -> {
                    currentMenuItemId = R.id.list_of_notes
                    FragmentManager.setFragment(ListNamesFragment.newInstance(), this)
                }
                R.id.new_item -> {
                    FragmentManager.currentFrag?.onClickNew()
                }
            }
            true
        }
    }

    private fun getSelectedTheme(): Int {
        return if (defPreferences.getString(
                THEME_KEY_FROM_SETTINGS_PREF,
                THEME_COLOR_FROM_SETTINGS_PREF) == THEME_COLOR_FROM_SETTINGS_PREF
        ) {
            R.style.Theme_ListOfNotes_Blue
        } else {
            R.style.Theme_ListOfNotes_Green
        }
    }

    override fun onClick(name: String) {
        Log.d("MyLog", "Name: $name")
    }

    companion object {
        private const val THEME_KEY_FROM_SETTINGS_PREF = "theme_key"
        private const val THEME_COLOR_FROM_SETTINGS_PREF = "голубая"
    }
}