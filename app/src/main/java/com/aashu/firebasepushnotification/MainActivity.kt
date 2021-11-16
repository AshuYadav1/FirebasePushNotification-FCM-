package com.aashu.firebasepushnotification

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

import com.google.firebase.messaging.FirebaseMessaging
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

const val TOPIC = "/topics/myTopic2"
class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnSend = findViewById<Button>(R.id.btnSend)
        val etTitle = findViewById<TextView>(R.id.etTitle)
        val etMessage = findViewById<TextView>(R.id.etMessage)
        val etToken = findViewById<TextView>(R.id.etToken)


       // FirebaseService.sharedPref = getSharedPreferences("sharedPref", Context.MODE_PRIVATE)
        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            FirebaseService.token = it
            etToken.setText(it)


            FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)

            btnSend.setOnClickListener {
                val title = etTitle.text.toString()
                val message = etMessage.text.toString()
               // val recipientToken = etToken.text.toString()
                if (title.isNotEmpty() && message.isNotEmpty() /*&& recipientToken.isNotEmpty()*/) {
                    pushNotification(
                        NotificationData(title, message),
                       /* recipientToken*/
                    ).also {
                           sendNotification(it)
                    }
                }
            }
        }


    }

    fun sendNotification(notification: pushNotification) =
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitInstance.api.postNotification(notification)
                if (response.isSuccessful) {
                    Log.d(TAG, "Response: ${Gson().toJson(response)}")
                } else {
                    Log.e(TAG, response.errorBody().toString())
                }
            } catch (e: Exception) {
                Log.e(TAG, e.toString())
            }

        }
}
