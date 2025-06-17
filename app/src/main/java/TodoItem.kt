package com.example.znotodo

data class TodoItem(
    val title: String,
    val description: String,
    var isCompleted: Boolean = false
)