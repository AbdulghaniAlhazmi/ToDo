package com.example.todo.Database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [Todo::class],version = 1)
@TypeConverters(TodoTypeConverters::class)
abstract class TodoDatabase : RoomDatabase(){

abstract fun todoDao():TodoDao
}