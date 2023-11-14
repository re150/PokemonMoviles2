package com.example.firebase

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebase.databinding.ActivityMainBinding
import com.google.firebase.database.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference


class UserlistActivity : AppCompatActivity() {

    private lateinit var dbref : DatabaseReference
    private lateinit var userRecyclerview : RecyclerView
    private lateinit var userArrayList : ArrayList<User>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userlist)
        userRecyclerview = findViewById(R.id.userList)
        userRecyclerview.layoutManager = LinearLayoutManager(this)
        userRecyclerview.setHasFixedSize(true)
        userArrayList = arrayListOf<User>()
        getUserData()
    }
    private fun getUserData() {

        dbref = FirebaseDatabase.getInstance().getReference("/Users")
        dbref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    // Limpiar la lista antes de agregar nuevos datos
                    userArrayList.clear()
                    for (userSnapshot in snapshot.children){
                        val user = userSnapshot.getValue(User::class.java)
                        userArrayList.add(user!!)
                    }
                    userRecyclerview.adapter = MyAdapter(userArrayList)
                }
               showNotification("Datos actualizados", "Se han actualizado los datos de los usuarios.")

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    private fun showNotification(title: String, message: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "default_channel_id",
                "Default Channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        // Create an intent to open the activity when the notification is tapped
        val intent = Intent(this, UserlistActivity::class.java)
        val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        } else {
            PendingIntent.FLAG_UPDATE_CURRENT
        }
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent, flags
        )

        // Build the notification
        val notificationBuilder = NotificationCompat.Builder(this, "default_channel_id")
            .setSmallIcon(R.drawable.icon_mesaje)
            .setContentTitle(title)
            .setContentText(message)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        // Get the notification service and display the notification
        val notificationManagerCompat = NotificationManagerCompat.from(this)
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the necessary permissions here if needed
            return
        }
        notificationManagerCompat.notify(1, notificationBuilder.build())
    }

}