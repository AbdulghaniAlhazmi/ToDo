package com.example.todo.TaskListFragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.todo.Database.Task
import com.example.todo.Database.TaskRepository
import java.util.*
import java.util.concurrent.Executors

private val executor = Executors.newSingleThreadExecutor()

class TaskListViewModel : ViewModel() {

    val taskRepository = TaskRepository.get()
    val taskLiveData = taskRepository.getAllTask()


    fun addTask(task: Task) {
        taskRepository.addTask(task)
    }

    fun sortByDueDate():LiveData<List<Task>>
    {
        return taskRepository.getAllTaskByEndDate()
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