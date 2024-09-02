package com.route.todoapp.ui.home.tasksList

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.route.todoapp.base.BaseFragment
import com.route.todoapp.database.AppDatabase
import com.route.todoapp.database.model.Task
import com.route.todoapp.databinding.FragmentTasksBinding
import com.route.todoapp.ui.home.tasksList.adapter.TasksAdapter
import com.route.todoapp.ui.taskDetails.TaskDetailsActivity
import com.route.todoapp.utils.Constants
import com.route.todoapp.utils.ignoreTime
import com.route.todoapp.utils.setTaskDate
import java.text.DateFormatSymbols
import java.util.Calendar
import java.util.Locale

class TasksFragment : BaseFragment<FragmentTasksBinding>() {

    private lateinit var adapter: TasksAdapter
    private val selectedDate = Calendar.getInstance().apply { ignoreTime() }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentTasksBinding {
        return FragmentTasksBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCalendarView()
        setupRecyclerView()
        loadTasks()
    }

    private fun setupCalendarView() {
        binding.calendarView.setDateSelected(CalendarDay.today(), true)
        binding.calendarView.setOnDateChangedListener { _, date, _ ->
            selectedDate.setTaskDate(date.year, date.month - 1, date.day)
            loadTasks()
        }
        setupCalendarViewLocale()
    }

    private fun setupCalendarViewLocale() {
        val locale = Locale.getDefault()
        if (locale.language == "ar") {
            binding.calendarView.layoutDirection = View.LAYOUT_DIRECTION_RTL
        } else {
            binding.calendarView.layoutDirection = View.LAYOUT_DIRECTION_LTR
        }
        val formatter = DateFormatSymbols(locale).months
        binding.calendarView.setTitleFormatter { day ->
            formatter[day.month - 1] // Month index starts from 0
            "${formatter[day.month - 1]} ${day.year}"
        }
    }

    override fun onResume() {
        super.onResume()
        loadTasks()
    }

    private fun setupRecyclerView() {
        adapter = TasksAdapter(emptyList())
        binding.rvTasks.adapter = adapter
        adapter.onTaskClickListener = TasksAdapter.OnItemClickListener { _, task -> navigateToTaskDetails(task) }
        adapter.onDeleteClickListener = TasksAdapter.OnItemClickListener { _, task -> deleteTask(task) }
        adapter.onDoneClickListener =
            TasksAdapter.OnItemClickListener { position, task -> toggleTaskDone(task)
                 adapter.updateTask(task, position)
            }
    }

    private fun navigateToTaskDetails(task: Task) {
        val intent = Intent(requireContext(), TaskDetailsActivity::class.java)
        intent.putExtra(Constants.TASK_KEY, task)
        startActivity(intent)
    }

    private fun deleteTask(task: Task) {
        AppDatabase.getInstance(requireContext()).getTasksDao().deleteTask(task)
        loadTasks()
    }

    private fun toggleTaskDone(task: Task) {
        task.isDone = !task.isDone
        AppDatabase.getInstance(requireContext()).getTasksDao().updateTask(task)
    }

    fun loadTasks() {
        if (isDetached) return
        val tasks = AppDatabase
            .getInstance(requireContext())
            .getTasksDao()
            .getTasksByDate(selectedDate.timeInMillis)
            adapter.updateList(tasks)
    }
}

