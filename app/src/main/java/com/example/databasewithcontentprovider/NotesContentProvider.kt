package com.example.databasewithcontentprovider

import android.content.ContentProvider
import android.content.ContentResolver
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.database.SQLException
import android.net.Uri
import kotlinx.coroutines.yield

class NotesContentProvider:ContentProvider() {

    private lateinit var dbHelper:DBHelper
    companion object{
        const val AUTHORITY = "com.example.databasewithcontentprovider.notescontentprovider"
        const val PATH_NOTES = DBHelper.TABLE_NAME
        val CONTENT_URI:Uri = Uri.parse("content://$AUTHORITY/$PATH_NOTES")
        private const val NOTES =1
        private const val NOTES_ID =2
        private const val NOTES_COUNT = 3
        private val uriMatcher = UriMatcher(UriMatcher.NO_MATCH).apply {
            addURI("com.example.databasewithcontentprovider.provider", "notes_app", NOTES)
            addURI(AUTHORITY, PATH_NOTES, NOTES)
            addURI(AUTHORITY, "$PATH_NOTES/#", NOTES_ID)
            addURI(AUTHORITY, "$PATH_NOTES/getNotesCount", NOTES_COUNT)
        }
    }
    override fun onCreate(): Boolean {
        dbHelper = DBHelper(context!!)
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor? {
        val db = dbHelper.readableDatabase
        return when(uriMatcher.match(uri)){
            NOTES -> db.query(DBHelper.TABLE_NAME,projection,selection,selectionArgs,null,null,sortOrder)
            NOTES_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid URI")
                db.query(DBHelper.TABLE_NAME,projection,"${DBHelper.COLUMN_ID}=?", arrayOf(id),null,null,sortOrder)
            }
            NOTES_COUNT -> {
                dbHelper.getNotesCount()
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

    }

    override fun getType(uri: Uri): String {
        return when(uriMatcher.match(uri)){
            NOTES -> "${ContentResolver.CURSOR_DIR_BASE_TYPE}/vnd.$AUTHORITY.$PATH_NOTES"
            NOTES_ID -> "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.$AUTHORITY.$PATH_NOTES"
            NOTES_COUNT -> "${ContentResolver.CURSOR_ITEM_BASE_TYPE}/vnd.$AUTHORITY.$PATH_NOTES.notesCount"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val db = dbHelper.writableDatabase
        println("URI's: $uri")
        val id = db.insert(DBHelper.TABLE_NAME,null,contentValues)
        context?.contentResolver?.notifyChange(uri,null)
        return Uri.withAppendedPath(CONTENT_URI,id.toString())
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        val count: Int
        when (uriMatcher.match(uri)) {
            NOTES -> count = db.delete(DBHelper.TABLE_NAME, selection, selectionArgs)
            NOTES_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid URI")
                count = db.delete(DBHelper.TABLE_NAME, "${DBHelper.COLUMN_ID}=?", arrayOf(id))
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri, null)
        return count
    }

    override fun update(uri: Uri, contentValues: ContentValues?, selection: String?, selectionArgs: Array<out String>?): Int {
        val db = dbHelper.writableDatabase
        var count = 0
        when(uriMatcher.match(uri)){
            NOTES -> count = db.update(DBHelper.TABLE_NAME,contentValues,selection,selectionArgs)
            NOTES_ID -> {
                val id = uri.lastPathSegment ?: throw IllegalArgumentException("Invalid URI: $uri")
                count = db.update(DBHelper.TABLE_NAME,contentValues,"${DBHelper.COLUMN_ID}=?", arrayOf(id))
            }
            else -> throw IllegalArgumentException("Invalid URI: $uri")
        }
        context?.contentResolver?.notifyChange(uri,null)
        return count
    }
}