package com.jerimkaura.contacts.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface ContactDao {
    @Query("SELECT * FROM contacts ORDER BY id DESC")
    fun getAllContacts(): LiveData<List<Contact>>

    @Query("SELECT * FROM contacts WHERE id=:id ")
    fun getContactById(id: Long): LiveData<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContact(contact: Contact): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateContact(contact: Contact)

    @Query("DELETE FROM contacts WHERE id = :contactId")
    fun deleteContact(contactId: Long)
}