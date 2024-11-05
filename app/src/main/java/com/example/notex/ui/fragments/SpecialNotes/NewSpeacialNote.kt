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
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentNewSpeacialNoteBinding
import com.example.notex.viewmodels.SpecialNoteViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewSpeacialNote : Fragment(R.layout.fragment_new_speacial_note) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private var _binding:FragmentNewSpeacialNoteBinding?=null
    private val binding get()= _binding

    private val specialNoteViewModel: SpecialNoteViewModel by viewModels()

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
        try {
            val fields = listOf(
                data?.field1 to binding?.specialnoteField1,
                data?.field2 to binding?.specialnoteField2,
                data?.field3 to binding?.specialnoteField3,
                data?.field4 to binding?.specialnoteField4,
                data?.field5 to binding?.specialnoteField5,
                data?.field6 to binding?.specialnoteField6,
                data?.field7 to binding?.specialnoteField7
            )

            fields.forEach { (field, editText) ->
                if (field != null) {
                    editText?.visibility = View.VISIBLE
                    editText?.setHint(field.title)
                } else {
                    editText?.visibility = View.GONE
                }
            }

            updateEditText()

        } catch (e: Exception) {
            crashlytics.recordException(e)
            context?.toast("Error updating view: ${e.message}")
        }
    }


    private fun updateEditText() {
        try {
            val fields = listOf(
                data?.field1 to binding?.specialnoteField1,
                data?.field2 to binding?.specialnoteField2,
                data?.field3 to binding?.specialnoteField3,
                data?.field4 to binding?.specialnoteField4,
                data?.field5 to binding?.specialnoteField5,
                data?.field6 to binding?.specialnoteField6,
                data?.field7 to binding?.specialnoteField7
            )

            fields.forEach { (field, editText) ->
                when (field?.datatype) {
                    CostumeDataType.Number.toString() -> {
                        editText?.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
                    }
                    CostumeDataType.Amount.toString() -> {
                        editText?.inputType = InputType.TYPE_CLASS_NUMBER or
                                InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
                    }
                    else -> {
                        editText?.inputType = InputType.TYPE_CLASS_TEXT
                    }
                }
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            context?.toast("Error updating edit text: ${e.message}")
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
                saveNote()
            }
            android.R.id.home->{
                nav.replace(this, R.id.action_newSpeacialNote_to_useCategoryFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveNote() {
        try {
            val title = binding?.specialnoteTitle
            val fields = listOf(
                binding?.specialnoteField1 to data?.field1,
                binding?.specialnoteField2 to data?.field2,
                binding?.specialnoteField3 to data?.field3,
                binding?.specialnoteField4 to data?.field4,
                binding?.specialnoteField5 to data?.field5,
                binding?.specialnoteField6 to data?.field6,
                binding?.specialnoteField7 to data?.field7
            )

            if (data == null) {
                context?.toast("Category is null")
                nav.replace(this, R.id.action_newSpeacialNote_to_homeFragment)
                return
            }

            if (title?.text.toString().isBlank()) {
                context?.toast("You must write Title")
                return
            }

            val specialNote = SpecialNoteModel().apply {
                this.title = title?.text.toString()
                this.categoryTitle = data!!.title
            }

            fields.forEachIndexed { index, (editText, field) ->
                if (editText?.visibility == View.VISIBLE && field != null) {
                    val fieldValue = specialField(editText?.text.toString(), field.datatype)
                    when (index) {
                        0 -> specialNote.specialField1 = fieldValue
                        1 -> specialNote.specialField2 = fieldValue
                        2 -> specialNote.specialField3 = fieldValue
                        3 -> specialNote.specialField4 = fieldValue
                        4 -> specialNote.specialField5 = fieldValue
                        5 -> specialNote.specialField6 = fieldValue
                        6 -> specialNote.specialField7 = fieldValue
                    }
                }
            }

            specialNoteViewModel.addspecialNote(data!!.title, specialNote)
            nav.replace(this, R.id.action_newSpeacialNote_to_homeFragment)

        } catch (e: Exception) {
            crashlytics.recordException(e)
            context?.toast("Error saving note: ${e.message}")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}