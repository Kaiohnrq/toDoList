package com.example.todolist

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todolist.api.Repository
import com.example.todolist.model.Category
import com.example.todolist.model.Task
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.Response
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: Repository
    ): ViewModel() {
    var selecTask: Task?= null

    private val _myResponseCategory = MutableLiveData<Response<List<Category>>>()
    private val _myResponseTask = MutableLiveData<Response<List<Task>>>()

    val myResponseCategory: LiveData<Response<List<Category>>> = _myResponseCategory
    val myResponseTask: LiveData<Response<List<Task>>> = _myResponseTask

    val selectedDate = MutableLiveData<LocalDate>()

    init {
        categoryList()
    }

    fun categoryList() {
        viewModelScope.launch {
            try {
                val response = repository.categoryList()
                _myResponseCategory.value = response
            } catch (e: Exception) {
                Log.d("Erro", e.message.toString())
            }
        }

    }

    fun taskAdd(task: Task){

        viewModelScope.launch {

            try {

                repository.taskAdd((task))

            } catch (e: Exception){

                Log.d("Erro", e.message.toString())

            }

        }

    }

    fun taskList() {

        viewModelScope.launch {
            try {

                val response = repository.taskList()
                _myResponseTask.value = response
                taskList()

            } catch (e: Exception) {

                Log.d("Erro", e.message.toString())

            }
        }

    }

    fun taskUpdate(task: Task){

        viewModelScope.launch {
            try{
                repository.taskUpdate(task)
                taskList()
            } catch (e: Exception){
                Log.d("Erro", e.message.toString())
            }
        }

    }

}