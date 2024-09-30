package com.example.notex.ui.fragments.SpecialNotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentUseCategoryBinding
import com.example.notex.viewmodels.CategoryViewModel
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UseCategoryFragment : Fragment(R.layout.fragment_use_category) {

    private var _binding:FragmentUseCategoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var nav:replaceFragments

    private lateinit var options:MutableList<String>
    private lateinit var backupDatas: MutableList<CategoryModel>

    var userId = FirebaseAuth.getInstance().currentUser?.uid

    private val categoryViewModel: CategoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUseCategoryBinding.inflate(inflater, container, false)

        val datas = arguments?.getParcelableArrayList<CategoryModel>("categories")

        backupDatas = mutableListOf<CategoryModel>()

        datas?.forEach {item->
            backupDatas.add(item)
        }
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
                if(selectedItemPosition!=0) {
                    categoryViewModel.getCategoryByDocument(
                        "Categories",
                        backupDatas.get(selectedItemPosition - 1) ?: CategoryModel()
                    )
                }
                categoryViewModel.documentResult.observe(viewLifecycleOwner, {result->
                    var bundle = Bundle()
                    bundle.putParcelable("category",result)
                    nav.replace(this , R.id.action_useCategoryFragment_to_newSpeacialNote,bundle)
                })

            }
            else
            {
                context?.toast("You must select a category")
            }
        }


        binding.withoutButton.setOnClickListener{
            var result = CategoryModel("WithoutCategory", "WithoutCategory",
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                specialField("", datatype = CostumeDataType.Sentence.toString()),
                userId.toString()
            )

            var bundle = Bundle()
            bundle.putParcelable("category",result)
            nav.replace(this , R.id.action_useCategoryFragment_to_newSpeacialNote,bundle)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home->{
                nav.replace(this, R.id.action_useCategoryFragment_to_homeFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

}