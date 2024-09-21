package com.example.notex.ui.fragments.SpecialNotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.databinding.FragmentUseCategoryBinding
import com.example.notex.viewmodels.categoryViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UseCategoryFragment : DialogFragment(R.layout.fragment_use_category) {

    private var _binding:FragmentUseCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var nav:replaceFragments

    private lateinit var options:MutableList<String>
    private var datas: ArrayList<CategoryModel>?=null

    private val categoryViewModel: categoryViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUseCategoryBinding.inflate(inflater, container, false)

        datas =  arguments?.getParcelableArrayList<CategoryModel>("categories")

        options = mutableListOf<String>()

        options.add("Please Select Category")
        datas?.forEach{item->
             options.add(item.title)
        }

          val adapter = ArrayAdapter(requireContext(), R.layout.use_spinner_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCheck.adapter = adapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val spinner = binding.spinnerCheck
        nav = replaceFragments()

        binding.useButton.setOnClickListener{
            val selectedItemPosition = spinner.selectedItemPosition

            if(selectedItemPosition !=0)
            {
                val selectedCategory = options[selectedItemPosition]
                if(selectedItemPosition!=0) {
                    categoryViewModel.getCategoryByDocument(
                        "Categories",
                        datas?.get(selectedItemPosition - 1) ?: CategoryModel()
                    )
                }
                categoryViewModel.documentResult.observe(viewLifecycleOwner, {result->
                    var bundle = Bundle()
                    bundle.putParcelable("category",result)
                    nav.replace(this , R.id.action_homeFragment_to_newSpeacialNote,bundle)
                    dismiss()
                })

            }
            else
            {
                Toast.makeText(context, "You must select a category", Toast.LENGTH_LONG).show()
            }
        }

    }



}