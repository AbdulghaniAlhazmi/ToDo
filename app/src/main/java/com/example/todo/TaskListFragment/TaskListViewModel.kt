package com.example.todo.TaskListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.Database.Task
import com.example.todo.Database.TaskRepository

class TaskListViewModel : ViewModel() {

    val taskRepository = TaskRepository.get()
    val taskLiveData = taskRepository.getAllTask()


    fun addTask(task: Task){
        taskRepository.addTask(task)
    }

//    fun deleteAll(task: Task){
//        taskRepository.deleteAll()
//    }




}