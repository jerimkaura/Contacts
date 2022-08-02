package com.jerimkaura.contacts.util

import android.content.Context
import android.widget.Toast

fun showAlert(
    context: Context,
    message: String,
    length: Int = Toast.LENGTH_SHORT
) {
    Toast.makeText(context, message, length).show()
}