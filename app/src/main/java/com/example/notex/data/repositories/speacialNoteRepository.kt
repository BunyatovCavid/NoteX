package com.example.notex.data.repositories

import android.util.Log
import com.example.notex.data.interfaces.specialNotesInterface
import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class speacialNoteRepository: specialNotesInterface {
    var firebaseStrore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value) {}

    var userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun getspeacialNote(collectionTitle: String): QuerySnapshot =
        firebaseStrore.collection("$collectionTitle").whereEqualTo("userId", userId).get().await()

    override suspend fun getspeacialNoteById(collectionTitle: String, documentId: String): QuerySnapshot =
        firebaseStrore.collection(collectionTitle).whereEqualTo("userId", userId).whereEqualTo(FieldPath.documentId(), documentId)
            .get().await()



    override suspend fun addspeacialNote(
        categoryTitle: String,
        speacialNoteModel: SpecialNoteModel
    ) {
        try {
            speacialNoteModel.userId = userId ?: ""
            firebaseStrore.collection("$categoryTitle").add(speacialNoteModel).await()
        } catch (e: Exception) {
            Log.e("categorieRepository", "Kateqoriya əlavə edərkən xəta baş verdi", e)
        }
    }

    override suspend fun updateSpecialNote(specialNoteModel: SpecialNoteModel) {
        var querySnapshot =
            firebaseStrore.collection(specialNoteModel.categoryTitle).whereEqualTo("userId", userId)
                .whereEqualTo(
                    FieldPath.documentId(), specialNoteModel.id
                ).get().await()

        if (querySnapshot.documents.isNotEmpty()) {

            val documentRef = querySnapshot.documents[0].reference

            val updateMap = mutableMapOf<String, Any?>()

            updateMap["specialField1"] = specialNoteModel.specialField1
            updateMap["specialField2"] = specialNoteModel.specialField2
            updateMap["specialField3"] = specialNoteModel.specialField3
            updateMap["specialField4"] = specialNoteModel.specialField4
            updateMap["specialField5"] = specialNoteModel.specialField5
            updateMap["specialField6"] = specialNoteModel.specialField6
            updateMap["specialField7"] = specialNoteModel.specialField7
            updateMap["updatedAt"] = FieldValue.serverTimestamp()

            documentRef.update(updateMap)
                .addOnSuccessListener {
                    Log.d("Firestore", "Belge başarıyla güncellendi!")
                }
                .addOnFailureListener { e ->
                    Log.w("Firestore", "Belge güncelleme hatası", e)
                }
        } else {
            Log.d("Firestore", "Belge bulunamadı.")
        }

    }

    override suspend fun deleteSpecialNote(collectionTitle:String, specialNoteId:String, callback : (Boolean, String)->Unit) {
        try {
            val documentSnapshot = firebaseStrore.collection(collectionTitle)
                .document(specialNoteId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val documentUserId = documentSnapshot.getString("userId")

                if (documentUserId == userId) {
                    firebaseStrore.collection(collectionTitle)
                        .document(specialNoteId)
                        .delete()
                        .await()

                    callback(true, "Success")
                    Log.d("Firestore", "Belge başarıyla silindi!")
                } else {
                    callback(false, "No Authentication")
                    Log.w("Firestore", "Belgeyi silme yetkiniz yok.")
                }
            } else {
                callback(false, "Not Found")
                Log.w("Firestore", "Belge bulunamadı.")
            }
        } catch (e: Exception) {
            callback(false,"Failed")
            Log.e("Firestore", "Silme işlemi başarısız: ${e.message}")
        }
    }
}