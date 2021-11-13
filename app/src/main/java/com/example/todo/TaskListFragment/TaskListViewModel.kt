package com.example.todo.TaskListFragment

import androidx.lifecycle.ViewModel
import com.example.todo.Database.Task
import com.example.todo.Database.TaskRepository
import java.util.*
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val executor: ExecutorService = Executors.newSingleThreadExecutor()

class TaskListViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskLiveData = taskRepository.getAllTask(incomplete = false)


    fun deleteAll() {
        return taskRepository.deleteAll()
    }


    fun deleteIncomplete() {
        return taskRepository.deleteIncomplete()
    }


    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }


    fun updateCompleted(completed: Boolean?, id: UUID) {
        executor.execute {
            taskRepository.updateCompleted(completed, id)
        }
    }

}