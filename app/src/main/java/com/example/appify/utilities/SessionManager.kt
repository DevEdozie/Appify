package com.example.appify.utilities

import android.content.Context
import android.content.SharedPreferences

object SessionManager {

    private const val PREF_NAME = "user_session"
    private const val KEY_USERNAME = "user_name"
    private const val KEY_USER_ID = "user_id"
    private const val KEY_REMEMBER_ME = "remember_me"
    private const val KEY_FIRST_NAME = "first_name"
    private const val KEY_LAST_NAME = "last_name"

    /*
     * Function to save the username into SharedPreferences.
     * @param context - application context
     * @param username - username to be saved
     */
    fun saveUsername(context: Context, username: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_USERNAME, username)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Function to save the user id into SharedPreferences.
     * @param context - application context
     * @param userId - user id to be saved
     */
    fun saveUserId(context: Context, userId: Int) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putInt(KEY_USER_ID, userId)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Function to save the "remember me" status into SharedPreferences.
     * @param context - application context
     * @param isChecked - boolean value to be saved
     */
    fun saveRememberMeStatus(context: Context, isChecked: Boolean) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putBoolean(KEY_REMEMBER_ME, isChecked)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Function to save the first name into SharedPreferences.
     * @param context - application context
     * @param firstName - first name to be saved
     */
    fun saveFirstName(context: Context, firstName: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_FIRST_NAME, firstName)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Function to save the last name into SharedPreferences.
     * @param context - application context
     * @param lastName - last name to be saved
     */
    fun saveLastName(context: Context, lastName: String) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.putString(KEY_LAST_NAME, lastName)
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Function to retrieve the username from SharedPreferences.
     * @param context - application context
     * @return the saved username or null if not found
     */
    fun getUsername(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_USERNAME, null)
    }

    /*
     * Function to retrieve the user id from SharedPreferences.
     * @param context - application context
     * @return the saved user id or -1 if not found
     */
    fun getUserId(context: Context): Int {
        val prefs = getPreferences(context)
        return prefs.getInt(KEY_USER_ID, -1)
    }

    /*
     * Function to retrieve the "remember me" status from SharedPreferences.
     * @param context - application context
     * @return the saved boolean value or false if not found
     */
    fun getRememberMeStatus(context: Context): Boolean {
        val prefs = getPreferences(context)
        return prefs.getBoolean(KEY_REMEMBER_ME, false)
    }

    /*
     * Function to retrieve the first name from SharedPreferences.
     * @param context - application context
     * @return the saved first name or null if not found
     */
    fun getFirstName(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_FIRST_NAME, null)
    }

    /*
     * Function to retrieve the last name from SharedPreferences.
     * @param context - application context
     * @return the saved last name or null if not found
     */
    fun getLastName(context: Context): String? {
        val prefs = getPreferences(context)
        return prefs.getString(KEY_LAST_NAME, null)
    }

    /*
     * Function to clear all data from SharedPreferences.
     * @param context - application context
     */
    fun clearData(context: Context) {
        val prefs = getPreferences(context)
        val editor = prefs.edit()
        editor.clear() // Clear all data
        editor.apply() // Apply changes asynchronously
    }

    /*
     * Helper function to get SharedPreferences instance.
     * @param context - application context
     * @return SharedPreferences instance
     */
    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
}
