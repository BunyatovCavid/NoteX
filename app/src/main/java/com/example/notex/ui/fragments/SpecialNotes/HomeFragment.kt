package com.example.notex.ui.fragments.SpecialNotes

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import com.example.notex.R
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notex.Independents.helper.toast
import com.example.notex.Independents.replaceFragments
import com.example.notex.adapters.NoteAdapter
import com.example.notex.adapters.SpecialNoteAdapter
import com.example.notex.data.models.Category
import com.example.notex.data.models.Note
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.databinding.FragmentHomeBinding
import com.example.notex.viewmodels.CategoryViewModel
import com.example.notex.viewmodels.SpecialNoteViewModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private var _binding:FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val categoryViewModel:CategoryViewModel by viewModels()
    private val specialNoteViewModel:SpecialNoteViewModel by viewModels()


    private lateinit var specialNoteAdapter: SpecialNoteAdapter
    private lateinit var nav:replaceFragments

    private var backPressedOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)


        var categories = mutableListOf<Category>()
        var options = mutableListOf<String>()


        categoryViewModel.getCategories("Categories")

        categoryViewModel.categoryResult.observe(viewLifecycleOwner, {result->
            try {
                categories.clear()
                options.clear()

                options.add("Please Select Category")

                result.forEach { item ->
                    categories.add(Category(item.id, item.title))
                }

                categories.forEach { item ->
                    options.add(item.title)
                }

                options.add("WithoutCategory")

                val adapter = ArrayAdapter(requireContext(), R.layout.use_spinner_item, options)
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.searchCategory.adapter = adapter

            } catch (e: Exception) {
                crashlytics.recordException(e)
                context?.toast("Error loading categories: ${e.message}")
            }
        })

        var previousSelectedCategory: String? = null

        binding.searchCategory.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedCategory = options[position]

                if (selectedCategory != "Please Select Category" && selectedCategory != previousSelectedCategory) {
                    previousSelectedCategory = selectedCategory
                    try {
                        specialNoteViewModel.getSpecialNotes(selectedCategory)
                        setUpRecyclerView()
                    } catch (e: Exception) {
                        crashlytics.recordException(e)
                        context?.toast("Error fetching special notes: ${e.message}")
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (backPressedOnce) {
                    requireActivity().finish()
                } else {
                    backPressedOnce = true
                    context?.toast("Press back again to exit")
                    view?.postDelayed({
                        backPressedOnce = false
                    }, 2000)
                }
            }
        })

        nav = replaceFragments()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fabAddNote.setOnClickListener {
            categoryViewModel.categoryResult.observe(viewLifecycleOwner, { result ->
                try {
                    var bundle = Bundle()
                    bundle.putParcelableArrayList("categories", ArrayList(result))
                    nav.replace(this, R.id.action_homeFragment_to_useCategoryFragment, bundle)
                } catch (e: Exception) {
                    crashlytics.recordException(e)
                    context?.toast("Error opening add note: ${e.message}")
                }
            })

        }

    }


    private fun setUpRecyclerView(){
        specialNoteAdapter = SpecialNoteAdapter()

        binding.recycleView.apply {
            layoutManager = StaggeredGridLayoutManager(2,
                StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = specialNoteAdapter
        }

        activity?.let {
            specialNoteViewModel.specialNoteResult.observe(viewLifecycleOwner, {specialNote->
                try {
                    specialNoteAdapter.differ.submitList(specialNote)
                    updateUI(specialNote)
                } catch (e: Exception) {
                    context?.toast("Error updating UI: ${e.message}")
                }
            })
        }
    }

    private fun updateUI(note: List<SpecialNoteModel>) {
        if (note.isNotEmpty()) {
            binding.cardView.visibility = View.GONE
            binding.recycleView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.VISIBLE
            binding.recycleView.visibility = View.GONE
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }


}