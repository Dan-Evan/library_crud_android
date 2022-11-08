package com.example.librarycrud.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.contactmanagment.R
import com.example.librarycrud.AddOrEditActivity
import com.example.librarycrud.models.Books

class BooksRecyclerAdapter(tasksList: List<Books>, internal var context: Context) : RecyclerView.Adapter<BooksRecyclerAdapter.TaskViewHolder>() {

    private var tasksList: List<Books> = ArrayList()
    init {
        this.tasksList = tasksList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_books, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val tasks = tasksList[position]
        holder.isbn.text = tasks.isbnID.toString()
        holder.title.text = tasks.title
        holder.author.text = tasks.author
        holder.course.text = tasks.course

        holder.bookList.background = ContextCompat.getDrawable(context, R.color.colorSuccess)

        holder.itemView.setOnClickListener {
            val i = Intent(context, AddOrEditActivity::class.java)
            i.putExtra("Mode", "E")
            i.putExtra("Id", tasks.id)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    override fun getItemCount(): Int {
        return tasksList.size
    }

    inner class TaskViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var isbn: TextView = view.findViewById(R.id.textViewISBN) as TextView
        var title: TextView = view.findViewById(R.id.textViewTitle) as TextView
        var author: TextView = view.findViewById(R.id.textViewAuthor) as TextView
        var course: TextView = view.findViewById(R.id.textViewCourse) as TextView
        var bookList: LinearLayout = view.findViewById(R.id.bookList) as LinearLayout
    }

}
