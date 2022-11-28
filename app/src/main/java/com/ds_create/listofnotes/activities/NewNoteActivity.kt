package com.ds_create.listofnotes.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ActivityNewNoteBinding
import com.ds_create.listofnotes.entities.NoteItem
import com.ds_create.listofnotes.fragments.NoteFragment
import com.ds_create.listofnotes.utils.HtmlManager
import com.ds_create.listofnotes.utils.MyTouchListener
import com.ds_create.listofnotes.utils.TimeManager
import java.text.SimpleDateFormat
import java.util.*

class NewNoteActivity : AppCompatActivity() {

    private val binding by lazy { ActivityNewNoteBinding.inflate(layoutInflater) }
    private lateinit var defPreferences: SharedPreferences
    private var note: NoteItem? = null
    private var preferences: SharedPreferences? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        setTheme(getSelectedTheme())
        setContentView(binding.root)
        actionBarSettings()
        getNote()
        init()
        setTextSize()
        onClickColorPicker()
        actionMenuCallback()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun init() {
        binding.colorPicker.setOnTouchListener(MyTouchListener())
        preferences = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.new_note_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.id_save -> {
                setMainResult()
            }
            android.R.id.home -> {
                finish()
            }
            R.id.id_bold -> {
                setBoldForSelectedText()
            }
            R.id.id_color -> {
              if (binding.colorPicker.isShown) {
                  closeColorPicker()
              } else {
                  openColorPicker()
              }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun onClickColorPicker() = with(binding) {
        ibRed.setOnClickListener {
            setColorForSelectedText(R.color.picker_red)
        }
        ibBlack.setOnClickListener {
            setColorForSelectedText(R.color.picker_black)
        }
        ibBlue.setOnClickListener {
            setColorForSelectedText(R.color.picker_blue)
        }
        ibYellow.setOnClickListener {
            setColorForSelectedText(R.color.picker_yellow)
        }
        ibGreen.setOnClickListener {
            setColorForSelectedText(R.color.picker_green)
        }
        ibOrange.setOnClickListener {
            setColorForSelectedText(R.color.picker_orange)
        }
    }

    private fun setBoldForSelectedText() = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, StyleSpan::class.java)
        var boldStyle: StyleSpan? = null
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        } else {
            boldStyle = StyleSpan(Typeface.BOLD)
        }
        edDescription.text.setSpan(boldStyle, startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setColorForSelectedText(colorId: Int) = with(binding) {
        val startPos = edDescription.selectionStart
        val endPos = edDescription.selectionEnd

        val styles = edDescription.text.getSpans(startPos, endPos, ForegroundColorSpan::class.java)
        if (styles.isNotEmpty()) {
            edDescription.text.removeSpan(styles[0])
        }
        edDescription.text.setSpan(ForegroundColorSpan(ContextCompat.getColor(
            this@NewNoteActivity, colorId)
        ),
            startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        edDescription.text.trim()
        edDescription.setSelection(startPos)
    }

    private fun setMainResult() {
        var editState = "new"
        val tempNote: NoteItem? = if (note == null) {
            createNewNote()
        } else {
            editState = "update"
            updateNote()
        }
        val intent = Intent().apply {
            putExtra(NoteFragment.NEW_NOTE_KEY, tempNote)
            putExtra(NoteFragment.EDIT_STATE_KEY, editState)
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun updateNote(): NoteItem? = with(binding) {
       return note?.copy(
            title = edTitle.text.toString(),
            content = HtmlManager.toHtml(edDescription.text)
        )
    }

    private fun actionBarSettings() {
        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun createNewNote(): NoteItem {
        return NoteItem(
            null,
            binding.edTitle.text.toString(),
            HtmlManager.toHtml(binding.edDescription.text),
            TimeManager.getCurrentTime(),
            ""
        )
    }

    private fun getNote() {
        val sNote = intent.getSerializableExtra(NoteFragment.NEW_NOTE_KEY)
        if (sNote != null) {
            note = sNote as NoteItem
            fillNote()
        }
    }

    private fun fillNote() = with(binding) {
            edTitle.setText(note?.title)
            edDescription.setText(HtmlManager.getFromHtml(note?.content!!).trim())
    }

    private fun openColorPicker() {
        binding.colorPicker.visibility = View.VISIBLE
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.open_color_picker)
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun closeColorPicker() {
        val openAnim = AnimationUtils.loadAnimation(this, R.anim.close_color_picker)
        openAnim.setAnimationListener(object : Animation.AnimationListener {

            override fun onAnimationStart(p0: Animation?) {

            }

            override fun onAnimationEnd(p0: Animation?) {
                binding.colorPicker.visibility = View.GONE
            }

            override fun onAnimationRepeat(p0: Animation?) {
            }
        })
        binding.colorPicker.startAnimation(openAnim)
    }

    private fun actionMenuCallback() {
        val actionCallback = object : ActionMode.Callback {

            override fun onCreateActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onPrepareActionMode(p0: ActionMode?, menu: Menu?): Boolean {
                menu?.clear()
                return true
            }

            override fun onActionItemClicked(p0: ActionMode?, menu: MenuItem?): Boolean {
                return true
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
            }
        }
        binding.edDescription.customSelectionActionModeCallback = actionCallback
    }

    private fun EditText.setTextSize(size: String?) {
        if (size != null) this.textSize = size.toFloat()
    }

    private fun setTextSize() = with(binding) {
        edTitle.setTextSize(preferences?.getString(TITLE_SIZE_KEY_FROM_SETTINGS_PREF,
            TITLE_SIZE_FROM_SETTINGS_PREF))
        edDescription.setTextSize(preferences?.getString(CONTENT_SIZE_KEY_FROM_SETTINGS_PREF,
            CONTENT_SIZE_FROM_SETTINGS_PREF))
    }

    private fun getSelectedTheme(): Int {
        return if (defPreferences.getString(THEME_KEY_FROM_SETTINGS_PREF,
                THEME_COLOR_FROM_SETTINGS_PREF) == THEME_COLOR_FROM_SETTINGS_PREF) {
            R.style.Theme_NewNote_Blue
        } else {
            R.style.Theme_NewNote_Green
        }
    }

    companion object {
        private const val THEME_KEY_FROM_SETTINGS_PREF = "theme_key"
        private const val THEME_COLOR_FROM_SETTINGS_PREF = "голубая"
        private const val TITLE_SIZE_KEY_FROM_SETTINGS_PREF = "title_size_key"
        private const val TITLE_SIZE_FROM_SETTINGS_PREF = "16"
        private const val CONTENT_SIZE_KEY_FROM_SETTINGS_PREF = "content_size_key"
        private const val CONTENT_SIZE_FROM_SETTINGS_PREF = "14"
    }
}