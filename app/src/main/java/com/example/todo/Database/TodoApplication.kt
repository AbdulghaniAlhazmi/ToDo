package com.example.todo.Database

import android.app.Application

class TodoApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        TaskRepository.initialize(this)
    }
}