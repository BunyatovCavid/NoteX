package com.example.notex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.interfaces.categorieInterface
import com.example.notex.data.interfaces.specialNotesInterface
import com.example.notex.data.models.CategoryModel
import com.example.notex.data.models.SpecialNoteModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class specialNoteViewModel @Inject constructor(private  val repository: specialNotesInterface):ViewModel() {

    private val _specialNoteResult = MutableLiveData<List<SpecialNoteModel>>()
    val specialNoteResult: LiveData<List<SpecialNoteModel>> get() = _specialNoteResult

    fun getCategories(collectionTitle:String)
    {
        viewModelScope.launch {
            try {
                val categories = repository.getspeacialNote(collectionTitle)
                var datas = mutableListOf<SpecialNoteModel>()
                for (document in categories) {
                    val item = document.toObject(SpecialNoteModel::class.java)
                    datas.add(item)
                }

                _specialNoteResult.postValue(datas)
            }catch(e:Exception)
            {
                Log.e("categoryViewModel", "Kateqoriyaları alarkən xəta baş verdi", e)
            }
        }
    }

    fun addspecialNote(collectionTitle: String, specialNoteModel: SpecialNoteModel){
        viewModelScope.launch {
            try {
                repository.addspeacialNote(collectionTitle, specialNoteModel)
                repository.getspeacialNote(collectionTitle)
            }catch (e:Exception)
            {
                Log.e("specialNoteViewModel", "Kateqoriya əlavə edərkən xəta baş verdi", e)
            }
        }
    }


}