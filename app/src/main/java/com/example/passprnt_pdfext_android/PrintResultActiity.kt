package com.example.passprnt_pdfext_android

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PrintResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if there are any results from the print process
//        val resultStatus = intent.getQueryParameter("result") // This assumes PassPRNT sends a result parameter (success or failure)
        val resultStatus = "success"
        // Display a message based on the print result
        if (resultStatus == "success") {
            Toast.makeText(this, "Printing Successful", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Printing Failed", Toast.LENGTH_SHORT).show()
        }

        // Close this activity after handling the result
        finish()
    }
}