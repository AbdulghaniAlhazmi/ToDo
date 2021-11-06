package com.example.todo.Database

import androidx.room.TypeConverter
import java.util.*


class TodoTypeConverters {

    @TypeConverter
    fun fromStartDate(date : Date? ) : Long?{
        return date?.time
    }

    @TypeConverter
    fun toStartDate(millisSinceEpoch : Long?) : Date?{
        return millisSinceEpoch?.let { Date(it) }
    }

}