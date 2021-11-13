package com.example.todo.TaskListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todo.Database.Task
import com.example.todo.Database.TaskRepository
import java.util.*
import java.util.concurrent.Executors

val executor = Executors.newSingleThreadExecutor()

class TaskListViewModel : ViewModel() {

    val taskRepository = TaskRepository.get()
    val taskLiveData = taskRepository.getAllTask(incomplete = false)



    fun deleteAll(){
        return taskRepository.deleteAll()
    }


    fun deleteIncomplete(){
        return taskRepository.deleteIncomplete()
    }



    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }


    fun updateCompleted(completed : Boolean?, id : UUID){
        executor.execute {
            taskRepository.updateCompleted(completed,id)
        }
    }

    fun saveUpdate(task: Task){
        executor.execute {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        executor.execute {
            taskRepository.deleteTask(task)
        }
    }
}