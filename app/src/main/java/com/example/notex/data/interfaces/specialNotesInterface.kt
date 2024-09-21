package com.example.notex.data.interfaces

import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.firestore.QuerySnapshot

interface specialNotesInterface {
    suspend fun getspeacialNote(categoryTitle:String): QuerySnapshot
    suspend fun addspeacialNote(speacialNotetitle:String, speacialNoteModel: SpecialNoteModel)
}