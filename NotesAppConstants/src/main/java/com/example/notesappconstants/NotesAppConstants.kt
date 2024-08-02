package com.example.notesappconstants

object NotesAppConstants {
    const val  DB_NAME = "notes_app.db"
    private const val DB_VERSION = 1
    const val TABLE_NAME = "notes_app"
    const val COLUMN_ID = "id"
    private const val COLUMN_TITLE = "title"
    private const val COLUMN_CONTENT = "content"
    private const val COLUMN_CREATED_AT = "created_at"
    private const val COLUMN_UPDATED_AT = "updated_at"
    private const val COLUMN_IS_PINNED = "is_pinned"
}