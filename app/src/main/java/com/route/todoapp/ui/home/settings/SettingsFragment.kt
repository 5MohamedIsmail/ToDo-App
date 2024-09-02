package com.route.todoapp.ui.home.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate
import com.route.todoapp.R
import com.route.todoapp.base.BaseFragment
import com.route.todoapp.databinding.FragmentSettingsBinding
import com.route.todoapp.ui.home.HomeActivity
import java.util.Locale

class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSettingsBinding {
        return FragmentSettingsBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeSettings()
    }

    override fun onResume() {
        super.onResume()
        initModeDropdown()
    }

    private fun initializeSettings() {
        setupLanguageSelection()
        setupModeSelection()
        displayCurrentLanguage()
    }

    // Display the current language in the dropdown
    private fun displayCurrentLanguage() {
        val currentLanguageCode = sharedPreferences.getString("language", "en")
        val languages = resources.getStringArray(R.array.languages)
        val selectedIndex = languages.indexOf(languageCodeToName(currentLanguageCode))
        binding.autoCompleteTVLanguages.setText(languages[selectedIndex], false)
    }

    // Converts language code to its corresponding name
    private fun languageCodeToName(languageCode: String?): String {
        return when (languageCode) {
            "en" -> "English"
            "ar" -> "Arabic"
            else -> "English"
        }
    }

    private fun setupLanguageSelection() {
        val languages = resources.getStringArray(R.array.languages)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, languages)
        binding.autoCompleteTVLanguages.setAdapter(arrayAdapter)

        binding.autoCompleteTVLanguages.setOnItemClickListener { _, _, position, _ ->
            val selectedLanguage = languages[position]
            applyLanguageChange(selectedLanguage)

            val languageCode = when (selectedLanguage) {
                "English" -> "en"
                "Arabic" -> "ar"
                else -> "en"
            }
            applyLanguageChange(languageCode)
        }
    }

    private fun applyLanguageChange(languageCode: String) {
        saveToPreferences("language", languageCode)

        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = resources.configuration
        config.setLocale(locale)
        requireActivity().resources.updateConfiguration(config, requireActivity().resources.displayMetrics)
        requireActivity().recreate()
        navigateToTasks()
    }

    private fun saveToPreferences(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun setupModeSelection() {
        val modes = resources.getStringArray(R.array.modes)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.drop_down_item, modes)
        binding.autoCompleteTVModes.setAdapter(arrayAdapter)

        val currentMode = sharedPreferences.getString("mode", "Light")
        val selectedIndex = modes.indexOf(currentMode)
        binding.autoCompleteTVModes.setText(modes[selectedIndex], false)

        binding.autoCompleteTVModes.setOnItemClickListener { _, _, position, _ ->
            val selectedMode = modes[position]
            applyModeChange(selectedMode)
        }
    }

    private fun applyModeChange(selectedMode: String) {
        saveToPreferences("mode", selectedMode)
        updateModeIcon(selectedMode)

        val nightMode = if (selectedMode == "Dark") {
            AppCompatDelegate.MODE_NIGHT_YES
        } else {
            AppCompatDelegate.MODE_NIGHT_NO
        }
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity()
        navigateToTasks()
    }

    private fun updateModeIcon(mode: String?) {
        val iconResId = if (mode == "Dark") R.drawable.ic_dark else R.drawable.ic_light_mode
        binding.modeTil.setStartIconDrawable(iconResId)
        binding.modeTil.refreshStartIconDrawableState()
    }

    private fun initModeDropdown() {
        val modes = resources.getStringArray(R.array.modes)
        val currentMode = sharedPreferences.getString("mode", "Light")
        val selectedIndex = modes.indexOf(currentMode)
        binding.autoCompleteTVModes.setText(modes[selectedIndex], false)
        updateModeIcon(currentMode)
    }

    private fun navigateToTasks() {
        (activity as? HomeActivity)?.updateSelectedItemId(R.id.navigation_tasks)
    }
}