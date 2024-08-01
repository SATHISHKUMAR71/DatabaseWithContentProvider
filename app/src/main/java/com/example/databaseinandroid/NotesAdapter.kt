package com.example.databaseinandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView

class NotesAdapter(private var notesList: MutableList<Notes>,private var activity: FragmentActivity):RecyclerView.Adapter<NotesAdapter.NotesViewHolder>() {

    inner class NotesViewHolder(itemView: View):RecyclerView.ViewHolder(itemView)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        return NotesViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.notes_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return notesList.size
    }

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.itemView.apply {
            findViewById<TextView>(R.id.titleNote).text = notesList[position].title
            findViewById<TextView>(R.id.dateNote).text = notesList[position].createdAt
            findViewById<TextView>(R.id.contentNote).text = notesList[position].content
            this.setOnClickListener {
                val addNoteFragment = AddNote()
                addNoteFragment.arguments = Bundle().apply {
                    putInt("id",notesList[position].id)
                    putString("title",notesList[position].title)
                    putString("date",notesList[position].createdAt)
                    putString("content",notesList[position].content)
                }
                Toast.makeText(context,"Message Clicked",Toast.LENGTH_SHORT).show()
                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.fragmentContainerView,addNoteFragment)
                    .addToBackStack("Note View")
                    .commit()
            }
        }
    }
}