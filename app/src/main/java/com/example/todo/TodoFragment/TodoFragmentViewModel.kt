package com.example.todo.TodoFragment

import androidx.lifecycle.ViewModel
import com.example.todo.Database.TodoRepository

class TodoFragmentViewModel : ViewModel() {

    private val todoRepository = TodoRepository.get()
}