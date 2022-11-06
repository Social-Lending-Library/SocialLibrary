package com.example.sociallibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

import com.parse.Parse
import com.parse.ParseException
import com.parse.ParseUser
import com.parse.SignUpCallback

class CreateAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        var username: String = ""
        var password: String = ""

        var usernameET = findViewById<EditText>(R.id.usernameEditText)
        var passwordET = findViewById<EditText>(R.id.passwordEditText)

        var createAccountButton = findViewById<Button>(R.id.createAccountButton)
        createAccountButton.setOnClickListener{

            username = usernameET.text.toString()
            password = passwordET.text.toString()

            if (username != "" && password != ""){

                val user = ParseUser()
                user.setUsername(username)
                user.setPassword(password)



                user.signUpInBackground(SignUpCallback() {
                    if (it == null) {
                        val intent = Intent(this, MainActivity::class.java)

                        // Pass in userObjectId to MainActivity
                        intent.putExtra("userObjectId", user.objectId.toString())

                        startActivity(intent)
                        finish()
                    } else {
                        ParseUser.logOut();
                        Toast.makeText(this, it.message, Toast.LENGTH_LONG).show();
                    }
                });

            }

            else {
                Toast.makeText(this, "Enter Username and Password", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
