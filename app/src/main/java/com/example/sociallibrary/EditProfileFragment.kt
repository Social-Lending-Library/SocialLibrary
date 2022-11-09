package com.example.sociallibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.parse.ParseUser

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ProfileFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditProfileFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var userObjectIdOrig: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        Log.i("user id", "The Profile Fragment has userObjectID " + userObjectId.toString())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val bundle = arguments
        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        Log.i("profile edit", "The Profile Fragment has userObjectID " + userObjectId.toString())

        val getUserInfo = ParseQuery<ParseObject>("_User")
        try {
            //val userInfo = getUserInfo.find()
            val userInfoTest = getUserInfo[userObjectId]
            Log.v("profile name", userInfoTest.get("firstName").toString())
        }
        catch (e: ParseException){
            Log.v("profile error" , "error querying user table: $e")
        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userObjectId:String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("userObjectId", userObjectId)
                }
            }
    }
}