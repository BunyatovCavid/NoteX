package com.example.notex.ui.fragments.Categories

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notex.Independents.replaceFragments
import com.example.notex.R
import com.example.notex.adapters.CategorieAdapter
import com.example.notex.adapters.NoteAdapter
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.Note
import com.example.notex.databinding.FragmentCategorieBinding
import com.example.notex.databinding.FragmentNoteBinding
import com.example.notex.ui.MainActivity
import com.example.notex.viewmodels.categoryViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CategorieFragment : Fragment(R.layout.fragment_categorie) {

    private var _binding:FragmentCategorieBinding?= null
    private val binding get() = _binding!!
    private lateinit var nav: replaceFragments

    private val categoryViewModel: categoryViewModel by viewModels()
    private lateinit var categoryAdapter:CategorieAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategorieBinding.inflate(inflater,container,false)

        return  binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setUpRecyclerView()
        nav = replaceFragments()

        binding.fabAddCategorie?.setOnClickListener{
            nav.replace(this, R.id.action_categorieFragment_to_newCategoryFragment)
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                nav.replace(this@CategorieFragment, R.id.action_categorieFragment_to_homeFragment)
            }
        })

    }


    private fun setUpRecyclerView(){
         categoryAdapter = CategorieAdapter()

        binding.categorierecycleView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = categoryAdapter
        }

        activity?.let {
            categoryViewModel.getCategories("Categories")
            categoryViewModel.categoryResult.observe(viewLifecycleOwner, {result->
                categoryAdapter.differ.submitList(result)
                updateUI(result)
            })
        }


    }

    private fun updateUI(categorie: List<CategoryModel>) {
        if (categorie.isNotEmpty()) {
            binding.cardView.visibility = View.GONE
            binding.categorierecycleView.visibility = View.VISIBLE
        } else {
            binding.cardView.visibility = View.VISIBLE
            binding.categorierecycleView.visibility = View.GONE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding=null
    }

}