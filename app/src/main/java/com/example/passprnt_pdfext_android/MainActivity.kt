package com.example.passprnt_pdfext_android

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Base64
import android.widget.Toast
import java.io.IOException
import java.io.InputStream

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)

        // Handle the intent when the activity is launched by sharing a file
        handleIncomingFileIntent(intent)
    }

    // Handle new intents when the activity is already open
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIncomingFileIntent(intent)
    }

    private fun handleIncomingFileIntent(intent: Intent?) {
        if (intent?.action == Intent.ACTION_SEND && intent.type != null) {
            val fileUri: Uri? = intent.getParcelableExtra(Intent.EXTRA_STREAM)
            val callbackUrl = "passprntpdfext://printresult"

            if (fileUri != null) {
                val base64File = convertFileToBase64(fileUri)
                if (base64File != null) {
                    // Build the URI for Star Micronics PassPRNT SDK
                    val passprntUri = "starpassprnt://v1/print/nopreview?&back=$callbackUrl&pdf=$base64File"
                    val passprntIntent = Intent(Intent.ACTION_VIEW, Uri.parse(passprntUri))
                    // Start PassPRNT with the file
                    startActivity(passprntIntent)
                } else {
                    Toast.makeText(this, "Failed to convert file to Base64", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No file received", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "No valid action or file received", Toast.LENGTH_SHORT).show()
        }
    }

    // Function to convert the selected file (PDF or DTML) to Base64
    private fun convertFileToBase64(fileUri: Uri): String? {
        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(fileUri)
            val bytes = inputStream?.readBytes()
            inputStream?.close()

            if (bytes != null) {
                // Convert the file content to Base64
                val base64String = Base64.encodeToString(bytes, Base64.NO_WRAP)
                // URL-encode the Base64 string as required by RFC3986
                return urlEncode(base64String)
            } else {
                null
            }
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    // Function to URL encode the Base64 string
    private fun urlEncode(base64: String): String {
        return base64
            .replace("+", "%2B")
            .replace("/", "%2F")
            .replace("=", "%3D")
    }

}