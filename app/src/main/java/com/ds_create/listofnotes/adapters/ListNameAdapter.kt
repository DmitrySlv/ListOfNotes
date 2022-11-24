package com.ds_create.listofnotes.adapters

import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.ListNameItemBinding
import com.ds_create.listofnotes.entities.ListOfNotesItem
import com.ds_create.listofnotes.entities.ListOfNotesNameItem

class ListNameAdapter(
   private val listener: Listener

): ListAdapter<ListOfNotesNameItem, ListNameAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ListNameItemBinding.bind(itemView)

        fun setData(listNameItem: ListOfNotesNameItem, listener: Listener) = with(binding) {
            tvListName.text = listNameItem.name
            tvTime.text = listNameItem.time
            pBar.max = listNameItem.allItemCounter
            pBar.progress = listNameItem.checkedItemsCounter
            val colorState = ColorStateList.valueOf(getColorProgressState(
                listNameItem, binding.root.context))
            pBar.progressTintList = colorState
            counterCard.backgroundTintList = colorState
            val counterText = "${listNameItem.checkedItemsCounter}/${listNameItem.allItemCounter}"
            tvCounter.text = counterText
            itemView.setOnClickListener {
                listener.onClickItem(listNameItem)
            }
            ibDelete.setOnClickListener {
                listener.deleteItem(listNameItem.id!!)
            }
            ibEdit.setOnClickListener {
                listener.editItem(listNameItem)
            }
        }

        private fun getColorProgressState(item: ListOfNotesNameItem, context: Context): Int {
            return if (item.checkedItemsCounter == item.allItemCounter) {
                ContextCompat.getColor(context, R.color.picker_green)
            } else {
                ContextCompat.getColor(context, R.color.picker_red)
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

    class ItemComparator: DiffUtil.ItemCallback<ListOfNotesNameItem>() {

        override fun areItemsTheSame(oldItem: ListOfNotesNameItem, newItem: ListOfNotesNameItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ListOfNotesNameItem, newItem: ListOfNotesNameItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun editItem(listNameItem: ListOfNotesNameItem)
        fun onClickItem(listNameItem: ListOfNotesNameItem)
    }
}