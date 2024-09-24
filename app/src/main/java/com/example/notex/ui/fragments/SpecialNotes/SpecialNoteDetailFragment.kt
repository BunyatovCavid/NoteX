package com.example.notex.ui.fragments.SpecialNotes

import android.app.AlertDialog
import android.graphics.drawable.Drawable
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
import androidx.compose.runtime.currentCompositionErrors
import androidx.compose.ui.util.fastCbrt
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.Note
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.data.models.specialField
import com.example.notex.databinding.FragmentSpecialNoteDetailBinding
import com.example.notex.databinding.FragmentUpdateNoteBinding
import com.example.notex.ui.MainActivity
import com.example.notex.ui.fragments.Notes.UpdateNoteFragmentArgs
import com.example.notex.viewmodels.specialNoteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SpecialNoteDetailFragment : Fragment(R.layout.fragment_special_note_detail) {
    private lateinit var nav: replaceFragments

    private val args: SpecialNoteDetailFragmentArgs by navArgs()
    private lateinit var currentNote: SpecialNoteModel
    private val specialViewModel:specialNoteViewModel by viewModels()

    private var _binding: FragmentSpecialNoteDetailBinding? = null
    private val binding get() = _binding!!

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        currentNote = args.specialnote

        updateView(currentNote)

        binding.fabUpdate.setOnClickListener{
            if(binding.specialnoteTitle.isClickable == false) {
                allowClick()
                binding.totalAmount.visibility = View.INVISIBLE

                binding.fabUpdate.setImageResource(R.drawable.baseline_done_24)
            }
            else
            {
                var specialNoteModel = updateNote()
                specialViewModel.updateSpecialNote(specialNoteModel)

                specialViewModel.updateResult.observe(viewLifecycleOwner, {result->
                     updateView(result)
                })
                disableClick()
                binding.totalAmount.visibility =View.VISIBLE

                binding.fabUpdate.setImageResource(R.drawable.baseline_edit_note_24)
            }

        }
    }

    private  fun updateView(data :SpecialNoteModel)
    {
        binding.specialnoteTitle.setText(data.title)

        if(currentNote.specialField1!=null) {
            binding.specialnoteField1.visibility = View.VISIBLE
            binding.specialnoteField1.setText(data.specialField1?.title)
        }else
            binding.specialnoteField1.visibility = View.GONE

        if(currentNote.specialField2 !=null) {
            binding.specialnoteField2.visibility = View.VISIBLE
            binding.specialnoteField2.setText(data.specialField2?.title)
        }else
            binding.specialnoteField2.visibility = View.GONE

        if(currentNote.specialField3 !=null){
            binding.specialnoteField3.visibility = View.VISIBLE
            binding.specialnoteField3.setText(data.specialField3?.title)
        }else
            binding.specialnoteField3.visibility = View.GONE

        if(currentNote.specialField4 !=null) {
            binding.specialnoteField4.visibility = View.VISIBLE
            binding.specialnoteField4.setText(data.specialField4?.title)
        }
        else
            binding.specialnoteField4.visibility = View.GONE

        if(currentNote.specialField5 !=null)
        {
            binding.specialnoteField5.visibility = View.VISIBLE
            binding.specialnoteField5.setText(data.specialField5?.title)
        }
        else
            binding.specialnoteField5.visibility = View.GONE

        if(currentNote.specialField6 !=null) {
            binding.specialnoteField6.visibility = View.VISIBLE
            binding.specialnoteField6.setText(data.specialField6?.title)
        }
        else
            binding.specialnoteField6.visibility = View.GONE

        if(currentNote.specialField7 !=null) {
            binding.specialnoteField7.visibility = View.VISIBLE
            binding.specialnoteField7.setText(data.specialField7?.title)
        }
        else
            binding.specialnoteField7.visibility = View.GONE

        updateEditText()
    }

    private fun updateEditText()
    {
        if(currentNote.specialField1?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField1?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField1.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField1.inputType = InputType.TYPE_CLASS_TEXT

        if(currentNote.specialField2?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField2?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField2.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField2.inputType = InputType.TYPE_CLASS_TEXT


        if(currentNote.specialField3?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField3?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField3.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField3.inputType = InputType.TYPE_CLASS_TEXT


        if(currentNote.specialField4?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField4?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField4.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField4.inputType = InputType.TYPE_CLASS_TEXT


        if(currentNote.specialField5?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField5?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField5.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField5.inputType = InputType.TYPE_CLASS_TEXT


        if(currentNote.specialField6?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField6?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField6.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField6.inputType = InputType.TYPE_CLASS_TEXT


        if(currentNote.specialField7?.datatype.toString() == CostumeDataType.Number.toString()||currentNote.specialField7?.datatype.toString() == CostumeDataType.Amount.toString())
            binding.specialnoteField7.inputType = InputType.TYPE_CLASS_NUMBER or InputType.TYPE_NUMBER_FLAG_SIGNED
        else
            binding.specialnoteField7.inputType = InputType.TYPE_CLASS_TEXT

    }

    private fun updateNote():SpecialNoteModel
    {
        var specialNoteModel = SpecialNoteModel(currentNote.id,"", currentNote.categoryTitle)
        if(binding.specialnoteTitle.visibility== View.VISIBLE)
          specialNoteModel.title = binding.specialnoteTitle.text.toString()

        if(binding.specialnoteField1.visibility == View.VISIBLE)
            specialNoteModel.specialField1 = currentNote.specialField1?.let { specialField(binding.specialnoteField1.text.toString(), it.datatype) }

        if(binding.specialnoteField2.visibility == View.VISIBLE)
            specialNoteModel.specialField2 = currentNote.specialField2?.let { specialField(binding.specialnoteField2.text.toString(), it.datatype) }

        if(binding.specialnoteField3.visibility == View.VISIBLE)
            specialNoteModel.specialField3 = currentNote.specialField3?.let { specialField(binding.specialnoteField3.text.toString(), it.datatype) }

        if(binding.specialnoteField4.visibility == View.VISIBLE)
            specialNoteModel.specialField4 = currentNote.specialField4?.let { specialField(binding.specialnoteField4.text.toString(), it.datatype) }

        if(binding.specialnoteField5.visibility == View.VISIBLE)
            specialNoteModel.specialField5 = currentNote.specialField5?.let { specialField(binding.specialnoteField5.text.toString(), it.datatype) }

        if(binding.specialnoteField6.visibility == View.VISIBLE)
            specialNoteModel.specialField6 = currentNote.specialField6?.let { specialField(binding.specialnoteField6.text.toString(), it.datatype) }

        if(binding.specialnoteField7.visibility == View.VISIBLE)
            specialNoteModel.specialField7 = currentNote.specialField7?.let { specialField(binding.specialnoteField7.text.toString(), it.datatype) }

        return specialNoteModel
    }

    private fun SumTotalAmount()
    {

    }

    private fun allowClick(){

        if(binding.specialnoteTitle.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteTitle
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField1
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField2
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField3
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField4
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField5
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField6
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField7
            editText.isFocusableInTouchMode = true
            editText.isFocusable = true
            editText.isClickable = true
            editText.isCursorVisible = true
            editText.isLongClickable = true
        }
    }

    private fun disableClick(){
        if(binding.specialnoteTitle.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteTitle
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }

        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField1
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField2
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField3
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField4
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField5
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField6
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
        if(binding.specialnoteField1.visibility == View.VISIBLE)
        {
            var editText = binding.specialnoteField7
            editText.isFocusableInTouchMode = false
            editText.isFocusable = false
            editText.isClickable = false
            editText.isCursorVisible = false
            editText.isLongClickable = false
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                specialViewModel.deleteSpecialNote(currentNote)
                view?.findNavController()?.navigate(R.id.action_specialNoteDetailFragment_to_homeFragment,
                    null,
                    NavOptions.Builder().setPopUpTo(R.id.noteFragment, true).build()
                )
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


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



}