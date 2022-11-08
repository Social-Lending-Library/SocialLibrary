package com.example.sociallibrary

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.sociallibrary.R.id

const val BOOK_DETAILS = "BOOK_DETAILS"

class BookSearchRVAdapter(
    private val books: ArrayList<Book>,
    private val mListener: SearchFragment
    )
        : RecyclerView.Adapter<BookSearchRVAdapter.BookViewHolder>()
    {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.fragment_search_book, parent, false)

            return BookViewHolder(view)
        }

        /**
         * This inner class lets us refer to all the different View elements
         * (Yes, the same ones as in the XML layout files!)
         */
        inner class BookViewHolder(val mView: View) : RecyclerView.ViewHolder(mView) {
            var mItem: Book? = null
            val mBookTitle: TextView = mView.findViewById<View>(id.book_title) as TextView
            val mBookAuthor: TextView = mView.findViewById<View>(id.book_author) as TextView
            val mBookImage: ImageView = mView.findViewById<View>(id.book_image) as ImageView
            val mLibraryButton: Button = mView.findViewById<View>(id.btnAddToLibrary) as Button
            val mReadingButton: Button = mView.findViewById<View>(id.btnCurrentlyReading) as Button
            override fun toString(): String {
                return mBookTitle.toString() + " '" + mBookAuthor.text + "'"
            }
        }

        /**
         * This lets us "bind" each Views in the ViewHolder to its' actual data!
         */
        override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
            val book = books[position]
            holder.mReadingButton.visibility = View.GONE
            holder.mLibraryButton.visibility = View.GONE
            holder.mItem = book
            holder.mBookTitle.text = book.title
            holder.mBookAuthor.text = book.author
            Glide.with(holder.mView)
                .load(book.image)
                .centerInside()
                .into(holder.mBookImage)

            holder.mView.setOnClickListener {
                holder.mItem?.let { book ->
                    mListener?.onItemClick(book)
                }
            }
        }

        /**
         * Remember: RecyclerView adapters require a getItemCount() method.
         */
        override fun getItemCount(): Int {
            return books.size
        }


}