package com.example.sociallibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import com.parse.Parse
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser
import com.squareup.picasso.Picasso

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ProfileFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        //Log.i("Daniel", "The Profile Fragment has userObjectID " + userObjectId.toString())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val bundle = arguments
        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        Log.i("Daniel", "The Profile Fragment has userObjectID " + userObjectId.toString())

        val getUserInfo = ParseQuery<ParseObject>("_User")
        try {
            //val userInfo = getUserInfo.find()
            val userInfoTest = getUserInfo[userObjectId]
            Log.v("profile name", userInfoTest.get("firstName").toString())
            val firstName = view.findViewById<TextView>(R.id.tvProfileName)
            firstName.text = userInfoTest.get("firstName").toString()
            val blurb = view.findViewById<TextView>(R.id.tvAboutMe)
            blurb.text = userInfoTest.get("aboutMe").toString()
            val imageView = view.findViewById<ImageView>(R.id.ivCurrentlyReading)
            // look up the book
            val bookInfo = ParseQuery<ParseObject>("Book")
            val title = userInfoTest.get("currentlyReading").toString()
            if (title != null && title != "null"){
                bookInfo.whereEqualTo("ownerObjectId", userObjectId)
                bookInfo.whereEqualTo("Title", title)
                try {
                    val thisBook = bookInfo.find()[0]
                    val imageUrl = thisBook.get("imageUrl").toString()
                    Picasso.get().load(imageUrl).into(imageView);
                } catch (e: ParseException){
                    Log.v("OH NO" , "SUPER BAD")
                }
            }



        }
        catch (e: ParseException){
            Log.v("profile error" , "error querying user table: $e")
        }

        val editButton = view.findViewById<ImageButton>(R.id.ibEditProfile)
        editButton.setOnClickListener(){

            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.rlContainer, EditProfileFragment.newInstance(userObjectId))
                transaction.addToBackStack(null)
                transaction.commit()
            }
        }

        val myLibraryButton = view.findViewById<Button>(R.id.btnViewMyLibrary)
        myLibraryButton.setOnClickListener(){
            val transaction = activity?.supportFragmentManager?.beginTransaction()
            if (transaction != null) {
                transaction.replace(R.id.rlContainer, MyLibraryFragment.newInstance(userObjectId))
                transaction.disallowAddToBackStack()
                transaction.commit()
            }
        }



        // Inflate the layout for this fragment
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ProfileFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}