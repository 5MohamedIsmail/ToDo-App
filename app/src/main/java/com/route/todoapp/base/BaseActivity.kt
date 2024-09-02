package com.route.todoapp.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<VB : ViewBinding> : AppCompatActivity() {
    private var _binding: VB? = null
    val binding get() = _binding!! // Use backing property to avoid null checks

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = inflateBinding(layoutInflater) // Replace 'inflateBinding' with the appropriate inflate method for your binding class
        setContentView(binding.root)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null // Clear binding to avoid memory leaks
    }

    abstract fun inflateBinding(inflater: LayoutInflater): VB
}