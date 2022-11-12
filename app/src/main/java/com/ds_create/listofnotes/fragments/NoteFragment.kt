package com.ds_create.listofnotes.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.ds_create.listofnotes.activities.MainApp
import com.ds_create.listofnotes.activities.NewNoteActivity
import com.ds_create.listofnotes.databinding.FragmentNoteBinding
import com.ds_create.listofnotes.viewModels.MainViewModel
import com.ds_create.listofnotes.viewModels.MainViewModelFactory

class NoteFragment : BaseFragment() {

    private var _binding: FragmentNoteBinding? = null
    private val binding: FragmentNoteBinding
        get() = _binding ?: throw RuntimeException("FragmentNoteBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }
    private lateinit var editLauncher: ActivityResultLauncher<Intent>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onEditResult()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun onEditResult() {
        editLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                Log.d("MyLog", "title: ${it.data?.getStringExtra(TITLE_KEY)}")
                Log.d("MyLog", "description: ${it.data?.getStringExtra(DESC_KEY)}")
            }
        }
    }

    override fun onClickNew() {
        editLauncher.launch(Intent(activity, NewNoteActivity::class.java))
    }



    companion object {

        const val TITLE_KEY = "title_key"
        const val DESC_KEY = "desc_key"

        @JvmStatic
        fun newInstance() = NoteFragment()
    }
}