package com.example.notex.ui.fragments.SpecialNotes

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.currentCompositionErrors
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.CostumeDataType
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.Note
import com.example.notex.data.models.SpecialNoteModel
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
        currentNote = args.specialnote!!

        binding.specialnoteTitle.setHint(currentNote.title)

        if(currentNote.specialField1!=null) {
            binding.specialnoteField1.visibility = View.VISIBLE
            binding.specialnoteField1.setHint(currentNote.specialField1?.title)
        }else
            binding.specialnoteField1.visibility = View.GONE

        if(currentNote.specialField2 !=null) {
            binding.specialnoteField2.visibility = View.VISIBLE
            binding.specialnoteField2.setHint(currentNote.specialField2?.title)
        }else
            binding.specialnoteField2.visibility = View.GONE

        if(currentNote.specialField3 !=null){
            binding.specialnoteField3.visibility = View.VISIBLE
            binding.specialnoteField3.setHint(currentNote.specialField3?.title)
        }else
            binding.specialnoteField3.visibility = View.GONE

        if(currentNote.specialField4 !=null) {
            binding.specialnoteField4.visibility = View.VISIBLE
            binding.specialnoteField4.setHint(currentNote.specialField4?.title)
        }
        else
            binding.specialnoteField4.visibility = View.GONE

        if(currentNote.specialField5 !=null)
        {
            binding.specialnoteField5.visibility = View.VISIBLE
            binding.specialnoteField5.setHint(currentNote.specialField5?.title)
        }
        else
            binding.specialnoteField5.visibility = View.GONE

        if(currentNote.specialField6 !=null) {
            binding.specialnoteField6.visibility = View.VISIBLE
            binding.specialnoteField6.setHint(currentNote.specialField6?.title)
        }
        else
            binding.specialnoteField6.visibility = View.GONE

        if(currentNote.specialField7 !=null) {
            binding.specialnoteField7.visibility = View.VISIBLE
            binding.specialnoteField7.setHint(currentNote.specialField7?.title)
        }
        else
            binding.specialnoteField7.visibility = View.GONE

//
//        var number =1
//        var amount = 0
//        var check = mutableListOf<CostumeDataType>()

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
                nav.replace(this, R.id.action_updateNoteFragment_to_noteFragment)
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