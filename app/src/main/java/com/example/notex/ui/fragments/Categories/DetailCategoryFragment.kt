package com.example.notex.ui.fragments.Categories

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.adapters.CategorieAdapter
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentDetailCategoryBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.categoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class DetailCategoryFragment : Fragment(R.layout.fragment_detail_category) {

    private var _binding:FragmentDetailCategoryBinding? = null
    private val binding get()= _binding!!
    private val args : DetailCategoryFragmentArgs by navArgs()
    private lateinit var currentCategory: CategoryModel

    private  lateinit var nav:replaceFragments

    private val categoryViewModel: categoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setViewField(textViewTitle: TextView, textViewSpinner:TextView, data:specialField?, layout:LinearLayout)
    {
        if(data!=null)
        {
            textViewTitle.setText(data.title)
            textViewSpinner.setText(data.datatype)
        }
        else
            layout.visibility = View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         currentCategory = args.category
         nav = replaceFragments()

        binding.categoryTitlenew.setText(currentCategory.title)
        if(currentCategory.description !=null)
        {
            binding.categoryDescriptionnew.setText(currentCategory.description)
        }
        else
            binding.categoryDescriptionnew.visibility = View.GONE


        setViewField(binding.categoryCheck1Title, binding.textCheck1, currentCategory.field1, binding.categoryCheck1 )
        setViewField(binding.categoryCheck2Title, binding.textCheck2, currentCategory.field2, binding.categoryCheck2 )
        setViewField(binding.categoryCheck3Title, binding.textCheck3, currentCategory.field3, binding.categoryCheck3 )
        setViewField(binding.categoryCheck4Title, binding.textCheck4, currentCategory.field4, binding.categoryCheck4 )
        setViewField(binding.categoryCheck5Title, binding.textCheck5, currentCategory.field5, binding.categoryCheck5 )
        setViewField(binding.categoryCheck6Title, binding.textCheck6, currentCategory.field6, binding.categoryCheck6 )
        setViewField(binding.categoryCheck7Title, binding.textCheck7, currentCategory.field7, binding.categoryCheck7 )

    }


    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Deleting the category will also remove all Special Notes associated with it.")
            setPositiveButton("DELETE") { _, _ ->
                categoryViewModel.deleteCategory("Categories", currentCategory)

                categoryViewModel.deleteResult.observe(viewLifecycleOwner, { result ->
                        nav.replace(this@DetailCategoryFragment, R.id.action_detailCategoryFragment_to_categorieFragment)
                        context.toast(result)
                })
            }
            setNegativeButton("CANCEL", null)
        }.create().show()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.update_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.delete_menu -> {
                deleteNote()
            }
            android.R.id.home->{
                nav.replace(this, R.id.action_detailCategoryFragment_to_categorieFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.GONE
        }
    }

    override fun onStop() {
        super.onStop()
        (activity as? MainActivity)?.let {
            it.findViewById<BottomNavigationView>(R.id.bottomNav).visibility = View.VISIBLE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.categoryCheck1.visibility =View.VISIBLE
        binding.categoryCheck2.visibility =View.VISIBLE
        binding.categoryCheck3.visibility =View.VISIBLE
        binding.categoryCheck4.visibility =View.VISIBLE
        binding.categoryCheck5.visibility =View.VISIBLE
        binding.categoryCheck6.visibility =View.VISIBLE
        binding.categoryCheck7.visibility =View.VISIBLE
        binding.categoryDescriptionnew.visibility =View.VISIBLE
    }




}