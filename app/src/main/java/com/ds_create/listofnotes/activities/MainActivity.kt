package com.ds_create.listofnotes.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setBottomNavListener()
    }

    private fun setBottomNavListener() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.settings -> {
                    Log.d("MyLog", "Settings")
                }
                R.id.notes -> {
                    Log.d("MyLog", "Notes")
                }
                R.id.list_of_notes -> {
                    Log.d("MyLog", "List of notes")
                }
                R.id.new_item -> {
                    Log.d("MyLog", "New")
                }
            }
            true
        }
    }
}