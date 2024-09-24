package com.example.notex.ui.fragments.SpecialNotes

import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.ManagedCategoryModel
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentNewSpeacialNoteBinding
import com.example.notex.viewmodels.categoryViewModel
import com.example.notex.viewmodels.specialNoteViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewSpeacialNote : Fragment(R.layout.fragment_new_speacial_note) {

    private var _binding:FragmentNewSpeacialNoteBinding?=null
    private val binding get()= _binding!!

    private val specialNoteViewModel: specialNoteViewModel by viewModels()
    private val categoryViewModel: categoryViewModel by viewModels()

    private var data:CategoryModel?=null


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
        _binding = FragmentNewSpeacialNoteBinding.inflate(inflater,container,false)

        nav = replaceFragments()

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backupData =  arguments?.getParcelable<CategoryModel>("category")
        data = backupData

        updateView()
    }

    private  fun updateView()
    {
        if(data?.field1!=null) {
            binding.specialnoteField1.visibility = View.VISIBLE
            binding.specialnoteField1.setHint(data?.field1?.title)
        }else
            binding.specialnoteField1.visibility = View.GONE

        if(data?.field2 !=null) {
            binding.specialnoteField2.visibility = View.VISIBLE
            binding.specialnoteField2.setHint(data?.field2?.title)
        }else
            binding.specialnoteField2.visibility = View.GONE

        if(data?.field3 !=null){
            binding.specialnoteField3.visibility = View.VISIBLE
            binding.specialnoteField3.setHint(data?.field3?.title)
        }else
            binding.specialnoteField3.visibility = View.GONE

        if(data?.field4 !=null) {
            binding.specialnoteField4.visibility = View.VISIBLE
            binding.specialnoteField4.setHint(data?.field4?.title)
        }
        else
            binding.specialnoteField4.visibility = View.GONE

        if(data?.field5 !=null)
        {
            binding.specialnoteField5.visibility = View.VISIBLE
            binding.specialnoteField5.setHint(data?.field5?.title)
        }
        else
            binding.specialnoteField5.visibility = View.GONE

        if(data?.field6 !=null) {
            binding.specialnoteField6.visibility = View.VISIBLE
            binding.specialnoteField6.setHint(data?.field6?.title)
        }
        else
            binding.specialnoteField6.visibility = View.GONE

        if(data?.field7 !=null) {
            binding.specialnoteField7.visibility = View.VISIBLE
            binding.specialnoteField7.setHint(data?.field7?.title)
        }
        else
            binding.specialnoteField7.visibility = View.GONE


        updateEditText()
    }


    private fun updateEditText()
    {
        if(data?.field1?.datatype.toString() == CostumeDataType.Number.toString()||data?.field1?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField1.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField1.inputType = InputType.TYPE_CLASS_TEXT

        if(data?.field2?.datatype.toString()== CostumeDataType.Number.toString()||data?.field2?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField2.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField2.inputType = InputType.TYPE_CLASS_TEXT


        if(data?.field3?.datatype.toString() == CostumeDataType.Number.toString()||data?.field3?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField3.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField3.inputType = InputType.TYPE_CLASS_TEXT


        if(data?.field4?.datatype.toString() == CostumeDataType.Number.toString()||data?.field4?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField4.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField4.inputType = InputType.TYPE_CLASS_TEXT


        if(data?.field5?.datatype.toString() == CostumeDataType.Number.toString()||data?.field5?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField5.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField5.inputType = InputType.TYPE_CLASS_TEXT


        if(data?.field6?.datatype.toString() == CostumeDataType.Number.toString()||data?.field6?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField6.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField6.inputType = InputType.TYPE_CLASS_TEXT


        if(data?.field7?.datatype.toString() == CostumeDataType.Number.toString()||data?.field7?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField7.inputType = InputType.TYPE_CLASS_NUMBER
        else
            binding.specialnoteField7.inputType = InputType.TYPE_CLASS_TEXT

    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.new_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.save_menu -> {
                saveNote()
            }
            android.R.id.home->{
                nav.replace(this, R.id.action_newSpeacialNote_to_useCategoryFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        var title = binding.specialnoteTitle
        var field1 = binding.specialnoteField1
        var field2 = binding.specialnoteField2
        var field3 = binding.specialnoteField3
        var field4 = binding.specialnoteField4
        var field5 = binding.specialnoteField5
        var field6 = binding.specialnoteField6
        var field7 = binding.specialnoteField7

        var specialNote = SpecialNoteModel()

        if(data!=null) {
            if (title.text.isNotBlank()) {
                specialNote.title = title.text.toString()
                specialNote.categoryTitle = data!!.title


                if (field1.visibility == View.VISIBLE)
                    specialNote.specialField1 =
                        data?.field1?.let { specialField(field1.text.toString(), it.datatype) }
                if (field2.visibility == View.VISIBLE)
                    specialNote.specialField2 =
                        data?.field2?.let { specialField(field2.text.toString(), it.datatype) }
                if (field3.visibility == View.VISIBLE)
                    specialNote.specialField3 =
                        data?.field3?.let { specialField(field3.text.toString(), it.datatype) }
                if (field4.visibility == View.VISIBLE)
                    specialNote.specialField4 =
                        data?.field4?.let { specialField(field4.text.toString(), it.datatype) }
                if (field5.visibility == View.VISIBLE)
                    specialNote.specialField5 =
                        data?.field5?.let { specialField(field5.text.toString(), it.datatype) }
                if (field6.visibility == View.VISIBLE)
                    specialNote.specialField6 =
                        data?.field6?.let { specialField(field6.text.toString(), it.datatype) }
                if (field7.visibility == View.VISIBLE)
                    specialNote.specialField7 =
                        data?.field7?.let { specialField(field7.text.toString(), it.datatype) }


                specialNoteViewModel.addspecialNote(data!!.title, specialNote)
                nav.replace(this, R.id.action_newSpeacialNote_to_homeFragment)
            } else
                Toast.makeText(context, "You must write Title", Toast.LENGTH_SHORT).show()
        }
        else
        {
            nav.replace(this, R.id.action_newSpeacialNote_to_homeFragment)
            Toast.makeText(context, "Category is null", Toast.LENGTH_LONG).show()
        }
    }


}