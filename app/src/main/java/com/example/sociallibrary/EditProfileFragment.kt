package com.example.sociallibrary

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.content.ContextCompat.getSystemService
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

        val etProfileName = view.findViewById<EditText>(R.id.etProfileName)
        val etProfileEmail = view.findViewById<EditText>(R.id.etProfileEmail)
        val etProfileBlurb = view.findViewById<EditText>(R.id.etProfileBlurb)

        val getUserInfo = ParseQuery<ParseObject>("_User")
        try {
            //val userInfo = getUserInfo.find()
            val userInfoTest = getUserInfo[userObjectId]
            Log.v("profile existing info", userInfoTest.keySet().toString())
            val currentName = userInfoTest.get("firstName").toString()
            val currentEmail = userInfoTest.get("email").toString()
            val currentBlurb = userInfoTest.get("aboutMe").toString()
            if (currentName != null && currentName != "null") { etProfileName.setText(currentName) }
            if (currentEmail != null && currentEmail != "null") { etProfileEmail.setText(currentEmail) }
            if (currentBlurb != null && currentBlurb != "null") { etProfileBlurb.setText(currentBlurb) }

            etProfileName.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {

                } else {
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
            }

            etProfileEmail.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {

                } else {
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                }
            }

            etProfileBlurb.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    Log.v("focus test", "blurb")
                } else {
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                    Log.v("focus test", "blurb false")
                }
            }

            val saveButton = view.findViewById<Button>(R.id.btnProfileSave)
            saveButton.setOnClickListener(){
                val updatedName = etProfileName.text
                val updatedEmail = etProfileEmail.text
                val updatedBlurb = etProfileBlurb.text
                Log.v("profile test", updatedName.toString())

                userInfoTest.put("firstName", updatedName.toString())
                userInfoTest.put("email", updatedEmail.toString())
                userInfoTest.put("aboutMe", updatedBlurb.toString())
                userInfoTest.saveInBackground()

                val fragmentManager = activity?.supportFragmentManager
                if (fragmentManager != null) {
                    fragmentManager.popBackStack()
                }
            }
        }
        catch (e: ParseException){
            Log.v("profile error" , "error querying user table: $e")
        }



        val libraryButton = view.findViewById<Button>(R.id.btnChangeNowReading)
        libraryButton.setOnClickListener(){
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
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(userObjectId:String) =
            EditProfileFragment().apply {
                arguments = Bundle().apply {
                    putString("userObjectId", userObjectId)
                }
            }
    }

    private fun closeKeyBoard() {
        val view = getActivity()?.currentFocus
        if (view != null) {
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
        }
    }

}

