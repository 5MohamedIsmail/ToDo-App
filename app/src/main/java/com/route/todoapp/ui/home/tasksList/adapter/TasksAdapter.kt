package com.route.todoapp.ui.home.tasksList.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.route.todoapp.R
import com.route.todoapp.database.model.Task
import com.route.todoapp.databinding.ItemTaskBinding
import com.route.todoapp.ui.home.tasksList.TaskDiffUtil
import com.route.todoapp.utils.formatTaskTime

class TasksAdapter(var tasksList: List<Task>? = null) : Adapter<TasksAdapter.TasksViewHolder>() {

    class TasksViewHolder(val itemTasksBinding: ItemTaskBinding) : ViewHolder(itemTasksBinding.root) {

        private fun updateTaskStatus(isDone: Boolean) {
            if (isDone) {
                itemTasksBinding.draggingBar.setImageResource(R.drawable.dragging_bar_done)
                itemTasksBinding.title.setTextColor(Color.parseColor("#61E757"))
                itemTasksBinding.btnTaskIsDone.setBackgroundResource(R.drawable.done)
            } else {
                val blue = ContextCompat.getColor(itemView.context, R.color.blue)
                itemTasksBinding.title.setTextColor(blue)
                itemTasksBinding.draggingBar.setImageResource(R.drawable.dragging_bar)
                itemTasksBinding.btnTaskIsDone.setBackgroundResource(R.drawable.check_mark)
            }
        }

        fun bind(task: Task?) {
            itemTasksBinding.title.text = task?.title.toString()
            itemTasksBinding.time.text = task?.time?.formatTaskTime()
            updateTaskStatus(task?.isDone ?: false)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        return TasksViewHolder(ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun getItemCount(): Int = tasksList?.size ?: 0

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        val task = tasksList?.get(position)!!
        holder.bind(task)

        onDoneClickListener?.let {
            holder.itemTasksBinding.btnTaskIsDone.setOnClickListener {
                onDoneClickListener?.onItemClick(position, task)
            }
        }
        onTaskClickListener?.let {
            holder.itemTasksBinding.dragItem.setOnClickListener {
                onTaskClickListener?.onItemClick(position, task)
            }
        }
        onDeleteClickListener?.let {
            holder.itemTasksBinding.leftView.setOnClickListener {
                onDeleteClickListener?.onItemClick(position, task)
            }
        }

    }

    fun updateList(tasks: List<Task>) {
        val diffUtil = TaskDiffUtil(oldList = tasksList ?: emptyList(), newList = tasks)
        val diffResult = DiffUtil.calculateDiff(diffUtil)
        tasksList = tasks
        diffResult.dispatchUpdatesTo(this)
    }

    fun updateTask(task: Task, position: Int) {
        this.tasksList?.toMutableList()?.set(position, task)
        notifyItemChanged(position)
    }

    var onDoneClickListener: OnItemClickListener? = null
    var onTaskClickListener: OnItemClickListener? = null
    var onDeleteClickListener: OnItemClickListener? = null

    fun interface OnItemClickListener {
        fun onItemClick(position: Int, task: Task)
    }

}

