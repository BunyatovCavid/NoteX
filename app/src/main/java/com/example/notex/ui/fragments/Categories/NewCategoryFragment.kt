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
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.ManagedCategoryModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentNewCategoryBinding
import com.example.notex.viewmodels.CategoryViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewCategoryFragment : Fragment(R.layout.fragment_new_category) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private var _binding:FragmentNewCategoryBinding?= null
    private val binding get() =_binding

    private val categoryViewModel:CategoryViewModel by viewModels()
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
           binding?.let { setOnClick(it.deleteButton1, it.categoryCheck1, 1, R.drawable.btn4)}
        }

        binding?.deleteButton2?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton2, it.categoryCheck2, 2, R.drawable.btn4)}
        }

        binding?.deleteButton3?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton3, it.categoryCheck3, 3, R.drawable.btn4)}
        }

        binding?.deleteButton4?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton4, it.categoryCheck4, 4, R.drawable.btn4)}
        }

        binding?.deleteButton5?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton5, it.categoryCheck5, 5, R.drawable.btn4)}
        }

        binding?.deleteButton6?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton6, it.categoryCheck6, 6, R.drawable.btn4)}
        }

        binding?.deleteButton7?.setOnClickListener{
            binding?.let { setOnClick(it.deleteButton7, it.categoryCheck7, 7, R.drawable.btn4)}
        }

    }


    private fun setOnClick(button: Button, linearLayout: LinearLayout, id:Int, drawable: Int){
            linearLayout.visibility = View.GONE
            button.setBackgroundResource(drawable)
            managedCategory.find { it.id==id }?.IsActiveBoolean =false
    }



    private fun managedFieldAreas() {
        for (item in managedCategory) {
            if (!item.IsActiveBoolean) {
                val view = getFieldLayoutViewById(item.id)
                view?.visibility = View.VISIBLE
                item.IsActiveBoolean = true
                break
            }
        }

        val allActive = managedCategory.all { it.IsActiveBoolean }

        if (allActive) {
            binding?.addButton?.setBackgroundResource(R.drawable.btn3)
        }
    }

    private fun getFieldLayoutViewById(id: Int): View? {
        return when (id) {
            1 -> binding?.categoryCheck1
            2 -> binding?.categoryCheck2
            3 -> binding?.categoryCheck3
            4 -> binding?.categoryCheck4
            5 -> binding?.categoryCheck5
            6 -> binding?.categoryCheck6
            7 -> binding?.categoryCheck7
            else -> null
        }
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

    private fun setField(editText: EditText, spinner: Spinner, ):specialField?
    {
        var response: specialField? = null
        if(editText.text?.isBlank()==false)
        {
            response = specialField()
            response.title = editText.text.toString()
            response.datatype = spinner.selectedItem.toString()
        }
        return response
    }

    private fun addCategory(){
        var title:String=binding?.categoryTitlenew?.text.toString()
        var categoryModel = CategoryModel()
        try {
            if (!title.all { it == '.' || it.isWhitespace() }) {
                categoryModel.title = title
                if (binding?.categoryDescriptionnew?.text.toString().isNotBlank())
                    categoryModel.description = binding?.categoryDescriptionnew?.text.toString()
                else
                    categoryModel.description = "None"

                categoryModel.field1 = binding?.let {setField(it.categoryCheck1Title, it.spinnerCheck1)}
                categoryModel.field2 = binding?.let {setField(it.categoryCheck2Title, it.spinnerCheck2)}
                categoryModel.field3 = binding?.let {setField(it.categoryCheck3Title, it.spinnerCheck3)}
                categoryModel.field4 = binding?.let {setField(it.categoryCheck4Title, it.spinnerCheck4)}
                categoryModel.field5 = binding?.let {setField(it.categoryCheck5Title, it.spinnerCheck5)}
                categoryModel.field6 = binding?.let {setField(it.categoryCheck6Title, it.spinnerCheck6)}
                categoryModel.field7 = binding?.let {setField(it.categoryCheck7Title, it.spinnerCheck7)}

                categoryViewModel.addCategory("Categories", categoryModel)
            } else {
                context?.toast("The category name cannot consist only of dots and spaces.")
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            Toast.makeText(context, "An error occurred while adding the category.", Toast.LENGTH_SHORT).show()
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}