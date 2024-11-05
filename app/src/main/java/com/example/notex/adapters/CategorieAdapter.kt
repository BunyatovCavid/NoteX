package com.example.notex.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notex.data.models.CategoryModel
import com.example.notex.databinding.CategorieLayoutAdapterBinding
import com.example.notex.ui.fragments.Categories.CategorieFragmentDirections

class CategorieAdapter():RecyclerView.Adapter<CategorieAdapter.CategorieViewHolder>() {

    class CategorieViewHolder(val itemBinding: CategorieLayoutAdapterBinding)
        :RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<CategoryModel>() {
            override fun areItemsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: CategoryModel, newItem: CategoryModel): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategorieViewHolder {
        return CategorieViewHolder(
            CategorieLayoutAdapterBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CategorieViewHolder, position: Int) {
        var currentcategorie:CategoryModel = differ.currentList[position]
        holder.itemBinding.textViewTitle.text = currentcategorie.title.toString()
        holder.itemBinding.textViewSubtitle.text = currentcategorie.description.toString()

        holder.itemView.setOnClickListener { view ->
           val direction = CategorieFragmentDirections.actionCategorieFragmentToDetailCategoryFragment(currentcategorie)
           view.findNavController().navigate(direction)
        }
    }


}