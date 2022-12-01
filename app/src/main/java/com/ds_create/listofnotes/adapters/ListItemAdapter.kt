package com.ds_create.listofnotes.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.LibraryListItemBinding
import com.ds_create.listofnotes.databinding.ListItemBinding
import com.ds_create.listofnotes.databinding.ListNameItemBinding
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNameItem

class ListItemAdapter(
   private val listener: Listener
   ): ListAdapter<ListOfNotesItem, ListItemAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return if (viewType == 0) {
            ItemHolder.createNoteItem(parent)
        } else {
            ItemHolder.createLibraryItem(parent)
        }
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        if (getItem(position).itemType == 0) {
            holder.setNoteItemData(getItem(position), listener)
        } else {
            holder.setLibraryData(getItem(position), listener)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).itemType
    }

    class ItemHolder(private val itemView: View): RecyclerView.ViewHolder(itemView) {

        fun setNoteItemData(listOfNotesItem: ListOfNotesItem, listener: Listener) {
            val binding = ListItemBinding.bind(itemView)
            binding.apply {
                tvName.text = listOfNotesItem.name
                tvInfo.text = listOfNotesItem.itemInfo
                tvInfo.visibility = infoVisibility(listOfNotesItem)
                checkBox.isChecked = listOfNotesItem.itemChecked
                setPaintFlagAndColor(binding)
                checkBox.setOnClickListener {
                    listener.onClickItem(listOfNotesItem.copy(
                        itemChecked = checkBox.isChecked),
                        CHECK_BOX
                    )
                }
                ibEdit.setOnClickListener {
                    listener.onClickItem(listOfNotesItem, EDIT)
                }
            }
        }

        fun setLibraryData(listOfNotesItem: ListOfNotesItem, listener: Listener) {
            val binding = LibraryListItemBinding.bind(itemView)
            binding.apply {
                tvName.text = listOfNotesItem.name

                ibEdit.setOnClickListener {
                    listener.onClickItem(listOfNotesItem, EDIT_LIBRARY_ITEM)
                }
                ibDelete.setOnClickListener {
                    listener.onClickItem(listOfNotesItem, DELETE_LIBRARY_ITEM)
                }
                itemView.setOnClickListener {
                    listener.onClickItem(listOfNotesItem, ADD_LIBRARY_ITEM)
                }
            }
        }

       private fun infoVisibility(listOfNotesItem: ListOfNotesItem): Int {
            return if (listOfNotesItem.itemInfo.isEmpty()) {
                View.GONE
            } else {
                View.VISIBLE
            }
        }

        private fun setPaintFlagAndColor(binding: ListItemBinding) {
            binding.apply {
             if (checkBox.isChecked) {
                 tvName.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                 tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_light))
                 tvInfo.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                 tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.gray_light))
             } else {
                 tvName.paintFlags = Paint.ANTI_ALIAS_FLAG
                 tvName.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
                 tvInfo.paintFlags = Paint.ANTI_ALIAS_FLAG
                 tvInfo.setTextColor(ContextCompat.getColor(binding.root.context, R.color.black))
             }
            }
        }

        companion object {

            fun createNoteItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_item, parent, false)
                )
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.library_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ListOfNotesItem>() {

        override fun areItemsTheSame(oldItem: ListOfNotesItem, newItem: ListOfNotesItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListOfNotesItem, newItem: ListOfNotesItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun onClickItem(listItem: ListOfNotesItem, state: Int)
    }

    companion object {
        const val EDIT = 0
        const val CHECK_BOX = 1
        const val EDIT_LIBRARY_ITEM = 2
        const val DELETE_LIBRARY_ITEM = 3
        const val ADD_LIBRARY_ITEM = 4
    }
}