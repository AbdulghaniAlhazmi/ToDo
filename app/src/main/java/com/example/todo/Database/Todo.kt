package com.example.todo.Database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class Todo(@PrimaryKey val id : UUID = UUID.randomUUID(),
                var todoTitle : String = "",
                var startDate : Date = Date(),
                var endDate : Date = Date(),
                var extraInfo : String = "",
                var isDone : Boolean = false, ) {
}