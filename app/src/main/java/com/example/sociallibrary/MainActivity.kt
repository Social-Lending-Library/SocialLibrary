package com.example.sociallibrary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {

        // Gets userObjectId from LoginScreen into this MainActivity screen
        val userObjectId = getIntent().getStringExtra("userObjectId") as String

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragmentManager: FragmentManager = supportFragmentManager

        // define your fragments here and add arguments to fragments.
        val args = Bundle()
        args.putString("userObjectId", userObjectId)

        val fragment1: Fragment = FriendsFragment()
        val fragment2: Fragment = MyLibraryFragment()
        val fragment3: Fragment = ProfileFragment()
        val fragment4: Fragment = SearchFragment()

        fragment1!!.arguments = args
        fragment2!!.arguments = args
        fragment3!!.arguments = args
        fragment4!!.arguments = args

        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)

        // handle navigation selection
        bottomNavigationView.setOnItemSelectedListener { item ->
            lateinit var fragment: Fragment
            when (item.itemId) {
                R.id.action_friends -> fragment = fragment1
                R.id.action_my_library -> fragment = fragment2
                R.id.action_profile -> fragment = fragment3
                R.id.action_search -> fragment = fragment4
            }
            fragmentManager.beginTransaction().replace(R.id.rlContainer, fragment).commit()
            true
        }

        // Set default selection
        bottomNavigationView.selectedItemId = R.id.action_friends

    }
}