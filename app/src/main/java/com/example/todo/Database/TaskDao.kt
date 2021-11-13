package com.example.todo.Database

import androidx.lifecycle.LiveData
import androidx.room.*
import java.util.*
import java.util.concurrent.Flow

@Dao
interface TaskDao {

        @Query("SELECT * FROM Task WHERE completed=(:incomplete)")
        fun getAllTask(incomplete : Boolean): LiveData<List<Task>>

        @Query("DELETE FROM Task")
        fun deleteAll()

        @Query("DELETE FROM Task WHERE completed=(:incomplete)")
        fun deleteIncomplete(incomplete : Boolean = false)

        @Query("DELETE FROM Task WHERE completed=(:complete)")
        fun deleteCompleted(complete : Boolean = true)


        @Query("SELECT * FROM Task WHERE completed=(:complete)")
        fun getAllCompleted(complete : Boolean): LiveData<List<Task>>

        @Query("SELECT * FROM Task WHERE id=(:id)")
        fun getTask(id : UUID) : LiveData<Task?>

        @Query("UPDATE Task SET completed=:completed WHERE id = :id")
        fun updateCompleted(completed : Boolean?, id : UUID)

        @Update
        fun updateTask (task: Task)

        @Insert
        fun addTask (task: Task)

        @Delete
        fun deleteTask (task: Task)


}