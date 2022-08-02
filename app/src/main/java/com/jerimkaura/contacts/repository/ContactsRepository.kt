package com.jerimkaura.contacts.repository

import androidx.lifecycle.LiveData
import com.jerimkaura.contacts.data.Contact
import com.jerimkaura.contacts.data.ContactDao
import javax.inject.Inject

class ContactsRepository @Inject constructor(private val contactDao: ContactDao) {
    val getAllContacts: LiveData<List<Contact>> = contactDao.getAllContacts()

    suspend fun addContact(contact: Contact){
        contactDao.addContact(contact)
    }

    suspend fun updateContact(contact: Contact){
        contactDao.updateContact(contact)
    }

    fun getContactById(id: Long): LiveData<Contact> {
        return contactDao.getContactById(id)
    }

    fun deleteContact(contactId: Long){
        return contactDao.deleteContact(contactId)
    }
}