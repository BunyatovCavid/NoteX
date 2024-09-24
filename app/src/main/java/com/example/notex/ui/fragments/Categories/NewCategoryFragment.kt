package com.example.notex.ui.fragments.Categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RelativeLayout
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.get
import androidx.fragment.app.viewModels
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.ManagedCategoryModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentNewCategoryBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.categoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewCategoryFragment : Fragment(R.layout.fragment_new_category) {

    private var _binding:FragmentNewCategoryBinding?= null
    private val binding get() =_binding

    private val categoryViewModel:categoryViewModel by viewModels()
    private lateinit var managedCategory:MutableList<ManagedCategoryModel>

    private lateinit var nav:replaceFragments


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNewCategoryBinding.inflate(inflater,container,false)
       return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val options = arrayOf(CostumeDataType.Sentence, CostumeDataType.Amount, CostumeDataType.Number)

        nav = replaceFragments()

        val adapter = ArrayAdapter(requireContext(), R.layout.spinnet_item, options)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding?.spinnerCheck1?.adapter = adapter
        binding?.spinnerCheck2?.adapter = adapter
        binding?.spinnerCheck3?.adapter = adapter
        binding?.spinnerCheck4?.adapter = adapter
        binding?.spinnerCheck5?.adapter = adapter
        binding?.spinnerCheck6?.adapter = adapter
        binding?.spinnerCheck7?.adapter = adapter


        managedCategory = mutableListOf<ManagedCategoryModel>(ManagedCategoryModel(1, true),
            ManagedCategoryModel(2, false),
            ManagedCategoryModel(3, false),
            ManagedCategoryModel(4, false),
            ManagedCategoryModel(5, false),
            ManagedCategoryModel(6, false),
            ManagedCategoryModel(7, false),
        )

        binding?.addButton?.setOnClickListener{
            managedFieldAreas()
        }

        binding?.deleteButton1?.setOnClickListener{
            binding?.categoryCheck1?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==1 }?.IsActiveBoolean = false
        }


        binding?.deleteButton2?.setOnClickListener{
            binding?.categoryCheck2?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==2 }?.IsActiveBoolean = false
        }

        binding?.deleteButton3?.setOnClickListener{
            binding?.categoryCheck3?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==3 }?.IsActiveBoolean = false
        }

        binding?.deleteButton4?.setOnClickListener{
            binding?.categoryCheck4?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==4 }?.IsActiveBoolean = false
        }

        binding?.deleteButton5?.setOnClickListener{
            binding?.categoryCheck5?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==5 }?.IsActiveBoolean = false
        }

        binding?.deleteButton6?.setOnClickListener{
            binding?.categoryCheck6?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==6 }?.IsActiveBoolean = false
        }
        binding?.deleteButton7?.setOnClickListener{
            binding?.categoryCheck7?.visibility =View.GONE
            binding?.addButton?.setBackgroundResource(R.drawable.btn4)
            managedCategory.find { it.id==7 }?.IsActiveBoolean = false
        }
    }




    private fun managedFieldAreas(){
        var lastVisibleLinearLayoutId: Int? = 1
        for (item in managedCategory) {
                if(!item.IsActiveBoolean)
                {
                    when(item.id)
                    {
                        1->{
                            binding?.categoryCheck2?.visibility = View.VISIBLE
                            true
                        }
                        2->{
                            binding?.categoryCheck2?.visibility = View.VISIBLE
                            true
                        }
                        3->{
                            binding?.categoryCheck3?.visibility = View.VISIBLE
                            true
                        }
                        4->{
                            binding?.categoryCheck4?.visibility = View.VISIBLE
                            true
                        }
                        5->{
                            binding?.categoryCheck5?.visibility = View.VISIBLE
                            true
                        }
                        6->{
                            binding?.categoryCheck6?.visibility = View.VISIBLE
                            true
                        }
                        7->{
                            binding?.categoryCheck7?.visibility = View.VISIBLE
                            true
                        }
                        else->
                            false
                    }
                    item.IsActiveBoolean = true
                    break
                }

            }

        var check = true
       managedCategory.forEach{item->
           if(item.IsActiveBoolean==false)
               check =false
           else {
               check = true
           }
       }

        if(check)
            binding?.addButton?.setBackgroundResource(R.drawable.btn3)
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.new_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                addCategory()
                nav.replace(this, R.id.action_newCategoryFragment_to_categorieFragment)
            }
            android.R.id.home->{
                nav.replace(this, R.id.action_newCategoryFragment_to_categorieFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addCategory(){
        var title:String=binding?.categoryTitlenew?.text.toString()
        var categoryModel = CategoryModel()

        categoryModel.title = title.toString()
        categoryModel.description = binding?.categoryDescriptionnew?.text.toString()

        if(binding?.categoryCheck1Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck1Title?.text.toString()
            val datatype =  binding?.spinnerCheck1?.selectedItem.toString()
            categoryModel.field1 = specialField(fieldtitle,datatype)
        }
        if(binding?.categoryCheck2Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck2Title?.text.toString()
            val datatype =  binding?.spinnerCheck2?.selectedItem.toString()
            categoryModel.field2 = specialField(fieldtitle, datatype)
        }
        if(binding?.categoryCheck3Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck3Title?.text.toString()
            val datatype =  binding?.spinnerCheck3?.selectedItem.toString()
            categoryModel.field3 = specialField(fieldtitle, datatype)
        }
        if(binding?.categoryCheck4Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck4Title?.text.toString()
            val datatype =  binding?.spinnerCheck4?.selectedItem.toString()
            categoryModel.field4 = specialField(fieldtitle, datatype)
        }
        if(binding?.categoryCheck5Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck5Title?.text.toString()
            val datatype =  binding?.spinnerCheck5?.selectedItem.toString()
            categoryModel.field5 = specialField(fieldtitle, datatype)
        }
        if(binding?.categoryCheck6Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck6Title?.text.toString()
            val datatype =  binding?.spinnerCheck6?.selectedItem.toString()
            categoryModel.field6 = specialField(fieldtitle, datatype)
        }
        if(binding?.categoryCheck7Title?.text?.isBlank()==false)
        {
            val fieldtitle = binding?.categoryCheck7Title?.text.toString()
            val datatype =  binding?.spinnerCheck7?.selectedItem.toString()
            categoryModel.field7 = specialField(fieldtitle, datatype)
        }

        categoryViewModel.addCategory("Categories", categoryModel)
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
        _binding=null
    }


}