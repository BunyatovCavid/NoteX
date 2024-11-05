package com.example.notex.ui

import NetworkReceiver
import android.annotation.SuppressLint
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var networkReceiver: NetworkReceiver

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(binding.toolbar)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        val appBarConfiguration = AppBarConfiguration(setOf(R.id.loginFragment, R.id.homeFragment, R.id.registerFragment, R.id.welcomingFragment,
            R.id.forgotPasswordFragment, R.id.noteFragment, R.id.categorieFragment, R.id.newSpeacialNote, R.id.profileFragment2, R.id.noInternetFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.bottomNav.setupWithNavController(navController)

        if (isUserLoggedIn()) {
            navController.navigate(R.id.homeFragment) // İstifadəçi artıq login olubsa, ana səhifəyə yönləndir
        } else {
            navController.navigate(R.id.welcomingFragment) // Login olmamış istifadəçilər login səhifəsinə yönləndirilsin
        }


        networkReceiver = NetworkReceiver(navController, binding.bottomNav)
        registerReceiver(networkReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))


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
                R.id.categorieFragment->{
                    navController.navigate(R.id.categorieFragment)
                    true
                }
                R.id.profileFragment2->{
                    navController.navigate(R.id.profileFragment2)
                    true
                }
                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.loginFragment, R.id.welcomingFragment, R.id.registerFragment, R.id.forgotPasswordFragment -> {
                    binding.toolbar.visibility = View.GONE
                }
                else -> {
                    binding.toolbar.visibility = View.VISIBLE
                }
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkReceiver) // Receiver'ı unregister et
    }

    // İstifadəçinin login olub-olmadığını yoxlayan funksiya
    private fun isUserLoggedIn(): Boolean {
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        return sharedPref.getBoolean("isLoggedIn", false)
    }


}


