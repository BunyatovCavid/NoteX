package com.example.notex.data.interfaces

import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.firestore.QuerySnapshot

interface SpecialNotesInterface {
    suspend fun getspeacialNote(collectionTitle:String): QuerySnapshot
    suspend fun getspeacialNoteById(collectionTitle:String, documentId: String): QuerySnapshot
    suspend fun addspeacialNote(speacialNotetitle:String, speacialNoteModel: SpecialNoteModel)
    suspend fun updateSpecialNote(specialNoteModel: SpecialNoteModel)
    suspend fun deleteSpecialNote(collectionTitle:String, specialNoteId:String, callback : (Boolean, String)->Unit)
}