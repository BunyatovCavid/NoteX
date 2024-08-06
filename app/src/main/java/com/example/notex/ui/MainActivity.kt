package com.example.notex.ui

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.example.notex.Independents.BaseDatas
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.databinding.ActivityMainBinding
import com.example.notex.ui.fragments.LoginFragment
import com.example.notex.ui.fragments.RegisterFragment
import com.example.notex.ui.fragments.SetUpProfileFragment
import com.example.notex.ui.fragments.WelcomingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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


        replaceFragment(WelcomingFragment())

        binding.bottomNav.setOnClickListener{
            when(it.id)
            {
                R.id.home -> replaceFragment(SetUpProfileFragment())
            }
        }

    }

    private  fun replaceFragment(fragment: Fragment){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.container,fragment)
        transaction.commit()
    }



}