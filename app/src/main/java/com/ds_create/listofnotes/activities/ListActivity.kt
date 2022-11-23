package com.ds_create.listofnotes.activities

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.MenuItem.OnActionExpandListener
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.adapters.ListItemAdapter
import com.ds_create.listofnotes.databinding.ActivityListBinding
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNameItem
import com.ds_create.listofnotes.utils.ShareHelper
import com.ds_create.listofnotes.utils.dialogs.EditListItemDialog
import com.ds_create.listofnotes.viewModels.MainViewModel
import com.ds_create.listofnotes.viewModels.MainViewModelFactory

class ListActivity : AppCompatActivity(), ListItemAdapter.Listener {

    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((applicationContext as MainApp).database)
    }
    private var listNameItem: ListOfNotesNameItem? = null
    private lateinit var saveItem: MenuItem
    private var edItem: EditText? = null
    private var adapter: ListItemAdapter? = null
    private lateinit var textWatcher: TextWatcher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
        initRcView()
        listItemObserver()
    }

    private fun init() = with(binding) {
        listNameItem = intent.getSerializableExtra(LIST_NAME) as ListOfNotesNameItem
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.list_menu, menu)
        saveItem = menu?.findItem(R.id.save_item)!!
        val newItem = menu.findItem(R.id.new_item)
        edItem = newItem.actionView.findViewById(R.id.edNewNoteItem) as EditText
        newItem.setOnActionExpandListener(expandActionView())
        saveItem.isVisible = false
        textWatcher = initTextWatcher()
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_item -> {  addNewNoteItem() }
            R.id.delete_list -> {
                mainViewModel.deleteNoteList(listNameItem?.id!!, true)
                finish()
            }
            R.id.clear_list -> {
                mainViewModel.deleteNoteList(listNameItem?.id!!, false)
            }
            R.id.share_list -> {
                startActivity(Intent.createChooser(ShareHelper.shareShopList(
                    adapter?.currentList!!, listNameItem?.name!!),
                    this.getString(R.string.share_by)
                ))
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun initTextWatcher(): TextWatcher {
        return object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                mainViewModel.getAllLibraryItems("%$s%")
            }

            override fun afterTextChanged(s: Editable?) {
            }
        }
    }

    private fun expandActionView(): OnActionExpandListener {
        return object : OnActionExpandListener {

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                edItem?.addTextChangedListener(textWatcher)
                libraryItemObserver()
                mainViewModel.getAllItemsFromList(listNameItem?.id!!)
                    .removeObservers(this@ListActivity)
                mainViewModel.getAllLibraryItems("%%")
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                edItem?.removeTextChangedListener(textWatcher)
                invalidateOptionsMenu()
                mainViewModel.libraryItems.removeObservers(this@ListActivity)
                edItem?.setText("")
                listItemObserver()
                return true
            }
        }
    }

    private fun addNewNoteItem() {
        if (edItem?.text.toString().isEmpty()) return
        val item = ListOfNotesItem(
            null,
            edItem?.text.toString(),
            "",
            false,
            listNameItem?.id!!,
            0
        )
        edItem?.setText("")
        mainViewModel.insertListItem(item)
    }

    private fun listItemObserver() {
        mainViewModel.getAllItemsFromList(listNameItem?.id!!).observe(this) {
            adapter?.submitList(it)
            binding.tvEmpty.visibility = if (it.isEmpty()) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }
    }

    private fun libraryItemObserver() {
        mainViewModel.libraryItems.observe(this) {
            val tempNoteList = ArrayList<ListOfNotesItem>()
            it.forEach { item ->
                val noteItem = ListOfNotesItem(
                    item.id,
                    item.name,
                    "",
                    false,
                    0,
                    1
                )
                tempNoteList.add(noteItem)
            }
            adapter?.submitList(tempNoteList)
        }
    }

    private fun initRcView() = with(binding) {
        adapter = ListItemAdapter(this@ListActivity)
        rcView.layoutManager = LinearLayoutManager(this@ListActivity)
        rcView.adapter = adapter
    }

    override fun onClickItem(listItem: ListOfNotesItem, state: Int) {
        when (state) {
            ListItemAdapter.EDIT -> editListItem(listItem)
            ListItemAdapter.CHECK_BOX -> { mainViewModel.updateListItem(listItem) }
        }
    }

    private fun editListItem(item: ListOfNotesItem) {
        EditListItemDialog.showDialog(this, item, object: EditListItemDialog.Listener {

            override fun onClick(item: ListOfNotesItem) {
                mainViewModel.updateListItem(item)
            }
        })
    }

    companion object {
        const val LIST_NAME = "list_name"
    }
}