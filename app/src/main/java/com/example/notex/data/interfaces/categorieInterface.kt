package com.example.notex.data.interfaces

import com.example.notex.data.models.CategoryModel
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot

interface CategorieInterface {
    suspend fun getCategories(collectionTitle:String):QuerySnapshot
    suspend fun getCategoryByDocument(collectionTitle: String, document:CategoryModel):DocumentSnapshot?
    suspend fun addCategory(collectionTitle:String, categoryModel: CategoryModel)
    suspend fun deleteCategory(collectionTitle: String, document: CategoryModel, callback : (Boolean, String?) -> Unit )
}