package com.example.notex.data.repositories

import android.util.Log
import com.example.notex.data.interfaces.SpecialNotesInterface
import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.FieldPath
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class SpeacialNoteRepository : SpecialNotesInterface {


    private val firebaseAuth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    private  val crashlytics: FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()

    private val firebaseStore:FirebaseFirestore
        get() = FirebaseFirestore.getInstance()

    var userId = firebaseAuth.currentUser?.uid

    override suspend fun getspeacialNote(collectionTitle: String): QuerySnapshot =
        firebaseStore.collection("$collectionTitle").whereEqualTo("userId", userId).get().await()

    override suspend fun getspeacialNoteById(collectionTitle: String, documentId: String): QuerySnapshot =
        firebaseStore.collection(collectionTitle).whereEqualTo("userId", userId).whereEqualTo(FieldPath.documentId(), documentId)
            .get().await()



    override suspend fun addspeacialNote(
        categoryTitle: String,
        speacialNoteModel: SpecialNoteModel
    ) {
        try {
            speacialNoteModel.userId = userId ?: ""
            firebaseStore.collection("$categoryTitle").add(speacialNoteModel).await()
        } catch (e: Exception) {
            crashlytics.recordException(e)
        }
    }

    override suspend fun updateSpecialNote(specialNoteModel: SpecialNoteModel) {
        var querySnapshot =
            firebaseStore.collection(specialNoteModel.categoryTitle).whereEqualTo("userId", userId)
                .whereEqualTo(
                    FieldPath.documentId(), specialNoteModel.id
                ).get().await()

        if (querySnapshot.documents.isNotEmpty()) {

            val documentRef = querySnapshot.documents[0].reference

            val updateMap = mutableMapOf<String, Any?>()
            updateMap["title"] = specialNoteModel.title
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
                    Log.d("Firestore", "Note uğurla dəyişildi!")
                }
                .addOnFailureListener { e ->
                    crashlytics.recordException(e)
                }
        } else {
            Log.d("Firestore", "Note tapılmadı.")
        }

    }

    override suspend fun deleteSpecialNote(collectionTitle:String, specialNoteId:String, callback : (Boolean, String)->Unit) {
        try {
            val documentSnapshot = firebaseStore.collection(collectionTitle)
                .document(specialNoteId)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val documentUserId = documentSnapshot.getString("userId")

                if (documentUserId == userId) {
                    firebaseStore.collection(collectionTitle)
                        .document(specialNoteId)
                        .delete()
                        .await()

                    callback(true, "Success")
                    Log.d("Firestore", "Note uğurla silindi!")
                } else {
                    callback(false, "No Authentication")
                    Log.w("Firestore", "Bu Note-u silmə icazəniz yoxdur.")
                }
            } else {
                callback(false, "Not Found")
                Log.w("Firestore", "Note tapılmadı.")
            }
        } catch (e: Exception) {
            crashlytics.recordException(e)
            callback(false,"Failed")
        }
    }
}