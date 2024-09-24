package com.example.notex.data.repositories

import androidx.lifecycle.LiveData
import com.example.notex.data.interfaces.Dao.NoteDao
import com.example.notex.data.interfaces.noteInteface
import com.example.notex.data.models.Note
import javax.inject.Inject

class noteRepository @Inject constructor(private val noteDao: NoteDao):noteInteface {

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    override fun getAllNotes():LiveData<List<Note>> = noteDao.getAllNotes()
    override fun searchNote(query: String?) = noteDao.searchNote(query)
}