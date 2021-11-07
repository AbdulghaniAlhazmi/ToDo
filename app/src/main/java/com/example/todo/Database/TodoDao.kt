package com.example.todo.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*

@Dao
interface TodoDao {

        @Query("SELECT * FROM todo")
        fun getAllTask(): LiveData<List<Todo>>

        @Query("SELECT * FROM todo ORDER BY startDate DESC")
        fun getAllTaskByStartDate(): LiveData<List<Todo>>

        @Query("SELECT * FROM todo ORDER BY EndDate DESC")
        fun getAllTaskByEndDate(): LiveData<List<Todo>>

        @Query("SELECT * FROM todo WHERE id=(:id)")
        fun getTask(id : UUID) : LiveData<Todo?>

        @Update
        fun updateTask (todo: Todo)

        @Insert
        fun addTask (todo: Todo)

        @Delete
        fun deleteTask (todo: Todo)

}