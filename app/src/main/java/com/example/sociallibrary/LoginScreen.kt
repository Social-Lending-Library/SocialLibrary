package com.example.sociallibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.ParseObject
import com.parse.LogInCallback;
import com.parse.ParseException
import com.parse.ParseUser

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        var createAccountButton = findViewById<Button>(R.id.createAccountButton)
        createAccountButton.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
        }
        
        var username: String = ""
        var password: String = ""

        var usernameET = findViewById<EditText>(R.id.usernameEditText)
        var passwordET = findViewById<EditText>(R.id.passwordEditText)


        var loginButton = findViewById<Button>(R.id.loginButton)
        loginButton.setOnClickListener {


            username = usernameET.text.toString()
            password = passwordET.text.toString()

            if (username != "" && password != "") {

                ParseUser.logInInBackground(username, password) { parseUser: ParseUser?, parseException: ParseException? ->

                    if (parseUser != null) {
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }

                    else {

                        Toast.makeText(this, "INVALID CREDENTIALS", Toast.LENGTH_SHORT).show()

                        ParseUser.logOut()
                        if (parseException != null) {
                            parseException.message?.let { it1 -> Log.i("Daniel", it1) }
                        }
                    }

                }


            } else {
                Toast.makeText(this, "Enter Username and Password", Toast.LENGTH_SHORT).show()
            }


        }
    }
}

