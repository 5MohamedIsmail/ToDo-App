package com.route.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.route.todoapp.database.model.Task

@Dao
interface TasksDao {

    @Insert
    fun createTask(task:Task)

    @Delete
    fun deleteTask(task: Task)

    @Update
    fun updateTask(task: Task)

    @Query("select * from Task")
    fun getAllTasks():List<Task>

    @Query("select * from Task where date=:date ORDER BY time ASC")
    fun getTasksByDate(date:Long):List<Task>
}