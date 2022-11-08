package com.example.sociallibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseObject
import com.parse.ParseException
import com.parse.ParseQuery


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MyLibraryFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MyLibraryFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        // Gets userObjectId from MainActivity into this Fragment
        val userObjectId = requireArguments().getString("userObjectId", "None")
        Log.i("Daniel", "The MyLibrary Fragment has userObjectID " + userObjectId.toString())

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
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_my_library, container, false)
        val recyclerView = view.findViewById<View>(R.id.rvMyBooks) as RecyclerView
        val searchButton = view.findViewById<Button>(R.id.btnMyLists)
        recyclerView.layoutManager = GridLayoutManager(context, 2)

        updateAdapter(recyclerView)

        return view
    }

    private fun updateAdapter(recyclerView: RecyclerView) {
        val bundle = arguments
        val user = bundle?.getString("userObjectId")
        var bookResults:MutableList<Book> = ArrayList()
        // Retrieves all book objects
        val getBooks = ParseQuery<ParseObject>("Book")
        // Filters to just books owned by the current user
        getBooks.whereEqualTo("ownerObjectId", user)
        // async method
        /*getBooks.findInBackground(
        ) { obj, e ->
            if (e == null) {
                // TODO write adapter, plug the results in. MutableList
                Log.v("book testing", obj.get(0).get("Author").toString())
            } else {
                Log.v("OH NO", "UGH")
            }
        }*/
        // sync method

        try {
            val booksList = getBooks.find()
            // Now we have a list of key value pairs. Turn these into Books.

            val bookResults:ArrayList<Book>? = fromParseObject(booksList)
            recyclerView.adapter = bookResults?.let { BookLibraryRVAdapter(it, this@MyLibraryFragment) }
        } catch (e: ParseException){
            Log.v("OH NO" , "SUPER BAD")
        }



        }

    public fun fromParseObject(bookList: List<ParseObject>): ArrayList<Book>? {
        val parsedBooks: ArrayList<Book> = ArrayList<Book>(bookList.size)
        // Process each result in json array, decode and convert to book object
        for (i in bookList.indices) {
            val booksParseObject = bookList[i]
            val bookObjectId = booksParseObject.get("objectId").toString()
            val title = booksParseObject.get("Title").toString()
            var author = booksParseObject.get("Author").toString()
            var checkedOut = booksParseObject.get("checkedOut")
            var image = booksParseObject.get("imageUrl").toString()
            val description = booksParseObject.get("Description").toString()


            Log.v("books title", title.toString())
            Log.v("books author", author.toString())
            Log.v("books image", image.toString())
            val newBook:Book = Book(bookObjectId, title, author, "", image, description, checkedOut as Boolean)
            parsedBooks.add(newBook)
        }
        return parsedBooks
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyLibraryFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyLibraryFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun onItemClick(book: Book) {
        // Toast.makeText(context, "test: " + book.title, Toast.LENGTH_LONG).show()

        val bundle = arguments
        val user = bundle?.getString("userObjectId").toString()
        val bookDetail = BookDetailFragment.newInstance(book, user, "library")
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.rlContainer, BookDetailFragment.newInstance(book, user, "library"))
            transaction.addToBackStack(null)
            transaction.commit()
        }


    }
}

