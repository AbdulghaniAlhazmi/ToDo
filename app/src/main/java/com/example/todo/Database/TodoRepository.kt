package com.example.todo.Database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATABASE_NAME = "todo-database"

class TodoRepository private constructor(context: Context){

    private val database : TodoDatabase = Room.databaseBuilder(
        context.applicationContext,
        TodoDatabase::class.java,
        DATABASE_NAME,).build()

    private val todoDao = database.todoDao()


    companion object{

        private var INSTANCE : TodoRepository? = null

        fun initialize (context: Context){

            if (INSTANCE == null)
            {
                INSTANCE = TodoRepository(context)
            }
        }

        fun get() : TodoRepository{

            return INSTANCE ?:
            throw IllegalStateException("Repository not initialized")
        }

    }

}