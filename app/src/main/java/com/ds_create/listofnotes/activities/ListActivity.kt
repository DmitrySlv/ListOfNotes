package com.ds_create.listofnotes.activities

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ds_create.listofnotes.databinding.ActivityListBinding
import com.ds_create.listofnotes.entities.ListOfNotesNameItem
import com.ds_create.listofnotes.viewModels.MainViewModel
import com.ds_create.listofnotes.viewModels.MainViewModelFactory

class ListActivity : AppCompatActivity() {

    private val binding by lazy { ActivityListBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((applicationContext as MainApp).database)
    }
    private var listNameItem: ListOfNotesNameItem? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        init()
    }

    private fun init() = with(binding) {
        listNameItem = intent.getSerializableExtra(LIST_NAME) as ListOfNotesNameItem
        tvTest.text = listNameItem?.name
    }

    companion object {
        const val LIST_NAME = "list_name"
    }
}