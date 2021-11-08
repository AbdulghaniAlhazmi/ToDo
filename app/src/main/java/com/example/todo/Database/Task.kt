package com.example.todo.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

//priority 12345

@Entity
data class Task(@PrimaryKey val id : UUID = UUID.randomUUID(),
                var taskTitle : String="",
                var startDate : Date = Date(),
                var endDate : Date = Date(),
                var extraInfo : String="",
                var isDone : Boolean = false, ) {


}