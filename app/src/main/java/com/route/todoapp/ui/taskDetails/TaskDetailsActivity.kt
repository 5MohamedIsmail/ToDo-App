package com.route.todoapp.ui.taskDetails

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.core.content.IntentCompat
import com.route.todoapp.base.BaseActivity
import com.route.todoapp.database.AppDatabase
import com.route.todoapp.database.model.Task
import com.route.todoapp.databinding.ActivityTaskDetailsBinding
import com.route.todoapp.utils.Constants
import com.route.todoapp.utils.formatTaskDate
import com.route.todoapp.utils.formatTaskTime
import com.route.todoapp.utils.ignoreDate
import com.route.todoapp.utils.ignoreTime
import com.route.todoapp.utils.setTaskDate
import com.route.todoapp.utils.setTaskTime
import java.util.Calendar

class TaskDetailsActivity: BaseActivity<ActivityTaskDetailsBinding>() {

    private lateinit var intentTask: Task
    private lateinit var newTask: Task
    private val time: Calendar = Calendar.getInstance().apply { ignoreDate() }
    val date: Calendar = Calendar.getInstance().apply { ignoreTime() }

    override fun inflateBinding(inflater: LayoutInflater): ActivityTaskDetailsBinding {
        return ActivityTaskDetailsBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retrieveIntentTask()
        initView()
        setupToolbar()
        initNewTask()
        setupListeners()
    }

    private fun retrieveIntentTask() {
        intentTask = IntentCompat.getParcelableExtra(intent, Constants.TASK_KEY, Task::class.java) as Task // (as Task) -> Casting from nullable task to normal task
    }

    private fun setupListeners() {
        binding.content.selectDateTil.setOnClickListener { showDatePicker() }
        binding.content.selectTimeTil.setOnClickListener { showTimePicker() }
        binding.content.btnSave.setOnClickListener { saveTask() }
    }

    private fun saveTask() {
        if (!isValidEdit()) return
        newTask.apply {
            title = binding.content.title.text.toString()
            description = binding.content.description.text.toString()
            AppDatabase.getInstance(this@TaskDetailsActivity)
                .getTasksDao()
                .updateTask(newTask)
        }
        finish()
    }

    private fun isValidEdit(): Boolean {
        var isValid = true

        if (binding.content.title.text.toString().isBlank()) {
            isValid = false
            binding.content.titleTil.error = "Please enter title"
        } else {
            binding.content.titleTil.error = null
        }

        if (binding.content.description.text.toString().isBlank()) {
            isValid = false
            binding.content.descriptionTil.error = "Please enter description"
        } else {
            binding.content.descriptionTil.error = null
        }

        return isValid
    }

    private fun showTimePicker() {
        TimePickerDialog(
            this,
            { _, hourOfDay, minute ->
                time.setTaskTime(hourOfDay, minute)
                binding.content.selectTimeTv.text = time.formatTaskTime()
                newTask.time = time.timeInMillis
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            false
        )
            .show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                date.setTaskDate(year, month, dayOfMonth)
                binding.content.selectDateTv.text = date.formatTaskDate()
                newTask.date = date.timeInMillis
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH),
        )
            .show()
    }

    private fun initNewTask() {
        newTask = intentTask.copy()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
    }

    private fun initView() {
        binding.content.title.setText(intentTask.title)
        binding.content.description.setText(intentTask.description)
        binding.content.selectDateTv.text = intentTask.date?.formatTaskDate()
        binding.content.selectTimeTv.text = intentTask.time?.formatTaskTime()
    }
}