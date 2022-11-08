package com.example.librarycrud.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.librarycrud.models.Books
import java.util.*

class DatabaseHandler(context: Context) : SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        val CREATE_TABLE = "CREATE TABLE $TABLE_NAME ($ID INTEGER PRIMARY KEY, $ISBN INTEGER, $TITLE TEXT, $AUTHOR TEXT, $COURSE TEXT )"
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    fun addTask(tasks: Books): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ISBN, tasks.isbnID)
        values.put(TITLE, tasks.title)
        values.put(AUTHOR, tasks.author)
        values.put(COURSE, tasks.course)
        val _success = db.insert(TABLE_NAME, null, values)
        db.close()
        Log.v("InsertedId", "$_success")
        return (Integer.parseInt("$_success") != -1)
    }

    fun getTask(_id: Int): Books {
        val tasks = Books()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME WHERE $ID = $_id"
        val cursor = db.rawQuery(selectQuery, null)

        cursor?.moveToFirst()
        tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
        tasks.isbnID = cursor.getString(cursor.getColumnIndexOrThrow(ISBN)).toLong()
        tasks.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
        tasks.author = cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR))
        tasks.course = cursor.getString(cursor.getColumnIndexOrThrow(COURSE))
        cursor.close()
        return tasks
    }

    fun task(): List<Books> {
        val taskList = ArrayList<Books>()
        val db = writableDatabase
        val selectQuery = "SELECT  * FROM $TABLE_NAME"
        val cursor = db.rawQuery(selectQuery, null)
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    val tasks = Books()
                    tasks.id = Integer.parseInt(cursor.getString(cursor.getColumnIndexOrThrow(ID)))
                    tasks.isbnID = cursor.getString(cursor.getColumnIndexOrThrow(ISBN)).toLong()
                    tasks.title = cursor.getString(cursor.getColumnIndexOrThrow(TITLE))
                    tasks.author = cursor.getString(cursor.getColumnIndexOrThrow(AUTHOR))
                    tasks.course = cursor.getString(cursor.getColumnIndexOrThrow(COURSE))
                    taskList.add(tasks)
                } while (cursor.moveToNext())
            }
        }
        cursor.close()
        return taskList
    }

    fun updateTask(books: Books): Boolean {
        val db = this.writableDatabase
        val values = ContentValues()
        values.put(ISBN, books.isbnID)
        values.put(TITLE, books.title)
        values.put(AUTHOR, books.author)
        values.put(COURSE, books.course)
        val _success = db.update(TABLE_NAME, values, "$ID=?", arrayOf(books.id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteTask(_id: Int): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, "$ID=?", arrayOf(_id.toString())).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    fun deleteAllTasks(): Boolean {
        val db = this.writableDatabase
        val _success = db.delete(TABLE_NAME, null, null).toLong()
        db.close()
        return Integer.parseInt("$_success") != -1
    }

    companion object {
        private const val DB_VERSION = 1
        private const val DB_NAME = "records"
        private const val TABLE_NAME = "books"
        private const val ID = "Id"
        private const val ISBN = "isbn"
        private const val TITLE = "Title"
        private const val AUTHOR = "Author"
        private const val COURSE = "Course"
    }
}