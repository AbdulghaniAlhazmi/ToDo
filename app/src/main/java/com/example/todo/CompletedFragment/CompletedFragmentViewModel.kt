package com.example.todo.CompletedFragment

import androidx.lifecycle.ViewModel
import com.example.todo.Database.TaskRepository
import com.example.todo.TaskListFragment.executor
import java.util.*


class CompletedFragmentViewModel : ViewModel() {

    private val taskRepository = TaskRepository.get()
    val taskLiveData = taskRepository.getAllCompleted(completed = true)


    fun updateCompleted(completed: Boolean?, id: UUID) {
        executor.execute {
            taskRepository.updateCompleted(completed, id)
        }
    }

    fun deleteCompleted() {
        return taskRepository.deleteCompleted()
    }

}