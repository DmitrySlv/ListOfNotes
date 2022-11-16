package com.ds_create.listofnotes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.ds_create.listofnotes.activities.MainApp
import com.ds_create.listofnotes.databinding.FragmentShopListNamesBinding
import com.ds_create.listofnotes.entities.ListOfNotesName
import com.ds_create.listofnotes.utils.TimeManager
import com.ds_create.listofnotes.utils.dialogs.NewListDialog
import com.ds_create.listofnotes.viewModels.MainViewModel
import com.ds_create.listofnotes.viewModels.MainViewModelFactory

class ListNamesFragment : BaseFragment() {

    private var _binding: FragmentShopListNamesBinding? = null
    private val binding: FragmentShopListNamesBinding
        get() = _binding ?: throw RuntimeException("FragmentShopListNamesBinding is null")

    private val mainViewModel: MainViewModel by activityViewModels {
        MainViewModelFactory((context?.applicationContext as MainApp).database)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShopListNamesBinding.inflate(layoutInflater, container, false)
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
    }

    private fun observer() {
        mainViewModel.allListNames.observe(viewLifecycleOwner) {
        }
    }

    override fun onClickNew() {
        NewListDialog.showDialog(requireActivity(), object: NewListDialog.Listener {

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

    companion object {

        @JvmStatic
        fun newInstance() = ListNamesFragment()
    }
}