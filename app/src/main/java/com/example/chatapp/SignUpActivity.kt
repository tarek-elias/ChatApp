package com.example.chatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {


    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnSignUp: Button
    private lateinit var edtUsername : EditText

    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        supportActionBar?.hide()

        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        edtUsername = findViewById(R.id.edt_username)
        btnSignUp = findViewById(R.id.btn_signup)

    btnSignUp.setOnClickListener {
        val email = edtEmail.text.toString()
        val password = edtPassword.text.toString()
        val username = edtUsername.text.toString()
        singUp(email, password, username)
    }

    }

    private fun singUp(email: String, password: String, username: String)
    {
        //TODO: Create user

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
                    addUserToDb(mAuth.currentUser?.uid!!, username, email)

                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                }
                else
                {
                    Toast.makeText(this, "Error\n"+ (task.exception?.message ?: "Unknown error"), Toast.LENGTH_SHORT).show()
                }
            }

    }

    private fun addUserToDb(uid: String, username: String, email: String)
    {
    mDbRef = FirebaseDatabase.getInstance().getReference()
        mDbRef.child("user").child(uid).setValue(User(username, email, uid, "https://i.imgur.com/PEhPQq8.png"))
    }
}