package com.example.sociallibrary

import android.util.Log
import com.google.gson.annotations.SerializedName
import org.json.JSONArray
import org.json.JSONObject


class BookSearchResult(val title:String,
                       val author:String,
                       val link:String,
                       val image:String,
                       val description:String,
                       val genres:List<String> = emptyList()
) {}



