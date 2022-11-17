package com.ds_create.listofnotes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ds_create.listofnotes.activities.MainApp
import com.ds_create.listofnotes.adapters.ListNameAdapter
import com.ds_create.listofnotes.databinding.FragmentListNamesBinding
import com.ds_create.listofnotes.entities.ListOfNotesName
import com.ds_create.listofnotes.entities.NoteItem
import com.ds_create.listofnotes.utils.TimeManager
import com.ds_create.listofnotes.utils.dialogs.DeleteDialog
import com.ds_create.listofnotes.utils.dialogs.NewListDialog
import com.ds_create.listofnotes.viewModels.MainViewModel
import com.ds_create.listofnotes.viewModels.MainViewModelFactory

class ListNamesFragment : BaseFragment(), ListNameAdapter.Listener {

    private var _binding: FragmentListNamesBinding? = null
    private val binding: FragmentListNamesBinding
        get() = _binding ?: throw RuntimeException("FragmentListNamesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    private lateinit var adapter: ListNameAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListNamesBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRcView()
        observer()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun initRcView() = with(binding) {
        rcView.layoutManager = LinearLayoutManager(activity)
        adapter = ListNameAdapter(this@ListNamesFragment)
        rcView.adapter = adapter
    }

    private fun observer() {
        mainViewModel.allListNames.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    override fun onClickNew() {
        NewListDialog.showDialog(activity as AppCompatActivity, object: NewListDialog.Listener {

            override fun onClick(name: String) {
             val listName = ListOfNotesName(
                 null,
                 name,
                 TimeManager.getCurrentTime(),
                 0,
                 0,
                 ""
             )
                mainViewModel.insertListName(listName)
            }
        })
    }

    override fun deleteItem(id: Int) {
        DeleteDialog.showDialog(requireContext(), object : DeleteDialog.Listener {

            override fun onClick() {
                mainViewModel.deleteListName(id)
            }
        })
    }

    override fun onClickItem(note: NoteItem) {
    }

    companion object {

        @JvmStatic
        fun newInstance() = ListNamesFragment()
    }
}