package com.example.siriusapp

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.siriusapp.databinding.ActivityMainBinding
import com.example.siriusapp.model.Settings

private lateinit var binding: ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private var pref: SharedPreferences? = null
    private var ACCENT_ARG: String = "ACCENT_ARG"
    private var DIFF_ARG: String = "DIFF_ARG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        pref = getSharedPreferences("Table", Context.MODE_PRIVATE)

        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        val controller = (supportFragmentManager.findFragmentById(R.id.container)
                as NavHostFragment).navController
        val navView = binding.navView
        navView.setupWithNavController(controller)
        readSettings()
    }

    override fun onDestroy() {
        super.onDestroy()
        saveSettings()
    }

    private fun readSettings() {
        Settings.accent = pref?.getString(ACCENT_ARG, "uk").toString()
        Settings.isHardDiff = pref?.getBoolean(DIFF_ARG, true)!!
    }

    internal fun saveSettings(){
        val editor = pref?.edit()
        editor?.putString(ACCENT_ARG, Settings.accent)
        editor?.putBoolean(DIFF_ARG, Settings.isHardDiff)
        editor?.apply()
    }
}