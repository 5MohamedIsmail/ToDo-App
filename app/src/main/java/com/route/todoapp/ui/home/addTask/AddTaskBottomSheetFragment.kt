package com.route.todoapp.ui.home.addTask

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.route.todoapp.R
import com.route.todoapp.database.AppDatabase
import com.route.todoapp.database.model.Task
import com.route.todoapp.databinding.FragmentAddTaskBinding
import com.route.todoapp.utils.formatTaskDate
import com.route.todoapp.utils.formatTaskTime
import com.route.todoapp.utils.ignoreDate
import com.route.todoapp.utils.ignoreTime
import com.route.todoapp.utils.setTaskDate
import com.route.todoapp.utils.setTaskTime
import java.util.Calendar

class AddTaskBottomSheetFragment: BottomSheetDialogFragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private val time = Calendar.getInstance().apply { ignoreDate() }
    private val date = Calendar.getInstance().apply { ignoreTime() }
    var taskAddedListener: OnTaskAddedListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
       setupClickListeners()
    }

    private fun setupClickListeners() {
        binding.selectDateTv.setOnClickListener { showDatePicker() }
        binding.selectTimeTv.setOnClickListener { showTimePicker() }
        binding.addTaskBtn.setOnClickListener { addTask() }
    }

    private fun isValidForm(): Boolean {
        var isValid = true

        if (binding.title.text.toString().isBlank()) {
            isValid = false
            binding.titleTil.error = "Please enter title."
        } else {
            binding.titleTil.error = null
        }

        if (binding.description.text.toString().isBlank()) {
            isValid = false
            binding.descriptionTil.error = "Please enter description."
        } else {
            binding.descriptionTil.error = null
        }

        if (binding.selectDateTv.text.toString().isBlank()) {
            isValid = false
            binding.selectDateTil.error = "Please select date."
        } else {
            binding.selectDateTil.error = null
        }

        if (binding.selectTimeTv.text.toString().isBlank()) {
            isValid = false
            binding.selectTimeTil.error = "Please select time."
        } else {
            binding.selectTimeTil.error = null
        }

        return isValid
    }

    private fun addTask() {
        if(!isValidForm()) return

        AppDatabase.getInstance(requireContext())
            .getTasksDao()
            .createTask(Task(
                title = binding.title.text.toString(),
                description = binding.description.text.toString(),
                date = date.timeInMillis,
                time = time.timeInMillis,
            ))
            showSuccessMessage()
        taskAddedListener?.taskAdded()
    }

    private fun showSuccessMessage() {
        val builder = AlertDialog.Builder(requireContext())
            .setMessage("Task added successfully")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                dismiss()
            }
                .setCancelable(false)
        builder.show()
    }

    private fun showTimePicker() {
        TimePickerDialog(
            requireContext(), R.style.CustomTimePickerDialog,
            { _, hourOfDay, minute ->
                time.setTaskTime(hourOfDay, minute)
                binding.selectTimeTv.text = time.formatTaskTime()
            },
            time.get(Calendar.HOUR_OF_DAY),
            time.get(Calendar.MINUTE),
            false
        )
            .show()
    }

    private fun showDatePicker() {
        DatePickerDialog(
            requireContext(), R.style.CustomTimePickerDialog,
            { _, year, month, dayOfMonth ->
                date.setTaskDate(year, month, dayOfMonth)
                binding.selectDateTv.text = date.formatTaskDate()
            },
            date.get(Calendar.YEAR),
            date.get(Calendar.MONTH),
            date.get(Calendar.DAY_OF_MONTH),
        )
            .show()
    }

    fun interface OnTaskAddedListener {
        fun taskAdded()
    }
}