package com.example.sirius_app

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.sirius_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    public val PACKAGE_NAME = applicationContext.packageName

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Data.PACKAGE_NAME = applicationContext.packageName
    }
}
