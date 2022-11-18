package com.ds_create.listofnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ds_create.listofnotes.R
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

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListNameItemBinding.bind(itemView)

        fun setNoteItemData(listOfNotesItem: ListOfNotesItem, listener: Listener) = with(binding) {
        }

        fun setLibraryData(listOfNotesItem: ListOfNotesItem, listener: Listener) = with(binding) {
        }

        companion object {

            fun createNoteItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }

            fun createLibraryItem(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
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
        fun deleteItem(id: Int)
        fun editItem(listNameItem: ListOfNotesNameItem)
        fun onClickItem(listNameItem: ListOfNotesNameItem)
    }
}