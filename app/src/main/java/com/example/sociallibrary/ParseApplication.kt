package com.example.sociallibrary
import android.app.Application
import com.parse.*
import okhttp3.OkHttpClient

class ParseApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG)
        ParseObject.registerSubclass(BookItem::class.java)

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(
            Parse.Configuration.Builder(this)
                .applicationId("back4app_app_id") // should correspond to Application ID env variable
                .clientKey("back4app_client_key")  // should correspond to Client key env variable
                .server("https://parseapi.back4app.com").build())
    }
}

@ParseClassName("BookItem")
class BookItem : ParseObject() {

}