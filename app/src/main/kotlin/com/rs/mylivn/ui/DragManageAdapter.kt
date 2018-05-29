package com.rs.mylivn.ui

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.util.Log

class DragManageAdapter(private var adapter: ImagesRecyclerViewAdapter,
                        dragDirs: Int, swipeDirs: Int) : ItemTouchHelper.SimpleCallback(dragDirs, swipeDirs) {

    private val TAG = DragManageAdapter::class.java.name
    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder,
                        target: RecyclerView.ViewHolder): Boolean {
        Log.d(TAG, "start pos ${viewHolder.adapterPosition} .... end pos ${target.adapterPosition}")
        adapter.swapItems(viewHolder.adapterPosition, target.adapterPosition)
        return true

    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }

}