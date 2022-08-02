package com.jerimkaura.contacts.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jerimkaura.contacts.data.Contact
import com.jerimkaura.contacts.repository.ContactsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactsViewModel @Inject constructor(private val contactsRepository: ContactsRepository) :
    ViewModel() {
    private val _allContacts: LiveData<List<Contact>> = contactsRepository.getAllContacts

    val allContacts: LiveData<List<Contact>>
        get() = _allContacts

    fun getContactById(id: Long): LiveData<Contact> = contactsRepository.getContactById(id)

    fun deleteContact(contactId: Long) = viewModelScope.launch {
        contactsRepository.deleteContact(contactId)
    }

    fun updateContact(contact: Contact) = viewModelScope.launch {
        contactsRepository.updateContact(contact)
    }

    fun addContact(contact: Contact) = viewModelScope.launch {
        contactsRepository.addContact(contact)
    }
}