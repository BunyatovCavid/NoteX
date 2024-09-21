package com.example.notex.ui.fragments.SpecialNotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
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

        data =  arguments?.getParcelable<CategoryModel>("category")


        if(data?.field1!=null)
            binding.specialnoteField1.visibility = View.VISIBLE
        else
            binding.specialnoteField1.visibility = View.GONE

        if(data?.field2 !=null)
            binding.specialnoteField2.visibility = View.VISIBLE
        else
            binding.specialnoteField2.visibility = View.GONE

        if(data?.field3 !=null)
            binding.specialnoteField3.visibility = View.VISIBLE
        else
            binding.specialnoteField3.visibility = View.GONE

        if(data?.field4 !=null)
            binding.specialnoteField4.visibility = View.VISIBLE
        else
            binding.specialnoteField4.visibility = View.GONE

        if(data?.field5 !=null)
            binding.specialnoteField5.visibility = View.VISIBLE
        else
            binding.specialnoteField5.visibility = View.GONE

        if(data?.field6 !=null)
            binding.specialnoteField6.visibility = View.VISIBLE
        else
            binding.specialnoteField6.visibility = View.GONE

        if(data?.field7 !=null)
            binding.specialnoteField7.visibility = View.VISIBLE
        else
            binding.specialnoteField7.visibility = View.GONE
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