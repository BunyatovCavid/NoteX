package com.example.notex.ui.fragments.Notes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.adapters.NoteAdapter
import com.example.notex.data.models.Note
import com.example.notex.databinding.FragmentHomeBinding
import com.example.notex.databinding.FragmentLoginBinding
import com.example.notex.databinding.FragmentNoteBinding
import com.example.notex.databinding.FragmentRegisterBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.noteViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NoteFragment : Fragment(R.layout.fragment_note), SearchView.OnQueryTextListener {

    private var _binding: FragmentNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var nav: replaceFragments

    private val noteViewModel: noteViewModel by viewModels()
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nav = replaceFragments()

        setUpRecyclerView()

        binding.fabAddNote.setOnClickListener{mView->
            nav.replace(this, R.id.action_noteFragment_to_newNoteFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                nav.replace(this@NoteFragment, R.id.action_noteFragment_to_homeFragment)
            }
        })
    }




    private fun setUpRecyclerView(){
        noteAdapter = NoteAdapter()

        binding.recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        activity?.let {
       noteViewModel.getAllNote().observe(viewLifecycleOwner, {note->
                noteAdapter.differ.submitList(note)
                updateUI(note)
            })
        }
    }

    private fun updateUI(note: List<Note>) {
        if (note.isNotEmpty()) {
            binding.cardView.visibility = View.GONE
            binding.recycleView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.VISIBLE
            binding.recycleView.visibility = View.GONE
        }
    }


    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        menu.clear()
        inflater.inflate(R.menu.home_menu, menu)

        val mMenuSearch = menu.findItem(R.id.menu_search).actionView as SearchView
        mMenuSearch.isSubmitButtonEnabled = true
        mMenuSearch.setOnQueryTextListener(this)
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

    override fun onQueryTextSubmit(query: String?): Boolean {

        if(query!=null)
            searchNotes(query)

        return true
    }

    override fun onQueryTextChange(query: String?): Boolean {
        if(query!=null)
            searchNotes(query)

        return true
    }

    private fun searchNotes(query: String?){
        val searchQuery="%$query%"
        noteViewModel.searchNote(searchQuery).observe(this@NoteFragment,{list->
            noteAdapter.differ.submitList(list)
        })
    }

}