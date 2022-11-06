package com.example.sociallibrary

import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.RequestParams
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import okhttp3.Headers
import org.json.JSONArray
import org.json.JSONObject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SearchFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SearchFragment : Fragment() {
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
        // Change this statement to store the view in a variable instead of a return statement
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val recyclerView = view.findViewById<View>(R.id.rvSearchBooks) as RecyclerView
        val searchButton = view.findViewById<Button>(R.id.btnSearchBooks)
        val searchView = view.findViewById<EditText>(R.id.etSearchBooks)

        recyclerView.layoutManager = GridLayoutManager(context, 1)
        searchButton.setOnClickListener {
            // Hide the keyboard
            val imm = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(requireView().windowToken, 0)
            updateAdapter(recyclerView, searchView.text.toString())}
        return view
    }

    private fun updateAdapter(recyclerView: RecyclerView, searchParams:String){
            Log.v("book SEARCH PARAMS", searchParams.toString())

            // Create and set up an AsyncHTTPClient() here
            val client = AsyncHttpClient()
            val params = RequestParams()
            params["api-key"] = getString(R.string.books_key)
            params["q"]= searchParams.toString()
            // Using the client, perform the HTTP request
            client[
                    "https://www.googleapis.com/books/v1/volumes",
                    params,
                    object : JsonHttpResponseHandler()

                    {
                        /*
                         * The onSuccess function gets called when
                         * HTTP response status is "200 OK"
                         */
                        override fun onSuccess(
                            statusCode: Int,
                            headers: Headers,
                            json: JsonHttpResponseHandler.JSON
                        ) {
                            // The wait for a response is over

                            // Get the results out of the response, and then get the books
                            val resultsJSON : JSONObject = json.jsonObject as JSONObject
                            val booksRawJSON : JSONArray = resultsJSON.get("items") as JSONArray

                            Log.v("json", resultsJSON.toString())
                            // Create a model using gson
                            val bookResults:ArrayList<Book>? = fromJson(booksRawJSON)
                            //val gson = Gson()
                            //val arrayBooks = object : TypeToken<List<BookSearchResult>>() {}.type
                            //val models : List<BookSearchResult> = gson.fromJson(booksRawJSON, arrayBooks)
                            recyclerView.adapter = bookResults?.let { BookSearchRVAdapter(it, this@SearchFragment) }

                            // Look for this in Logcat:
                            //Log.d("BestSellerBooksFragment", "response successful")
                        }

                        /*
                         * The onFailure function gets called when
                         * HTTP response status is "4XX" (eg. 401, 403, 404)
                         */
                        override fun onFailure(
                            statusCode: Int,
                            headers: Headers?,
                            errorResponse: String,
                            t: Throwable?
                        ) {
                            // If the error is not null, log it!
                            t?.message?.let {
                                Log.e("BestSellerBooksFragment", errorResponse)
                            }
                        }
                    }]

        }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SearchFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SearchFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }


    public fun fromJson(jsonArray: JSONArray): ArrayList<Book>? {
        var booksJson: JSONObject?
        val parsedBooks: ArrayList<Book> = ArrayList<Book>(jsonArray.length())
        // Process each result in json array, decode and convert to book object
        for (i in 0 until jsonArray.length()) {
            booksJson = try {
                jsonArray.getJSONObject(i)
            } catch (e: Exception) {
                e.printStackTrace()
                continue
            }

            val link = booksJson?.get("selfLink") as String
            val info:JSONObject = booksJson?.get("volumeInfo") as JSONObject
            val title:String = info["title"] as String
            var author = ""
            try{
                val test:JSONArray = info["authors"] as JSONArray
                author = test.getString(0).toString()

            }catch (e: Exception) {
                author = "Unknown"
                e.printStackTrace()
                continue
            }
            val description = info["description"] as String

            var image = ""
            try{
                val images:JSONObject = info["imageLinks"] as JSONObject
                image = images["thumbnail"] as String
            }catch (e: Exception) {
                e.printStackTrace()
                continue
            }


            Log.v("books custom parser", link.toString())
            Log.v("books title", title.toString())
            Log.v("books author", author)
            Log.v("books image", image)
            val newBook:Book = Book(title, author, link, image, description)
            parsedBooks.add(newBook)
        }
        return parsedBooks
    }

    fun onItemClick(book: Book) {
        Toast.makeText(context, "test: " + book.title, Toast.LENGTH_LONG).show()
        val bookDetail = BookDetailFragment.newInstance(book)

        val transaction = activity?.supportFragmentManager?.beginTransaction()
        if (transaction != null) {
            Log.v("bookDetails", "replacing")
            transaction.replace(R.id.rlContainer, BookDetailFragment.newInstance(book))
            transaction.disallowAddToBackStack()
            transaction.commit()
        }


    }
}