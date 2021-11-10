package com.example.todo.TaskFragment

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.todo.Database.Task
import com.example.todo.Database.TaskRepository
import java.util.*
import java.util.concurrent.Executors

class TaskFragmentViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    private val taskIdLiveData = MutableLiveData<UUID>()
    private val executor = Executors.newSingleThreadExecutor()


    var taskLiveData : LiveData<Task?> =
        Transformations.switchMap(taskIdLiveData){
            taskRepository.getTask(it)
        }

    fun loadTask(taskId : UUID){
        taskIdLiveData.value = taskId
    }

    fun saveUpdate(task: Task){
        executor.execute {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task){
        executor.execute {
            taskRepository.deleteTask(task)
        }

    }



}