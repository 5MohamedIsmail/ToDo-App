package com.route.todoapp.ui.home.tasksList

import androidx.recyclerview.widget.DiffUtil
import com.route.todoapp.database.model.Task

class TaskDiffUtil(
    private val oldList: List<Task>,
    private val newList: List<Task>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return when {
            oldList[oldItemPosition].id != newList[newItemPosition].id -> {
                false
            }

            oldList[oldItemPosition].title != newList[newItemPosition].title -> {
                false
            }

            oldList[oldItemPosition].time != newList[newItemPosition].time -> {
                false
            }

            else -> true

        }
    }
}