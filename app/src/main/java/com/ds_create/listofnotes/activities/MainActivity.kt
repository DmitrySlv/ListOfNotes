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
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class MainActivity : AppCompatActivity(), NewListDialog.Listener {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var currentMenuItemId = R.id.list_of_notes
    private lateinit var defPreferences: SharedPreferences
    private var currentTheme = ""
    private var interstitialAd: InterstitialAd? = null
    private var adShowCounter = 0
    private var adShowCounterMax = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        currentTheme = defPreferences.getString(THEME_KEY_FROM_SETTINGS_PREF,
            THEME_COLOR_FROM_SETTINGS_PREF).toString()
        setTheme(getSelectedTheme())
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        FragmentManager.setFragment(ListNamesFragment.newInstance(), this)
        setBottomNavListener()
        loadInterAd()
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

    private fun loadInterAd() {
        val request = AdRequest.Builder().build()
        InterstitialAd.load(
            this,
            getString(R.string.interstitial_ad_id),
            request,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(ad: InterstitialAd) {
                   interstitialAd = ad
                }
                override fun onAdFailedToLoad(ad: LoadAdError) {
                    interstitialAd = null
                }
            })
    }

    private fun showInterAd(adListener: AdListener) {
        if (interstitialAd != null && adShowCounter > adShowCounterMax) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    interstitialAd = null
                    loadInterAd()
                    adListener.onFinish()
                }
                override fun onAdShowedFullScreenContent() {
                    interstitialAd = null
                    loadInterAd()
                }
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    interstitialAd = null
                    loadInterAd()
                }
            }
            adShowCounter = 0
            interstitialAd?.show(this)
        } else {
            adShowCounter++
            adListener.onFinish()
        }
    }

    private fun setBottomNavListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            startActivity(Intent(this@MainActivity,
                                SettingsActivity::class.java))
                        }
                    })
                }
                R.id.notes -> {
                    showInterAd(object : AdListener {
                        override fun onFinish() {
                            currentMenuItemId = R.id.notes
                            FragmentManager.setFragment(NoteFragment.newInstance(),
                                this@MainActivity)
                        }
                    })
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

    interface AdListener {
        fun onFinish()
    }

    companion object {
        private const val THEME_KEY_FROM_SETTINGS_PREF = "theme_key"
        private const val THEME_COLOR_FROM_SETTINGS_PREF = "голубая"
    }
}