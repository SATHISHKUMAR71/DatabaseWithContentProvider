package com.example.databasewithcontentprovider

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Activity OnCreate")
        val uri = Uri.parse("content://com.example.databasewithcontentprovider.provider/notes_app")
        val cursor = contentResolver.query(uri,null,null,null,null)

        cursor?.use {
            println("cursor count: ${cursor.count}")
            while (cursor.moveToNext()){
                println("Cursor Value: ${cursor.getString(cursor.getColumnIndexOrThrow("title"))}")
            }
            cursor.close()
        }
        setContentView(R.layout.activity_main)


    }
    override fun onDestroy() {
        super.onDestroy()
        println("Activity OnDestroy")
    }
    override fun onResume() {
        super.onResume()
        println("Activity On Resume")
    }
    override fun onPause() {
        super.onPause()
        println("Activity OnPause")
    }
    override fun onStop() {
        super.onStop()
        println("Activity OnStop")
    }
}