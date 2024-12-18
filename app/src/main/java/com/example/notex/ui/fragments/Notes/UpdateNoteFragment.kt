package com.example.notex.ui.fragments.Notes

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
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.data.models.Note
import com.example.notex.databinding.FragmentUpdateNoteBinding
import com.example.notex.viewmodels.NoteViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateNoteFragment : Fragment(R.layout.fragment_update_note) {


    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()



    private var _binding: FragmentUpdateNoteBinding? = null
    private val binding get() = _binding
    private lateinit var nav: replaceFragments

    private val args: UpdateNoteFragmentArgs by navArgs()
    private lateinit var currentNote:Note
    private val noteViewModel: NoteViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateNoteBinding.inflate(
            inflater,
            container,
            false
        )
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = replaceFragments()

        currentNote = args.note!!

        binding?.etNoteTitleUpdate?.setText(currentNote.noteTitle)
        binding?.etNoteBodyUpdate?.setText(currentNote.noteBody)

        binding?.fabDone?.setOnClickListener{

            val title = binding?.etNoteTitleUpdate?.text.toString().trim()
            val body = binding?.etNoteBodyUpdate?.text.toString().trim()

            if(title.isNotEmpty())
            {
                 val note = Note(currentNote.id, title, body)

                try {
                    noteViewModel.updateNote(note)
                    context?.toast("Note updated successfully")
                    nav.replace(this, R.id.action_updateNoteFragment_to_noteFragment)
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    context?.toast("Error updating note: ${e.message}")
                }
            }else {
                activity?.toast("Please enter title name!")
            }
        }
    }

    private fun deleteNote() {
        AlertDialog.Builder(activity).apply {
            setTitle("Delete Note")
            setMessage("Are you sure you want to permanently delete this note?")
            setPositiveButton("DELETE") { _, _ ->
                try {
                    noteViewModel.deleteNote(currentNote)
                    view?.findNavController()?.navigate(R.id.action_updateNoteFragment_to_noteFragment,
                        null,
                        NavOptions.Builder().setPopUpTo(R.id.noteFragment, true).build()
                    )
                   context.toast("Note deleted successfully")
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    context.toast("Error deleting note: ${e.message}")
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
                nav.replace(this, R.id.action_updateNoteFragment_to_noteFragment)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}