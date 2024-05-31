package com.example.appify

import android.content.Context
import android.content.SharedPreferences

// Singleton object to manage user session related data
object SessionManager {

    // Constants for keys used in SharedPreferences
    private const val USER_NAME = "user_name"
    private val usernameTag = R.string.username_tag

    /*
     * Function to save the username into SharedPreferences.
     * @param context - application context
     * @param username - username to be saved
     */
    fun saveUsername(context: Context, username: String) {
        saveString(context, USER_NAME, username, usernameTag)
    }

    /*
     * Function to retrieve the username from SharedPreferences.
     * @param context - application context
     * @return the saved username or null if not found
     */
    fun getUsername(context: Context): String? {
        return getString(context, USER_NAME, usernameTag)
    }

    /*
     * Helper function to save a string value into SharedPreferences.
     * @param context - application context
     * @param key - key to associate the value with
     * @param value - string value to be saved
     * @param name - resource ID for the SharedPreferences name
     */
    fun saveString(context: Context, key: String, value: String, name: Int) {
        val prefs: SharedPreferences = context.getSharedPreferences(
            context.getString(name), Context.MODE_PRIVATE
        )
        val editor = prefs.edit()
        editor.putString(key, value)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Helper function to retrieve a string value from SharedPreferences.
     * @param context - application context
     * @param key - key associated with the value
     * @param name - resource ID for the SharedPreferences name
     * @return the string value or null if not found
     */
    fun getString(context: Context, key: String, name: Int): String? {
        val prefs: SharedPreferences = context.getSharedPreferences(
            context.getString(name), Context.MODE_PRIVATE
        )
        return prefs.getString(key, null)
    }

    /*
     * Function to clear all data from SharedPreferences.
     * @param context - application context
     */
    fun clearData(context: Context) {
        // Get SharedPreferences editor
        val editor = context.getSharedPreferences(
            context.getString(R.string.username_tag),
            Context.MODE_PRIVATE
        ).edit()
        editor.clear() // Clear all data
        editor.apply() // Apply changes asynchronously
    }
}
