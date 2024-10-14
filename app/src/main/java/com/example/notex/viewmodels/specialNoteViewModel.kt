package com.example.notex.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.notex.data.interfaces.SpecialNotesInterface
import com.example.notex.data.models.SpecialNoteModel
import com.google.firebase.crashlytics.FirebaseCrashlytics
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SpecialNoteViewModel @Inject constructor(private  val repository: SpecialNotesInterface,  private val crashlytics : FirebaseCrashlytics):ViewModel() {

    private val _specialNoteResult = MutableLiveData<List<SpecialNoteModel>>()
    val specialNoteResult: LiveData<List<SpecialNoteModel>> get() = _specialNoteResult

    private val _deleteResult = MutableLiveData<String>()
    val deleteResult: LiveData<String> get() = _deleteResult

    private val _updateResult = MutableLiveData<SpecialNoteModel>()
    val updateResult: LiveData<SpecialNoteModel> get() = _updateResult

    fun getSpecialNotes(collectionTitle:String)
    {
        viewModelScope.launch {
            try {
                val categories = repository.getspeacialNote(collectionTitle)
                var datas = mutableListOf<SpecialNoteModel>()
                for (document in categories) {
                    val item = document.toObject(SpecialNoteModel::class.java)
                    item.id=document.id
                    datas.add(item)
                }

                _specialNoteResult.postValue(datas)
            }catch(e:Exception)
            {
                crashlytics.recordException(e)
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
                crashlytics.recordException(e)
            }
        }
    }

    fun updateSpecialNote(specialNoteModel: SpecialNoteModel){
        viewModelScope.launch {
            repository.updateSpecialNote(specialNoteModel)
            val document =  repository.getspeacialNoteById(specialNoteModel.categoryTitle, specialNoteModel.id)
            lateinit var item:SpecialNoteModel
            document.forEach{i->
             item = i.toObject(SpecialNoteModel::class.java)
                item.id = i.id
            }
            _updateResult.postValue(item)
        }
    }


    fun deleteSpecialNote(specialNoteModel: SpecialNoteModel){
        viewModelScope.launch {
            repository.deleteSpecialNote(specialNoteModel.categoryTitle, specialNoteModel.id){succes, result->
                _deleteResult.postValue(result)
            }

        }
    }

}