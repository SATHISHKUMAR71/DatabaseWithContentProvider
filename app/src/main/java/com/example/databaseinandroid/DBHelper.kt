package com.example.databaseinandroid

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelper(context: Context): SQLiteOpenHelper(context, DB_NAME,null, DB_VERSION){
    companion object{
        private const val  DB_NAME = "notes_app.db"
        private const val DB_VERSION = 1
        private const val TABLE_NAME = "notes_app"
        private const val COLUMN_ID = "id"
        private const val COLUMN_TITLE = "title"
        private const val COLUMN_CONTENT = "content"
        private const val COLUMN_CREATED_AT = "created_at"
        private const val COLUMN_UPDATED_AT = "updated_at"
        private const val COLUMN_IS_PINNED = "is_pinned"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY, $COLUMN_TITLE " +
                "TEXT, $COLUMN_CONTENT TEXT, $COLUMN_CREATED_AT TEXT, $COLUMN_UPDATED_AT TEXT, $COLUMN_IS_PINNED INTEGER DEFAULT 0)"
        db?.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        val dropQuery = "DROP TABLE IF EXISTS $TABLE_NAME"
        db?.execSQL(dropQuery)
        onCreate(db)
    }

    fun insert(note:Notes){
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_TITLE,note.title)
            put(COLUMN_CONTENT,note.content)
            put(COLUMN_CREATED_AT,note.createdAt)
            put(COLUMN_IS_PINNED,note.isPinned)
            put(COLUMN_UPDATED_AT,note.updatedAt)
        }
        db.insert(TABLE_NAME,null,values)
        db.close()
    }

    fun readAllNotes():MutableList<Notes>{
        val db = readableDatabase
        val notesList = mutableListOf<Notes>()
        val query = "SELECT * FROM $TABLE_NAME"
        val cursor = db.rawQuery(query,null)
        while (cursor.moveToNext()){
            notesList.add(
                Notes(
                    title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE)),
                    content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                    isPinned = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_PINNED)),
                    createdAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CREATED_AT)),
                    updatedAt = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UPDATED_AT)),
                    id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID))
                )
            )
        }
        cursor.close()
        return notesList
    }

    fun update(note:Notes){
        val db = writableDatabase
        val contentValues = ContentValues().apply {
            put(COLUMN_TITLE,note.title)
            put(COLUMN_CONTENT,note.content)
            put(COLUMN_CREATED_AT,note.createdAt)
            put(COLUMN_IS_PINNED,note.isPinned)
            put(COLUMN_UPDATED_AT,note.updatedAt)
        }
        val whereClause = "$COLUMN_ID = ?"
        val selectionArgs = arrayOf(note.id.toString())
        println( db.update(TABLE_NAME,contentValues,whereClause,selectionArgs))
        db.close()
    }

    fun delete(note:Notes){
        val db = writableDatabase
        val whereClause = "$COLUMN_ID = ?"
        val whereArgs = arrayOf(note.id.toString())
        db.delete(TABLE_NAME,whereClause,whereArgs)
        db.close()
    }
}