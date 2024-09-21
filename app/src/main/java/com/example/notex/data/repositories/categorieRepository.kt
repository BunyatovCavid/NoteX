package com.example.notex.data.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.notex.data.interfaces.categorieInterface
import com.example.notex.data.models.CategoryModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.tasks.await

class categorieRepository:categorieInterface {

    var firebaseStrore:FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
        set(value){}

    override suspend fun getCategories(collectionTitle:String) :QuerySnapshot =
          firebaseStrore.collection("$collectionTitle").get().await()

    override suspend fun getCategoryByDocument(collectionTitle: String, document: CategoryModel): DocumentSnapshot? {
       return try {
            val documentSnapshot = firebaseStrore.collection(collectionTitle)
                .document(document.id)
                .get()
                .await()

            if (documentSnapshot.exists()) {
                val data = documentSnapshot.data
                Log.d("Firestore", "Document Tapildi: $data")
            } else {
                Log.d("Firestore", "Document Yoxdu")
            }
           documentSnapshot
        } catch (e: Exception) {
            Log.e("Firestore", "Document goturulen zaman  bir xeta bas verdi", e)
           null
        }

    }

    override suspend fun addCategory(collectionTitle: String, categoryModel: CategoryModel) {
        try {
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
        val batch = firebaseStrore.batch()

        firebaseStrore.collection(collectionTitle)
            .document(data.id)
            .delete().addOnSuccessListener {
                firebaseStrore.collection(data.title)
                    .get()
                    .addOnSuccessListener { querySnapshot ->
                        for (document in querySnapshot) {
                            val docRef = firebaseStrore.collection(data.title).document(document.id)
                            batch.delete(docRef)
                        }
                        batch.commit()
                            .addOnSuccessListener {
                                callback(true, "Success")
                            }
                            .addOnFailureListener{
                                callback(false, "Failed")
                            }
                    }
                    .addOnFailureListener { e ->
                        callback(false, e.message)
                    }
            }
            .addOnFailureListener { e ->
                callback(false, e.message)
            }



    }

}