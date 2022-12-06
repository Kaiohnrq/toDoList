package com.example.todolist.model

data class Category (
    var id: Long,
    var description: String?,
    var tasks: List<Task>?
        ) {

    override fun toString(): String {
        return description!!
    }

}