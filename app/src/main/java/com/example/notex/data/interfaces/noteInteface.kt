package com.example.notex.data.interfaces

import androidx.lifecycle.LiveData
import com.example.notex.data.models.Note

interface NoteInteface {
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
    fun getAllNotes():LiveData<List<Note>>
    fun searchNote(query: String?):LiveData<List<Note>>
}