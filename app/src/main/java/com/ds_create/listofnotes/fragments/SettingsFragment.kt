package com.ds_create.listofnotes.fragments

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.utils.billing.BillingManager

class SettingsFragment: PreferenceFragmentCompat() {

    private lateinit var removeAdsPref: Preference
    private lateinit var billingManager: BillingManager

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.settings_preference, rootKey)
        init()
    }

    override fun onDestroy() {
        billingManager.closeConnection()
        super.onDestroy()
    }

    private fun init() {
        billingManager = BillingManager(activity as AppCompatActivity)
        removeAdsPref = findPreference(REMOVE_ADS_KEY)!!
        removeAdsPref.setOnPreferenceClickListener {
            billingManager.startConnection()
            true
        }
    }

    companion object {
        private const val REMOVE_ADS_KEY = "remove_ads_key"
    }
}