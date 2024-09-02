package com.route.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.route.todoapp.database.dao.TasksDao
import com.route.todoapp.database.model.Task

@Database(entities = [Task::class], version = 4)
abstract class AppDatabase: RoomDatabase() {

    abstract fun getTasksDao(): TasksDao
    // other daos go here

    companion object {  // static
        // Singleton pattern
        private var db: AppDatabase? = null
        private const val databaseName = "Tasks-database"
        fun getInstance(context: Context): AppDatabase {
            if (db == null) {
                //  Create
                db = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    databaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return db!!
        }
    }
}