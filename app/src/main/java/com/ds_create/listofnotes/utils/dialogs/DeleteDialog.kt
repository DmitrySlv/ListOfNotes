package com.ds_create.listofnotes.utils.dialogs

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.ds_create.listofnotes.databinding.DeleteDialogBinding

object DeleteDialog {

    fun showDialog(context: Context, listener: Listener) {
        var dialog: AlertDialog? = null
        val builder = AlertDialog.Builder(context)
        val binding = DeleteDialogBinding.inflate(LayoutInflater.from(context))
        builder.setView(binding.root)
        binding.apply {
            btnDelete.setOnClickListener {
                listener.onClick()
                dialog?.dismiss()
            }
            btnCancel.setOnClickListener {
                dialog?.dismiss()
            }
        }
        dialog = builder.create()
        dialog.window?.setBackgroundDrawable(null)
        dialog.show()
    }

    interface Listener {
        fun onClick()
    }
}