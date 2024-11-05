package com.example.notex.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.interfaces.NoteInteface
import com.example.notex.data.models.Note
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(private val repository:NoteInteface):ViewModel() {

    fun addNote(note: Note) =
        viewModelScope.launch {
            repository.insertNote(note)
        }

    fun deleteNote(note: Note) =
        viewModelScope.launch {
            repository.deleteNote(note)
        }

    fun updateNote(note: Note) =
        viewModelScope.launch {
            repository.updateNote(note)
        }

    fun getAllNote() = repository.getAllNotes()

    fun searchNote(query: String?) =
        repository.searchNote(query)


}