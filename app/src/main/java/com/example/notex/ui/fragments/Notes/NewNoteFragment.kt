package com.example.notex.ui.fragments.Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.Note
import com.example.notex.databinding.FragmentNewNoteBinding
import com.example.notex.viewmodels.NoteViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NewNoteFragment : Fragment(R.layout.fragment_new_note) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()


    private var _binding: FragmentNewNoteBinding? = null
    private val binding get() = _binding
    private lateinit var nav: replaceFragments

    private val noteViewModel: NoteViewModel by viewModels()
    private lateinit var mView:View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        nav = replaceFragments()

        _binding = FragmentNewNoteBinding.inflate(
            inflater,
            container,
            false
        )

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
    }

    private fun saveNote(view: View) {
        val noteTitle = binding?.etNoteTitle?.text.toString().trim()
        val noteBody = binding?.etNoteBody?.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteBody)

            try {
                noteViewModel.addNote(note)
                Snackbar.make(
                    view, "Note saved successfully",
                    Snackbar.LENGTH_SHORT
                ).show()
                nav.replace(this, R.id.action_newNoteFragment_to_noteFragment)
            } catch (e: Exception) {
                crashlytics.recordException(e)
                Snackbar.make(
                    view, "Failed to save note: ${e.message}",
                    Snackbar.LENGTH_LONG
                ).show()
            }
        } else {
            context?.toast("Please enter note title")
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
                saveNote(mView)
            }
            android.R.id.home->{
                nav.replace(this, R.id.action_newNoteFragment_to_noteFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }



}