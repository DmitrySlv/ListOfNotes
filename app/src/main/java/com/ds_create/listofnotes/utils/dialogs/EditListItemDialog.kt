package com.ds_create.listofnotes.utils.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.ds_create.listofnotes.databinding.EditListItemDialogBinding
import com.ds_create.listofnotes.entities.ListOfNotesItem

object EditListItemDialog {

    fun showDialog(context: Context, item: ListOfNotesItem, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = EditListItemDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            edName.setText(item.name)
            edInfo.setText(item.itemInfo)
            btnUpdate.setOnClickListener {
                if (edName.text.toString().isNotEmpty()) {
                    listener.onClick(item.copy(
                        name = edName.text.toString(),
                        itemInfo = edInfo.text.toString())
                    )
                }
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick(item: ListOfNotesItem)
    }
}