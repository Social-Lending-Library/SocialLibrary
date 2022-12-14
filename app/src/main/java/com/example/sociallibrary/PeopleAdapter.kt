package com.example.sociallibrary

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseException
import com.parse.ParseObject
import com.parse.ParseQuery
import com.squareup.picasso.Picasso
import java.util.ArrayList

const val ARTICLE_EXTRA = "ARTICLE_EXTRA"

class ResultAdapter(context: Context?, list: List<ParseObject>?) : RecyclerView.Adapter<ResultAdapter.ResultHolder>() {

    private var context: Context? = null
    private var list: List<ParseObject>? = null

    init {

        this.list = list
        this.context = context
    }

    fun clearList() {
        list = ArrayList()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.people_friend_cell, parent, false)
        return ResultHolder(v)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ResultHolder, position: Int) {
        val profile = list!![position]
        holder.bind(profile)
        /*
            holder.name?.text = profile.getString("name")
            holder.details?.text = "Friend Count: " + profile.getInt("friendCount") + " Birthday: " + profile.getDate(
                "birthDay"
            )*/
    }

    override fun getItemCount(): Int {
        return list!!.size
    }

    inner class ResultHolder(itemView: View) : RecyclerView.ViewHolder(itemView),
        View.OnClickListener {

        //   private val mediaImageView = itemView.findViewById<ImageView>(R.id.mediaImage)
        private val titleTextView = itemView.findViewById<TextView>(R.id.name)
        private val abstractTextView = itemView.findViewById<TextView>(R.id.details)
        private val imageView = itemView.findViewById<ImageView>(R.id.bookImage)
        private val bookInfo = ParseQuery<ParseObject>("Book")
        init {
            itemView.setOnClickListener(this)
        }

        // TODO: Write a helper method to help set up the onBindViewHolder method
        fun bind(profile: ParseObject) {
            /* holder.name?.text = profile.getString("name")
             holder.details?.text = "Friend Count: " + profile.getInt("friendCount") + " Birthday: " + profile.getDate(
                 "birthDay"
             )*/
            titleTextView.text = profile.getString("firstName").toString()
            abstractTextView.text =  profile.getString("aboutMe").toString()

            val title = profile.get("currentlyReading").toString()
            Log.v("testing", title.toString())

           Log.v("object id" , profile.objectId.toString() )
            if (title != null && title != "null"){
                bookInfo.whereEqualTo("ownerObjectId",profile.objectId.toString() )
                bookInfo.whereEqualTo("Title", title)

               try {
                    val thisBook = bookInfo.find()[0]
                    val imageUrl = thisBook.get("imageUrl").toString()
                   Log.v("Image :" , imageUrl )
                    Picasso.get().load(imageUrl).into(imageView);
                } catch (e: ParseException){
                    Log.v("OH NO" , "SUPER BAD")
                }
            }else{
                Log.v("UPLOADING IMG" , "FF")
                Picasso.get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/6/65/No-Image-Placeholder.svg/495px-No-Image-Placeholder.svg.png").into(imageView);
            }

            //       abstractTextView.text =  "Friend Count: " + profile.getInt("friendCount") + " Birthday: " + profile.getDate(
            //       "birthDay")
/*
            Glide.with(context)
                .load(article.mediaImageUrl)
                .into(mediaImageView)*/
        }

        override fun onClick(v: View?) {
            // TODO: Get selected article
            //val absoluteAdapterPosition
            val p = layoutPosition
            val activity = v!!.context as AppCompatActivity
            val article = list!![p]
            //   val bundle = bundleOf("title" to article.getString("name"))
            /*      val fragment = DemoFragment()
               //   fragment.arguments = bundle
                  //fragmentMa
                  activity.supportFragmentManager?.beginTransaction().replace(R.id.demo_test, fragment)?.commit()
                 // Fragment.findNavController().navigate(R.id.demo_test, bundle)

                  val activity = v!!.context as AppCompatActivity
                */  val demoFragment = PeopleFragment()
            val bundle = bundleOf("title" to article)
            demoFragment.arguments = bundle
            activity.supportFragmentManager.beginTransaction().replace(R.id.rlContainer, demoFragment).addToBackStack(null).commit()
            /*
            val article = list!![1]
            // TODO: Navigate to Details screen and pass selected article
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(ARTICLE_EXTRA, article)
            context!!.startActivity(intent)*/
        }

    }
/*
    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.apply { replace(R.id.frame_layout, fragment) }
        fragmentTransaction.commit()
    }*/
}