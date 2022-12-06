package com.example.todolist.api

import com.example.todolist.model.Category
import com.example.todolist.model.Task
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT

interface ApiService {

    @GET("categoria")
    suspend fun categoryList (): Response<List<Category>>

    @POST("tarefa")
    suspend fun taskAdd(
        @Body task: Task
    ) : Response<Task>

    @GET("tarefa")
    suspend fun taskList() : Response<List<Task>>

    @PUT("tarefa")
    suspend fun taskUpdate(
        @Body task: Task
    ) : Response<Task>

}