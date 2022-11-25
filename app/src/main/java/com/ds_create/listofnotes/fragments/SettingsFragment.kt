package com.ds_create.listofnotes.fragments

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.ds_create.listofnotes.R

class SettingsFragment: PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
    }
}