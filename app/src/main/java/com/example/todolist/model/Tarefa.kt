package com.example.todolist.model

data class Task (
    var id: Long,
    var name: String,
    var description: String,
    var responsavel: String,
    var date: String,
    var status: Boolean,
    var category: Category
        ) {
}