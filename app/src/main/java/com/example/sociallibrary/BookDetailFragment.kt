package com.example.sociallibrary

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.parse.Parse
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
 * Use the [BookDetailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class BookDetailFragment (): Fragment()  {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)
        val bundle = arguments
        val title = bundle?.getString("title")
        val author = bundle?.getString("author")
        val description = bundle?.getString("description")
        val image = bundle?.getString("image")
        val user = bundle?.getString("userObjectId")
        val source = bundle?.getString("source")
        val checkedOut = bundle?.getBoolean("checkedOut")
        // Need the user's key to support reading list additions
        Log.v("book User from result", user.toString())
        val titleTextView = view.findViewById<TextView>(R.id.book_title_detail)
        val authorTextView = view.findViewById<TextView>(R.id.book_author_detail)
        val descriptionTextView = view.findViewById<TextView>(R.id.book_description_detail)
        val imageView = view.findViewById<ImageView>(R.id.book_image_detail)

        val btnAddToLibrary: Button = view.findViewById<View>(R.id.btnAddToLibrary) as Button
        val setReadingButton: Button = view.findViewById<View>(R.id.btnCurrentlyReading) as Button
        val btnAddToList: Button = view.findViewById<View>(R.id.btnReadingLists) as Button
        val libraryFlag: TextView = view.findViewById<TextView>(R.id.tvLibraryFlag)


        if (source.equals("library")){
            btnAddToList.visibility = View.VISIBLE
            setReadingButton.visibility = View.VISIBLE
            libraryFlag.visibility = View.VISIBLE
            val getUserInfo = ParseQuery<ParseObject>("_User")
            try {
                val thisUser = getUserInfo[user]
                setReadingButton.setOnClickListener(){
                    if (title != null) {
                        thisUser.put("currentlyReading", title)
                        thisUser.saveInBackground()
                    }
                }
            } catch (e: ParseException){
                Log.v("book error" , "now reading failed")
            }
        }
        else if (source.equals("search")){
            btnAddToLibrary.visibility = View.VISIBLE
        }

        titleTextView.text = title
        authorTextView.text = author
        descriptionTextView.text = description
        Picasso.get().load(image).into(imageView);

        val backButton = view.findViewById<Button>(R.id.btnBookBack)
        backButton.setOnClickListener {

            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                fragmentManager.popBackStack()
            }
        }

        btnAddToLibrary.setOnClickListener{
            val bundle = arguments
            val user = bundle?.getString("userObjectId")
            val newBook = ParseObject("Book")
            if (user != null) {
                newBook.put("ownerObjectId", user)
            }
            if (title != null) {
                newBook.put("Title", title)
            }
            if (author != null) {
                newBook.put("Author", author)
            }
            if (checkedOut != null) {
                newBook.put("checkedOut", checkedOut)
            }
            if (image != null) {
                newBook.put("imageUrl", image)
            }
            if (description != null) {
                newBook.put("Description", description)
            }

            newBook.saveInBackground()
            val fragmentManager = activity?.supportFragmentManager
            if (fragmentManager != null) {
                fragmentManager.popBackStack()
            }

        }
        btnAddToList.setOnClickListener{
            val bundle = arguments
            val user = bundle?.getString("userObjectId")
            // Get the user's reading lists
            // Retrieves all book objects
            val getLists = ParseQuery<ParseObject>("BookList")
            val getThisBookList = ParseQuery<ParseObject>("Book")
            val bookObjectId = bundle?.getString("bookObjectId")
            // Filters to lists owned by the current user
            getLists.whereEqualTo("ownerObjectId", user)
            // Filter to just this book
            getThisBookList.whereEqualTo("Title", title)
            getThisBookList.whereEqualTo("ownerObjectId", user)

            try {
                val listList = getLists.find()
                val currentBook = getThisBookList.find()
                // Now we have a list of key value pairs. Turn these into BookLists
                val listResults:ArrayList<BookList>? = bookListFromParseObject(listList)

                val alertDialog = AlertDialog.Builder(requireContext())
                var listNames = listResults?.map{it.ListName}?.toTypedArray()
                alertDialog.setTitle("Select a list for this book")
                var selectedList:String?
                if (listResults != null) {
                    var checked = -1
                    var newChoice = -1

                    if (listResults.size > 0) {
                        selectedList = listResults[0].listObjectId
                        for (i in listResults.indices) {
                            Log.v("dialog test list name", listResults[i].ListName.toString())
                            if (listResults[i].ListName.equals(listResults[0].ListName)){
                            // Pre check this list, and persistence happens!
                                checked = i
                            }
                        }


                        alertDialog.setSingleChoiceItems(listNames, checked) { dialog, which ->
                            newChoice = which
                            // TODO Really icky here, back4app isn't giving me the object id of the lists
                            // in the query results. So a true FK is not possible. Doing more
                            // research but hacky solution of using name. So, uh, don't duplicate
                            // list names on a single user.
                            Log.v("dialog update target", currentBook[0].keySet().toString())
                            Log.v("dialog update source", listResults[newChoice].ListName.toString())
                            currentBook[0].put("listObjectId", listResults[newChoice].ListName.toString())
                            currentBook[0].saveInBackground()
                            dialog.dismiss()
                        }
                    }


                    else{
                        alertDialog.setMessage("No lists! Create some lists in your Library.")
                        alertDialog.setPositiveButton("OK"){ _,_ -> }
                    }
                }
                val listAlert = alertDialog.create()
                listAlert.show()


                //recyclerView.adapter = bookResults?.let { BookLibraryRVAdapter(it, this@MyLibraryFragment) }
            } catch (e: ParseException){
                Log.v("OH NO" , "SUPER BAD")
            }

        }

        // Inflate the layout for this fragment
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(book:Book, user:String, source:String) =
            BookDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("bookObjectId", book.bookObjectId)
                    putString("title", book.title)
                    putString("author", book.author)
                    putString("image", book.image)
                    putString("link", book.link)
                    putString("description", book.description)
                    putString("userObjectId", user)
                    putString("source", source)

                }
            }
    }

    public fun bookListFromParseObject(listList: List<ParseObject>): ArrayList<BookList>? {
        val parsedLists: ArrayList<BookList> = ArrayList<BookList>(listList.size)
        // Process each result in json array, decode and convert to book object
        for (i in listList.indices) {
            val listParseObject = listList[i]
            val listObjectId = listParseObject.get("objectId").toString()
            val ownerId = listParseObject.get("ownerObjectId").toString()
            val listName = listParseObject.get("ListName").toString()
            val newList:BookList = BookList(listObjectId, ownerId, listName)
            parsedLists.add(newList)
        }
        return parsedLists
    }
}


