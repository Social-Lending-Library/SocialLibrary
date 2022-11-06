package com.example.sociallibrary

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.sociallibrary.R.id
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
class BookDetailFragment (book:Book): Fragment()  {
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
        Log.v("bookDetails", "onCreateView")

        val view = inflater.inflate(R.layout.fragment_book_detail, container, false)
        val bundle = arguments
        val title = bundle?.getString("title")
        val author = bundle?.getString("author")
        val description = bundle?.getString("description")
        val image = bundle?.getString("image")
        Log.v("bookDetails", title.toString())
        val titleTextView = view.findViewById<TextView>(R.id.book_title_detail)
        val authorTextView = view.findViewById<TextView>(R.id.book_author_detail)
        val descriptionTextView = view.findViewById<TextView>(R.id.book_description_detail)
        val imageView = view.findViewById<ImageView>(R.id.book_image_detail)

        titleTextView.text = title.toString()
        authorTextView.text = author.toString()
        descriptionTextView.text = description.toString()
        Picasso.get().load(image).into(imageView);
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
         * @return A new instance of fragment BookDetailFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(book:Book) =
            BookDetailFragment(book).apply {
                arguments = Bundle().apply {
                    putString("title", book.title)
                    putString("author", book.author)
                    putString("image", book.image)
                    putString("link", book.link)
                    putString("description", book.description)

                }
            }
    }
}