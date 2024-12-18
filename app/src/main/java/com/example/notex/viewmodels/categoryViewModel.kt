package com.example.notex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.interfaces.CategorieInterface
import com.example.notex.data.models.CategoryModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(private  val repository: CategorieInterface): ViewModel() {

    private val crashlytics : FirebaseCrashlytics
        get() = FirebaseCrashlytics.getInstance()


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
               crashlytics.recordException(e)
               Log.d("CostumeExceptionHandle", e.message.toString())
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
                crashlytics.recordException(e)
                Log.d("CostumeExceptionHandle", e.message.toString())
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
                crashlytics.recordException(e)
                Log.d("CostumeExceptionHandle", e.message.toString())
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