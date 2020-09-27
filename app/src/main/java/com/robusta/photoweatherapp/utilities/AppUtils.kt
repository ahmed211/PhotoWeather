package com.cabi.driver.utilities

import android.R
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.view.Gravity
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

object AppUtils {

    fun Activity.hideKeyboard() {
        val imm =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        var view = currentFocus
        if (view == null) view = View(this)
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun Activity.showSnackBar(message: String?) {
        val mSnackBar = Snackbar.make(
            findViewById(R.id.content),
            message!!,
            Snackbar.LENGTH_SHORT
        )
        val mainTextView =
            mSnackBar.view.findViewById<View>(com.google.android.material.R.id.snackbar_text) as TextView
        mainTextView.textAlignment =
            View.TEXT_ALIGNMENT_CENTER
        mainTextView.gravity = Gravity.CENTER_HORIZONTAL
        mSnackBar.show()
    }

    fun Context.showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}
