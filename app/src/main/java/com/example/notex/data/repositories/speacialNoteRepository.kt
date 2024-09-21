package com.example.notex.data.repositories

import android.util.Log
import com.example.notex.data.interfaces.specialNotesInterface
import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class speacialNoteRepository:specialNotesInterface {
    var firebaseStrore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value){}

    override suspend fun getspeacialNote(speacialNotetitle: String): QuerySnapshot =
        firebaseStrore.collection("$speacialNotetitle").get().await()


    override suspend fun addspeacialNote(
        categoryTitle: String,
        speacialNoteModel: SpecialNoteModel
    ) {
        try {
            firebaseStrore.collection("$categoryTitle").add(speacialNoteModel).await()
        }catch (e:Exception){
            Log.e("categorieRepository", "Kateqoriya əlavə edərkən xəta baş verdi", e)
        }
    }
}