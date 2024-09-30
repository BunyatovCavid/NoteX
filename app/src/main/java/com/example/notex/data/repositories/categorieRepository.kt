package com.example.notex.data.repositories

import android.util.Log
import com.google.firebase.firestore.FieldPath
import com.example.notex.data.interfaces.CategorieInterface
import com.example.notex.data.models.CategoryModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class CategorieRepository:CategorieInterface {

    var firebaseStrore:FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value){}

    var userId = FirebaseAuth.getInstance().currentUser?.uid

    override suspend fun getCategories(collectionTitle:String) :QuerySnapshot =
          firebaseStrore.collection("$collectionTitle").whereEqualTo("userId", userId).get().await()

    override suspend fun getCategoryByDocument(collectionTitle: String, document: CategoryModel): DocumentSnapshot? {
       return try {
            val documentSnapshot = firebaseStrore.collection(collectionTitle)
                .whereEqualTo("userId", userId)
                .whereEqualTo(FieldPath.documentId() ,document.id)
                .get()
                .await().documents
                .firstOrNull()

           if (documentSnapshot != null) {
               if (documentSnapshot.exists()) {
                   val data = documentSnapshot.data
                   Log.d("Firestore", "Document Tapildi: $data")
               } else {
                   Log.d("Firestore", "Document Yoxdu")
               }
           }
           documentSnapshot
        } catch (e: Exception) {
            Log.e("Firestore", "Document goturulen zaman  bir xeta bas verdi", e)
           null
        }

    }

    override suspend fun addCategory(collectionTitle: String, categoryModel: CategoryModel) {
        try {
            categoryModel.userId = userId?:""
            firebaseStrore.collection("$collectionTitle").add(categoryModel).await()
        }catch (e:Exception){
            Log.e("categorieRepository", "Kateqoriya əlavə edərkən xəta baş verdi", e)
        }
    }

    override suspend fun deleteCategory(
        collectionTitle: String,
        data: CategoryModel,
        callback: (Boolean, String?) -> Unit
    ) {
        try {
            val batch = firebaseStrore.batch()


            val documentSnapshot = firebaseStrore.collection(collectionTitle)
                .document(data.id)
                .get()
                .await()

            val documentUserId = documentSnapshot.getString("userId")

            if (documentUserId == userId) {
                firebaseStrore.collection(collectionTitle)
                    .document(data.id)
                    .delete()
                    .await()

                firebaseStrore.collection(data.title).get().addOnSuccessListener {result->
                    for (document in result) {
                        val docRef = firebaseStrore.collection(data.title).document(document.id)
                        batch.delete(docRef)
                    }
                }.addOnFailureListener{}.await()


                batch.commit().await()

                callback(true, "Success")
            } else {
                callback(false, "Unauthorized: You can't delete this document")
            }
        } catch (e: Exception) {
            callback(false, e.message)
        }

    }
}