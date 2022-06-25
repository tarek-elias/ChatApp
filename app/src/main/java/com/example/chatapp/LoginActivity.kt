package com.example.chatapp

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SharedMemory
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var btnSignUp: Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        supportActionBar?.hide()
        mAuth = FirebaseAuth.getInstance()

        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        btnLogin = findViewById(R.id.btn_login)
        btnSignUp = findViewById(R.id.btn_signup)



        btnSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            if(checkInput()) {
                val email = edtEmail.text.toString()
                val password = edtPassword.text.toString()

                login(email, password)
            }
            else
            {
                edtEmail.setError("Please type your email and password")
            }
        }
    }

    override fun onStart() {
        super.onStart()
        if(mAuth.currentUser?.uid != null)
        {
            var intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun login(email: String, password: String){

        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful)
                {
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

    private fun checkInput(): Boolean {
        if(edtEmail.text.isEmpty())
        {
            edtEmail.setError("Please type your email address")
            return false
        }
        else if(edtPassword.text.isEmpty())
        {
            edtPassword.setError("Please type your password")
            return false
        }
        else if(edtEmail.text.isEmpty() && edtPassword.text.isEmpty())
        {
            edtEmail.setError("Please type your email address")
            edtPassword.setError("Please type your password")
            return false

        }
        return true
    }

    private fun checkLoggedUser() {
    }

}