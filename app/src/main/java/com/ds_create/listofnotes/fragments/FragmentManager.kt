package com.ds_create.listofnotes.fragments

import androidx.appcompat.app.AppCompatActivity
import com.ds_create.listofnotes.R

object FragmentManager {
    var currentFrag: BaseFragment? = null

    fun setFragment(newFrag: BaseFragment, activity: AppCompatActivity) {
        val transaction = activity.supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container, newFrag)
        transaction.commit()
        currentFrag = newFrag
    }


}