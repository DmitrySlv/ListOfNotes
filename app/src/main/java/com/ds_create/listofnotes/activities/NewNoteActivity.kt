package com.ds_create.listofnotes.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ActivityNewNoteBinding
import com.ds_create.listofnotes.fragments.NoteFragment

class NewNoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewNoteBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        actionBarSettings()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.id_save) {
           setMainResult()
        } else if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setMainResult() {
        val intent = Intent().apply {
            putExtra(NoteFragment.TITLE_KEY, binding.edTitle.text.toString())
            putExtra(NoteFragment.DESC_KEY, binding.edDescription.text.toString())
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun actionBarSettings() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }
}