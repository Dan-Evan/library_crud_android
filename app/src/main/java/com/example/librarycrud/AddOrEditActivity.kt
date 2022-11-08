package com.example.librarycrud

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.contactmanagment.R
import com.example.librarycrud.database.DatabaseHandler
import com.example.librarycrud.models.Books

class AddOrEditActivity : AppCompatActivity() {

    private var dbHandler: DatabaseHandler? = null
    private var isEditMode = false

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initDB()
        initOperations()
    }

    private fun initDB() {
        
        val isbnInput: EditText = findViewById(R.id.isbn)
        val titleInput: EditText = findViewById(R.id.bookTitle)
        val authorInput: EditText = findViewById(R.id.bookAuthor)
        val courseInput: EditText = findViewById(R.id.bookCourse)

        dbHandler = DatabaseHandler(this)
        if (intent != null && intent.getStringExtra("Mode") == "E") {
            isEditMode = true
            val tasks: Books = dbHandler!!.getTask(intent.getIntExtra("Id",0))
            isbnInput.setText(String.format(tasks.isbnID.toString()))
            titleInput.setText(tasks.title)
            authorInput.setText(tasks.author)
            courseInput.setText(tasks.course)
        }
    }

    private fun initOperations() {

        val deleteButton: Button = findViewById(R.id.btn_delete)
        val saveButton: Button = findViewById(R.id.btn_save)
        val isbnInput: EditText = findViewById(R.id.isbn)
        val titleInput: EditText = findViewById(R.id.bookTitle)
        val authorInput: EditText = findViewById(R.id.bookAuthor)
        val courseInput: EditText = findViewById(R.id.bookCourse)

        saveButton.setOnClickListener {
            var success: Boolean = false
            if (!isEditMode) {
                val tasks = Books()
                tasks.isbnID = (isbnInput.text.toString()).toLong()
                tasks.title = titleInput.text.toString()
                tasks.author = authorInput.text.toString()
                tasks.course = courseInput.text.toString()

                success = dbHandler?.addTask(tasks) as Boolean
            } else {
                val tasks = Books()
                tasks.id = intent.getIntExtra("Id", 0)
                tasks.isbnID = isbnInput.text.toString().toLong()
                tasks.title = titleInput.text.toString()
                tasks.author = authorInput.text.toString()
                tasks.course = courseInput.text.toString()

                success = dbHandler?.updateTask(tasks) as Boolean
            }

            if (success)
                finish()
        }

        deleteButton.setOnClickListener {
            val dialog = AlertDialog.Builder(this).setTitle("Deleting record ${titleInput.text}.")
                .setMessage("Are you sure you want to delete ${titleInput.text}?")
                .setPositiveButton("YES") { dialog, _ ->
                    val success = dbHandler?.deleteTask(intent.getIntExtra("Id", 0)) as Boolean
                    if (success)
                        finish()
                    dialog.dismiss()
                }
                .setNegativeButton("NO") { dialog, _ ->
                    dialog.dismiss()
                }
            dialog.show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
