package com.ds_create.listofnotes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ListNameItemBinding
import com.ds_create.listofnotes.entities.ListOfNotesName
import com.ds_create.listofnotes.entities.NoteItem

class ListNameAdapter(
   private val listener: Listener

): ListAdapter<ListOfNotesName, ListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListNameItemBinding.bind(itemView)

        fun setData(listNameItem: ListOfNotesName, listener: Listener) = with(binding) {
            tvListName.text = listNameItem.name
            tvTime.text = listNameItem.time

            itemView.setOnClickListener {}
            ibDelete.setOnClickListener {
                listener.deleteItem(listNameItem.id!!)
            }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context)
                        .inflate(R.layout.list_name_item, parent, false)
                )
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<ListOfNotesName>() {

        override fun areItemsTheSame(oldItem: ListOfNotesName, newItem: ListOfNotesName): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListOfNotesName, newItem: ListOfNotesName): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
}