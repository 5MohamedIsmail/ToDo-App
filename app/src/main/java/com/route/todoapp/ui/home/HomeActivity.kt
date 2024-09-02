package com.route.todoapp.ui.home

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.route.todoapp.R
import com.route.todoapp.base.BaseActivity
import com.route.todoapp.databinding.ActivityHomeBinding
import com.route.todoapp.ui.home.addTask.AddTaskBottomSheetFragment
import com.route.todoapp.ui.home.settings.SettingsFragment
import com.route.todoapp.ui.home.tasksList.TasksFragment
import java.util.Locale

class HomeActivity: BaseActivity<ActivityHomeBinding>() {

    private var tasksFragment: TasksFragment? = null

    override fun inflateBinding(inflater: LayoutInflater): ActivityHomeBinding {
        return ActivityHomeBinding.inflate(inflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupPreferences()
        setupBottomNavigation()
        setupFabListener()
    }

    private fun setupPreferences() {
        val sharedPreferences = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
        applyLanguage(sharedPreferences.getString("language", "en"))
        applyAppMode(sharedPreferences.getString("mode", "Light") ?: "Light")
        binding.bottomNavigation.selectedItemId = sharedPreferences.getInt("selected_item_id", R.id.navigation_tasks)
    }

    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_tasks -> {
                    tasksFragment = TasksFragment()
                    pushFragment(tasksFragment!!)
                }
                R.id.navigation_settings -> pushFragment(SettingsFragment())
            }
            return@setOnItemSelectedListener true
        }
        binding.bottomNavigation.selectedItemId = R.id.navigation_tasks
    }

    private fun pushFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    private fun setupFabListener() {
        binding.fabAddTask.setOnClickListener {
            showAddTaskBottomSheet()
        }
    }

    private fun showAddTaskBottomSheet() {
        val addTaskSheet = AddTaskBottomSheetFragment()
        addTaskSheet.taskAddedListener = AddTaskBottomSheetFragment.OnTaskAddedListener {
            tasksFragment?.loadTasks()
        }
        addTaskSheet.show(supportFragmentManager, null)
    }

    private fun applyLanguage(languageCode: String?) {
        val locale = Locale(languageCode!!)
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)
    }

    private fun applyAppMode(mode: String) {
        when (mode) {
            "Dark" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            "Light" -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Update selected Item Id after activity recreate
    fun updateSelectedItemId(itemId: Int) {
        binding.bottomNavigation.selectedItemId = itemId
    }

}