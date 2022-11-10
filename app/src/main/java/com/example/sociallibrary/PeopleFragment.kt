package com.example.sociallibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.squareup.picasso.Picasso

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [PeopleFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class PeopleFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_people, container, false)
        val profileName = view.findViewById<TextView>(R.id.tvProfileName)
        val aboutMe = view.findViewById<TextView>(R.id.tvAboutMe)
        val args = this.arguments
        val inputData = args?.get("title") as ParseObject
        profileName.text =  inputData.getString("firstName").toString()
        aboutMe.text = inputData.getString("aboutMe").toString()
        //  user_id.text = userObjectId
        val bookInfo = ParseQuery<ParseObject>("Book")
        val title = inputData.get("currentlyReading").toString()
     //   Log.v("OH NO" , title)
     //   Log.v("object id" , inputData.objectId.toString() )

        val backButton = view.findViewById<Button>(R.id.btnReturn)
        backButton.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                fragmentManager.popBackStack()
            }
        }
        val imageView = view.findViewById<ImageView>(R.id.ivCurrentlyReading)
        if (title != null && title != "null"){
            bookInfo.whereEqualTo("ownerObjectId",inputData.objectId.toString() )
            bookInfo.whereEqualTo("Title", title)
            try {
                val thisBook = bookInfo.find()[0]
                val imageUrl = thisBook.get("imageUrl").toString()
                Picasso.get().load(imageUrl).into(imageView);
            } catch (e: ParseException){
                Log.v("OH NO" , "SUPER BAD")
            }
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment PeopleFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PeopleFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}