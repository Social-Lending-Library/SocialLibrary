package com.example.sociallibrary

import android.app.ProgressDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery

/**
 * A simple [Fragment] subclass.
 * Use the [FriendsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FriendsFragment : Fragment() {

    private var resultList: RecyclerView? = null
    private var adapter: ResultAdapter? = null
    /* private var queryByName: Button? = null
     private var queryByFriendCount: Button? = null
     private var queryByOrdering: Button? = null
     private var queryByAll: Button? = null
     private var clearResults: Button? = null

     */
    private var progressDialog: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        Log.i("Daniel", "The Friends Fragment has userObjectID " + userObjectId.toString())

        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_friends, container, false)

        resultList = view.findViewById<RecyclerView>(R.id.resultList)
        val queryByPeople = view.findViewById<Button>(R.id.queryByBrowsePeople)
        val queryByFriends = view.findViewById<Button>(R.id.queryByMyFriends)
        val titleQuery = view.findViewById<TextView>(R.id.title_query)
        progressDialog = ProgressDialog(activity)
        //var adapter: ResultAdapter? = null
        val a = "People".toString()
        queryByPeople?.setOnClickListener(View.OnClickListener { view: View? -> doQueryByPeople(); titleQuery.text = "People" })
        //   queryByOrdering?.setOnClickListener(View.OnClickListener { view: View? -> doQueryByOrdering() })
        //   queryByAll?.setOnClickListener(View.OnClickListener { view: View? -> doQueryByAll() })
        //    clearResults?.setOnClickListener(View.OnClickListener { view: View? -> adapter?.clearList() })


        /* ///separate
         val recyclerView = view.findViewById<View>(R.id.) as RecyclerView
         val searchButton = view.findViewById<Button>(R.id.btnSearchBooks)
         val searchView = view.findViewById<EditText>(R.id.etSearchBooks)
 */
        //      recyclerView.layoutManager = GridLayoutManager(context, 1)
        /*    searchButton.setOnClickListener {
                // Hide the keyboard
                val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(requireView().windowToken, 0)
                updateAdapter(recyclerView, searchView.text.toString())}
            //return view*/
        return view
    }

    private fun doQueryByPeople() {
        val query = ParseQuery<ParseObject>("_User")
        //query.whereGreaterThan("friendCount", 20)
        progressDialog!!.show()

        query.findInBackground { objects: List<ParseObject>?, e: ParseException? ->
            progressDialog!!.hide()
            if (e == null) {
                adapter = ResultAdapter(activity, objects)
                resultList!!.layoutManager = LinearLayoutManager(activity)
                resultList!!.adapter = adapter
            } else {
                Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

}