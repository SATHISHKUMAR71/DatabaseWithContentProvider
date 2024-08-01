package com.example.databasewithcontentprovider

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        println("Activity OnCreate")
        setContentView(R.layout.activity_main)

        val uri = Uri.parse("content://com.example.databasewithcontentprovider.provider/notes")
        val cursor = contentResolver.query(uri,null,null,null,null)
        cursor?.use {
            while (it.moveToNext()){
                println("Title: ${it.getString(it.getColumnIndexOrThrow("title"))}")
            }
        }
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