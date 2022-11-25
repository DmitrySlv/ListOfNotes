package com.ds_create.listofnotes.adapters

import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ds_create.listofnotes.R
import com.ds_create.listofnotes.databinding.NoteListItemBinding
import com.ds_create.listofnotes.entities.NoteItem
import com.ds_create.listofnotes.utils.HtmlManager
import com.ds_create.listofnotes.utils.TimeManager

class NoteAdapter(
    private val listener: Listener,
    private val defPreferences: SharedPreferences
    ): ListAdapter<NoteItem, NoteAdapter.ItemHolder>(ItemComparator()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemHolder {
        return ItemHolder.create(parent)
    }

    override fun onBindViewHolder(holder: ItemHolder, position: Int) {
        holder.setData(getItem(position), listener, defPreferences)
    }

    class ItemHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = NoteListItemBinding.bind(itemView)

        fun setData(
            note: NoteItem, listener: Listener, defPreferences: SharedPreferences
            ) = with(binding) {
            tvTitle.text = note.title
            tvDescription.text = HtmlManager.getFromHtml(note.content).trim()
            tvTime.text = TimeManager.getTimeFormat(note.time, defPreferences)

            itemView.setOnClickListener { listener.onClickItem(note) }
            ibDelete.setOnClickListener { listener.deleteItem(note.id!!) }
        }

        companion object {
            fun create(parent: ViewGroup): ItemHolder {
                return ItemHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.note_list_item, parent, false)
                )
            }
        }
    }

    class ItemComparator: DiffUtil.ItemCallback<NoteItem>() {

        override fun areItemsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: NoteItem, newItem: NoteItem): Boolean {
            return oldItem == newItem
        }
    }

    interface Listener {
        fun deleteItem(id: Int)
        fun onClickItem(note: NoteItem)
    }
}