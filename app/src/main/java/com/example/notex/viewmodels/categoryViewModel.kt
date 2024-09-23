package com.example.notex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.interfaces.categorieInterface
import com.example.notex.data.models.CategoryModel
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class categoryViewModel @Inject constructor(private  val repository: categorieInterface): ViewModel() {

    private val _categoryResult = MutableLiveData<List<CategoryModel>>()
    val categoryResult: LiveData<List<CategoryModel>> get() = _categoryResult

    private val _documentResult = MutableLiveData<CategoryModel?>()
    val documentResult:LiveData<CategoryModel?> get() = _documentResult

    private val _deleteResult = MutableLiveData<String>()
    val deleteResult:LiveData<String> get() = _deleteResult

    fun getCategories(collectionTitle:String)
    {
        viewModelScope.launch {
           try {
               val categories = repository.getCategories(collectionTitle)
               var datas = mutableListOf<CategoryModel>()
               for (document in categories) {
                   var item = CategoryModel()
                   item = document.toObject(CategoryModel::class.java)
                   item.id = document.id
                   datas.add(item)
               }

               _categoryResult.postValue(datas)
           }catch(e:Exception)
           {
               Log.e("categoryViewModel", "Kateqoriyaları alarkən xəta baş verdi", e)
           }
        }
    }

    fun getCategoryByDocument(collectionTitle: String, document:CategoryModel)
    {
        viewModelScope.launch {
            try {
                val category = repository.getCategoryByDocument(collectionTitle, document)
                val item = category?.toObject(CategoryModel::class.java)
                _documentResult.postValue(item)
            }catch(e:Exception)
            {
                Log.e("categoryViewModel", "Kateqoriyaları alarkən xəta baş verdi", e)
            }
        }
    }

    fun addCategory(collectionTitle: String, categoryModel: CategoryModel){
        viewModelScope.launch {
            try {
                repository.addCategory(collectionTitle, categoryModel)
                repository.getCategories(collectionTitle)
           }catch (e:Exception)
            {
                Log.e("categoryViewModel", "Kateqoriya əlavə edərkən xəta baş verdi", e)
           }
        }
    }

    fun deleteCategory(collectionTitle: String, data:CategoryModel)
    {
        viewModelScope.launch {
            repository.deleteCategory(collectionTitle, data){ success, result->
                    _deleteResult.postValue(result.toString())
            }
        }
    }


}