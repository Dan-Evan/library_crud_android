package com.example.librarycrud

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.contactmanagment.R
import com.example.librarycrud.adapter.BooksRecyclerAdapter
import com.example.librarycrud.database.DatabaseHandler
import com.example.librarycrud.models.Books
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private var booksRecyclerAdapter: BooksRecyclerAdapter? = null
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
    private var dbHandler: DatabaseHandler? = null
    private var listTasks: List<Books> = ArrayList()
    private var linearLayoutManager: LinearLayoutManager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        initOperations()
    }

    private fun initDB() {
        dbHandler = DatabaseHandler(this)
        listTasks = (dbHandler as DatabaseHandler).task()
        booksRecyclerAdapter =
            BooksRecyclerAdapter(tasksList = listTasks, context = applicationContext)
        (recyclerView as RecyclerView).adapter = booksRecyclerAdapter
    }

    private fun initViews() {
        val toolbar = findViewById<Toolbar>(R.id.adjustToolBar)
        setSupportActionBar(toolbar)
        fab = findViewById(R.id.floatingAddButton)
        recyclerView = findViewById(R.id.recycler_view)
        booksRecyclerAdapter =
            BooksRecyclerAdapter(tasksList = listTasks, context = applicationContext)
        linearLayoutManager = LinearLayoutManager(applicationContext)
        (recyclerView as RecyclerView).layoutManager = linearLayoutManager
    }

    private fun initOperations() {
        fab?.setOnClickListener {
            val i = Intent(applicationContext, AddOrEditActivity::class.java)
            i.putExtra("Mode", "A")
            startActivity(i)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_delete) {
            val dialog = AlertDialog.Builder(this).setTitle("Deleting all records").setMessage("Please confirm you want ot delete All records.")
                    .setPositiveButton("YES") { dialog, _ ->
                        dbHandler!!.deleteAllTasks()
                        initDB()
                        dialog.dismiss()
                    }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onResume() {
        super.onResume()
        initDB()
    }
}