package com.example.todo.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Task::class],version = 1)
@TypeConverters(TodoTypeConverters::class)
abstract class TaskDatabase : RoomDatabase(){

    abstract fun taskDao():TaskDao

}
