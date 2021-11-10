package com.example.todo.Database

import android.content.Context
import android.os.AsyncTask
import androidx.lifecycle.LiveData
import androidx.room.Room
import java.util.*
import java.util.concurrent.Executors

private const val DATABASE_NAME = "Task-Database1"
private val executor = Executors.newSingleThreadExecutor()

class TaskRepository private constructor(context: Context){

    private val database : TaskDatabase = Room.databaseBuilder(
        context.applicationContext,
        TaskDatabase::class.java,
        DATABASE_NAME,
    ).build()

    private val taskDao = database.taskDao()

    fun getAllTask(): LiveData<List<Task>> = taskDao.getAllTask()

    fun getTask(id : UUID) : LiveData<Task?> {
        return taskDao.getTask(id)
    }

    fun updateCompleted(completed : Boolean?, id : UUID){
        return taskDao.updateCompleted(completed,id)
    }

    fun updateTask (task: Task){
        return taskDao.updateTask(task)
    }

    fun addTask (task: Task) {
        executor.execute {
            taskDao.addTask(task)
        }
    }

    fun deleteTask (task: Task){
        return taskDao.deleteTask(task)
    }

    companion object{

        private var INSTANCE : TaskRepository? = null

        fun initialize (context: Context){

            if (INSTANCE == null)
            {
                INSTANCE = TaskRepository(context)
            }
        }

        fun get() : TaskRepository{
            return INSTANCE ?:
            throw IllegalStateException("Repository not initialized")
        }

    }

}