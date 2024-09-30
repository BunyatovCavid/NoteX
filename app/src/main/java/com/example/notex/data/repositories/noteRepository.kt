package com.example.notex.data.repositories

import androidx.lifecycle.LiveData
import com.example.notex.data.interfaces.Dao.NoteDao
import com.example.notex.data.interfaces.NoteInteface
import com.example.notex.data.models.Note
import javax.inject.Inject

class NoteRepository @Inject constructor(private val noteDao: NoteDao):NoteInteface {

    override suspend fun deleteNote(note: Note) = noteDao.deleteNote(note)
    override suspend fun insertNote(note: Note) = noteDao.insertNote(note)
    override suspend fun updateNote(note: Note) = noteDao.updateNote(note)
    override fun getAllNotes():LiveData<List<Note>> = noteDao.getAllNotes()
    override fun searchNote(query: String?) = noteDao.searchNote(query)
}