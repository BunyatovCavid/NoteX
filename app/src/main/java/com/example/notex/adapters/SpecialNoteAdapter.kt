package com.example.notex.adapters

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.Note
import com.example.notex.data.models.SpecialNoteModel
import com.example.notex.databinding.SpecialNoteAdapterLayoutBinding
import com.example.notex.ui.fragments.Notes.NoteFragmentDirections
import com.example.notex.ui.fragments.SpecialNotes.HomeFragmentDirections
import com.example.notex.ui.fragments.SpecialNotes.SpecialNoteDetailFragment
import com.example.notex.ui.fragments.SpecialNotes.SpecialNoteDetailFragmentDirections
import java.util.Random

class SpecialNoteAdapter: RecyclerView.Adapter<SpecialNoteAdapter.SpecialNoteViewHolder>(){

    class SpecialNoteViewHolder(val itemBinding: SpecialNoteAdapterLayoutBinding)
        : RecyclerView.ViewHolder(itemBinding.root)

    private val differCallback =
        object : DiffUtil.ItemCallback<SpecialNoteModel>() {
            override fun areItemsTheSame(oldItem: SpecialNoteModel, newItem: SpecialNoteModel): Boolean {
                return oldItem.categoryTitle == newItem.categoryTitle &&
                        oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: SpecialNoteModel, newItem: SpecialNoteModel): Boolean {
                return oldItem == newItem
            }
        }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpecialNoteViewHolder {
        return SpecialNoteViewHolder(
            SpecialNoteAdapterLayoutBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: SpecialNoteViewHolder, position: Int) {
        val currentNote = differ.currentList[position]

        holder.itemBinding.tvNoteTitle.setText(currentNote.title)
        holder.itemBinding.tvNoteBody.setText(currentNote.categoryTitle)

        val random = Random()
        val color = Color.argb(
            255,
            128 + random.nextInt(128),
            128 + random.nextInt(128),
            128 + random.nextInt(128)
        )
        holder.itemBinding.cardView.setBackgroundColor(color)

        holder.itemView.setOnClickListener { view ->
            val direction = HomeFragmentDirections
                .actionHomeFragmentToSpecialNoteDetailFragment(currentNote)
            view.findNavController().navigate(direction)
        }
    }


}