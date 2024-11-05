package com.example.notex.ui.fragments.SpecialNotes

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentSpecialNoteDetailBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.SpecialNoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialNoteDetailFragment : Fragment(R.layout.fragment_special_note_detail) {
    private lateinit var nav: replaceFragments

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()


    private val args: SpecialNoteDetailFragmentArgs by navArgs()
    private lateinit var currentNote: SpecialNoteModel
    private val specialViewModel:SpecialNoteViewModel by viewModels()

    private var _binding: FragmentSpecialNoteDetailBinding? = null
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSpecialNoteDetailBinding.inflate(inflater, container, false)

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentNote = args.specialnote

        updateView(currentNote)

        nav = replaceFragments()

        binding?.fabUpdate?.setOnClickListener{
            if(binding?.specialnoteTitle?.isClickable == false) {
                configureEditTexts()
                binding?.totalAmount?.visibility = View.INVISIBLE
                binding?.fabUpdate?.setImageResource(R.drawable.baseline_done_24)
            }
            else
            {
                var specialNoteModel = updateNote()
                specialViewModel.updateSpecialNote(specialNoteModel)

                specialViewModel.updateResult.observe(viewLifecycleOwner, {result->
                     updateView(result)
                })

                disableEditTexts()
                binding?.totalAmount?.visibility =View.VISIBLE
                binding?.fabUpdate?.setImageResource(R.drawable.baseline_edit_note_24)
            }

        }
    }

    private fun setFieldVisibilityAndText(field: EditText, dataField: specialField?) {
        if (dataField != null) {
            field.visibility = View.VISIBLE
            field.setText(dataField.title)
        } else {
            field.visibility = View.GONE
        }
    }

    private fun updateView(data: SpecialNoteModel) {
        binding?.let {
            it.specialnoteTitle.setText(data.title)
            setFieldVisibilityAndText(it.specialnoteField1, data.specialField1)
            setFieldVisibilityAndText(it.specialnoteField2, data.specialField2)
            setFieldVisibilityAndText(it.specialnoteField3, data.specialField3)
            setFieldVisibilityAndText(it.specialnoteField4, data.specialField4)
            setFieldVisibilityAndText(it.specialnoteField5, data.specialField5)
            setFieldVisibilityAndText(it.specialnoteField6, data.specialField6)
            setFieldVisibilityAndText(it.specialnoteField7, data.specialField7)

            calculateTotalAmount()
            setInputTypes()
        }
    }



    private fun setInputTypeForField(field: EditText, dataType: String?) {
        if(CostumeDataType.Number.toString() == dataType) {
            field.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        else if(CostumeDataType.Amount.toString() == dataType) {
            field.inputType = InputType.TYPE_CLASS_NUMBER or
                    InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_NUMBER_FLAG_SIGNED
        }
        else {
            field.inputType = InputType.TYPE_CLASS_TEXT
        }
    }

    private fun setInputTypes() {
        binding?.let {
            setInputTypeForField(it.specialnoteField1, currentNote.specialField1?.datatype)
            setInputTypeForField(it.specialnoteField2, currentNote.specialField2?.datatype)
            setInputTypeForField(it.specialnoteField3, currentNote.specialField3?.datatype)
            setInputTypeForField(it.specialnoteField4, currentNote.specialField4?.datatype)
            setInputTypeForField(it.specialnoteField5, currentNote.specialField5?.datatype)
            setInputTypeForField(it.specialnoteField6, currentNote.specialField6?.datatype)
            setInputTypeForField(it.specialnoteField7, currentNote.specialField7?.datatype)
        }
    }


    private fun updateNote():SpecialNoteModel
    {
        val specialNoteModel = SpecialNoteModel(currentNote.id, "", currentNote.categoryTitle)

        try {
            if (binding?.specialnoteTitle?.visibility == View.VISIBLE) {
                specialNoteModel.title = binding?.specialnoteTitle?.text.toString()
            }


                binding?.let {
                    val fields = listOf(
                        it.specialnoteField1 to currentNote.specialField1,
                        it.specialnoteField2 to currentNote.specialField2,
                        it.specialnoteField3 to currentNote.specialField3,
                        it.specialnoteField4 to currentNote.specialField4,
                        it.specialnoteField5 to currentNote.specialField5,
                        it.specialnoteField6 to currentNote.specialField6,
                        it.specialnoteField7 to currentNote.specialField7
                    )


                    fields.forEachIndexed { index, (editText, specialField) ->
                        if (editText.visibility == View.VISIBLE) {
                            when (index) {
                                0 -> specialNoteModel.specialField1 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                1 -> specialNoteModel.specialField2 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                2 -> specialNoteModel.specialField3 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                3 -> specialNoteModel.specialField4 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                4 -> specialNoteModel.specialField5 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                5 -> specialNoteModel.specialField6 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }

                                6 -> specialNoteModel.specialField7 = specialField?.let {
                                    specialField(
                                        editText.text.toString(),
                                        it.datatype
                                    )
                                }
                            }
                        }
                    }
                }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            context?.toast("Error updating note: ${e.message}")
        }

        return specialNoteModel
    }

    private fun calculateTotalAmount()
    {
        var totalAmount = 0.0
        var multiplier = 1.0

        for (i in 1..7) {
            val specialField = when (i) {
                1 -> currentNote.specialField1
                2 -> currentNote.specialField2
                3 -> currentNote.specialField3
                4 -> currentNote.specialField4
                5 -> currentNote.specialField5
                6 -> currentNote.specialField6
                7 -> currentNote.specialField7
                else -> null
            }

            if (specialField?.datatype == CostumeDataType.Amount.toString()) {
                totalAmount += binding?.root?.findViewById<EditText>(resources.getIdentifier("specialnoteField$i", "id", context?.packageName))?.text.toString().toDoubleOrNull() ?: 0.0
            }

            if (specialField?.datatype == CostumeDataType.Number.toString()) {
                multiplier *= binding?.root?.findViewById<EditText>(resources.getIdentifier("specialnoteField$i", "id", context?.packageName))?.text.toString().toDoubleOrNull() ?: 1.0
            }
        }

        binding?.totalAmount?.text = (totalAmount * multiplier).toString()
    }

    private fun setEditTextProperties(editText: EditText) {
        editText.isFocusableInTouchMode = true
        editText.isFocusable = true
        editText.isClickable = true
        editText.isCursorVisible = true
        editText.isLongClickable = true
    }

    private fun configureEditTexts() {
        binding?.let {
            if (it.specialnoteTitle.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteTitle)
            }
            if (it.specialnoteField1.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField1)
            }
            if (it.specialnoteField2.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField2)
            }
            if (it.specialnoteField3.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField3)
            }
            if (it.specialnoteField4.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField4)
            }
            if (it.specialnoteField5.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField5)
            }
            if (it.specialnoteField6.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField6)
            }
            if (it.specialnoteField7.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField7)
            }
        }
    }

    private fun setEditTextProperties(editText: EditText, isEnabled: Boolean) {
        editText.isFocusableInTouchMode = isEnabled
        editText.isFocusable = isEnabled
        editText.isClickable = isEnabled
        editText.isCursorVisible = isEnabled
        editText.isLongClickable = isEnabled
    }

    private fun disableEditTexts() {
        binding?.let {
            if (it.specialnoteTitle.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteTitle, false)
            }
            if (it.specialnoteField1.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField1, false)
            }
            if (it.specialnoteField2.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField2, false)
            }
            if (it.specialnoteField3.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField3, false)
            }
            if (it.specialnoteField4.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField4, false)
            }
            if (it.specialnoteField5.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField5, false)
            }
            if (it.specialnoteField6.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField6, false)
            }
            if (it.specialnoteField7.visibility == View.VISIBLE) {
                setEditTextProperties(it.specialnoteField7, false)
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                try {
                    specialViewModel.deleteSpecialNote(currentNote)
                    view?.findNavController()?.navigate(
                        R.id.action_specialNoteDetailFragment_to_homeFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.noteFragment, true).build()
                    )
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    context?.toast("Error deleting note: ${e.message}")
                }
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
                nav.replace(this, R.id.action_specialNoteDetailFragment_to_homeFragment)
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
        _binding = null
    }



}