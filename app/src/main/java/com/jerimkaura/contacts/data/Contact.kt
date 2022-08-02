package com.jerimkaura.contacts.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "contacts")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0,
    var name: String,
    var phoneNumber: String
): Serializable
