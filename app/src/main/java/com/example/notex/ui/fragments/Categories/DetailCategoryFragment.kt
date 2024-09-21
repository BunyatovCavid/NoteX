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
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.adapters.CategorieAdapter
import com.example.notex.data.models.CategoryModel
import com.example.notex.databinding.FragmentDetailCategoryBinding
import com.example.notex.viewmodels.categoryViewModel
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

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentDetailCategoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         currentCategory = args.category
         nav = replaceFragments()


        if(currentCategory.field1 !=null)
        {
            binding.categoryCheck1Title.setText(currentCategory?.field1?.title)
            binding.textCheck1.setText(currentCategory.field1?.datatype)
        }
        else
            binding.categoryCheck1.visibility = View.GONE


        if(currentCategory.field2 !=null)
        {
            binding.categoryCheck2Title.setText(currentCategory?.field2?.title)
            binding.textCheck2.setText(currentCategory.field2?.datatype)
        }
        else
            binding.categoryCheck2.visibility = View.GONE


        if(currentCategory.field3 !=null)
        {
            binding.categoryCheck3Title.setText(currentCategory?.field3?.title)
            binding.textCheck3.setText(currentCategory.field3?.datatype)
        }
        else
            binding.categoryCheck3.visibility = View.GONE


        if(currentCategory.field4 !=null)
        {
            binding.categoryCheck4Title.setText(currentCategory?.field4?.title)
            binding.textCheck4.setText(currentCategory.field4?.datatype)
        }
        else
            binding.categoryCheck4.visibility = View.GONE


        if(currentCategory.field5 !=null)
        {
            binding.categoryCheck5Title.setText(currentCategory?.field5?.title)
            binding.textCheck5.setText(currentCategory.field5?.datatype)
        }
        else
            binding.categoryCheck5.visibility = View.GONE


        if(currentCategory.field6 !=null)
        {
            binding.categoryCheck6Title.setText(currentCategory?.field6?.title)
            binding.textCheck6.setText(currentCategory.field6?.datatype)
        }
        else
            binding.categoryCheck6.visibility = View.GONE


        if(currentCategory.field7 !=null)
        {
            binding.categoryCheck7Title.setText(currentCategory?.field7?.title)
            binding.textCheck7.setText(currentCategory.field7?.datatype)
        }
        else
            binding.categoryCheck7.visibility = View.GONE

    }


    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Deleting the category will also remove all Special Notes associated with it.")
            setPositiveButton("DELETE") { _, _ ->
                categoryViewModel.deleteCategory("Categories", currentCategory)

                categoryViewModel.deleteResult.observe(viewLifecycleOwner, { result ->
                    if (result == "Success"){
                        nav.replace(this@DetailCategoryFragment, R.id.action_detailCategoryFragment_to_categorieFragment)
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
                    }
                    else
                        Toast.makeText(context, result, Toast.LENGTH_LONG).show()
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
        }

        return super.onOptionsItemSelected(item)
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
    }




}