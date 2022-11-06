package com.example.sociallibrary

import android.support.annotation.Keep
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Keep
@Serializable
data class Book (val title:String,
           val author:String,
           val link:String,
           val image:String,
           val description:String,
           val genres:List<String> = emptyList()
) {

}



