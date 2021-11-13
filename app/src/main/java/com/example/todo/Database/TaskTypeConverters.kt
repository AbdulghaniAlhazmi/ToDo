package com.example.todo.Database

import androidx.room.TypeConverter
import java.util.*


class TodoTypeConverters {

    @TypeConverter
    fun fromUUID(uuid: UUID?): String? {
        return uuid?.toString()
    }

    @TypeConverter
    fun toUUID(uuid: String?): UUID? {
        return UUID.fromString(uuid)
    }


    @TypeConverter
    fun fromStartDate(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toStartDate(millisSinceEpoch: Long?): Date? {
        return millisSinceEpoch?.let { Date(it) }
    }

}