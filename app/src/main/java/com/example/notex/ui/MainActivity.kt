package com.example.notex.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.notex.R
import com.example.notex.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.homeFragment, R.id.registerFragment, R.id.welcomingFragment,
            R.id.forgotPasswordFragment, R.id.setUpProfileFragment, R.id.onBoardingFragment, R.id.noteFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)

        binding.bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.noteFragment -> {
                    navController.navigate(R.id.noteFragment)
                    true
                }
                R.id.homeFragment -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.noteFragment, R.id.newNoteFragment, R.id.updateNoteFragment -> {
                    binding.toolbar.visibility = View.VISIBLE
                }
                else -> {
                    binding.toolbar.visibility = View.INVISIBLE
                }
            }
        }

    }

}


