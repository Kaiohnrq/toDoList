package com.example.todolist.api

import com.example.todolist.model.Category
import com.example.todolist.model.Task
import retrofit2.Response

class Repository {

    suspend fun categoryList(): Response<List<Category>>{
        return RetrofitInstance.api.categoryList()
    }

    suspend fun taskAdd(task: Task): Response<Task>{

        return RetrofitInstance.api.taskAdd((task))

    }

    suspend fun taskList(): Response<List<Task>>{

        return RetrofitInstance.api.taskList()

    }

    suspend fun taskUpdate(task: Task): Response<Task>{

        return RetrofitInstance.api.taskUpdate(task)

    }

}