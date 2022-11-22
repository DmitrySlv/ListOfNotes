package com.ds_create.listofnotes.activities

import android.os.Bundle
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
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.save_item) {
            addNewNoteItem()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun expandActionView(): OnActionExpandListener {
        return object : OnActionExpandListener {

            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                saveItem.isVisible = true
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                saveItem.isVisible = false
                invalidateOptionsMenu()
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