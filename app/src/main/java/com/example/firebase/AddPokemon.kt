package com.example.firebase

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class AddPokemon : AppCompatActivity() {
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etAge: EditText
    private lateinit var etImageUrl: EditText
    private lateinit var btnAddUser: Button
    private lateinit var dbref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_pokemon)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etAge = findViewById(R.id.etAge)
        etImageUrl = findViewById(R.id.etImageUrl)
        btnAddUser = findViewById(R.id.btnAddUser)

        dbref = FirebaseDatabase.getInstance().reference

        btnAddUser.setOnClickListener {
            val firstName = etFirstName.text.toString()
            val lastName = etLastName.text.toString()
            val age = etAge.text.toString()
            val imageUrl = etImageUrl.text.toString()

            // Verificar que los campos no estén vacíos antes de agregar el usuario
            if (firstName.isNotEmpty() && lastName.isNotEmpty() && age.isNotEmpty() && imageUrl.isNotEmpty()) {
                addUserToDatabase(firstName, lastName, age, imageUrl)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun addUserToDatabase(firstName: String, lastName: String, age: String, imageUrl: String) {
        val newUser = User(firstName, lastName, age, imageUrl)
        dbref.child("Users").push().setValue(newUser)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario agregado exitosamente", Toast.LENGTH_SHORT).show()
                    var i = Intent(this,MainActivity::class.java)
                    startActivity(i)
                    etFirstName.text.clear()
                    etLastName.text.clear()
                    etAge.text.clear()
                    etImageUrl.text.clear()
                } else {
                    Toast.makeText(this, "Error al agregar usuario", Toast.LENGTH_SHORT).show()
                }
            }
    }
}