package com.jerimkaura.contacts.presentation

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jerimkaura.contacts.data.Contact
import com.jerimkaura.contacts.databinding.ContactItemBinding

class ContactsAdapter : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {
    private var contacts: MutableList<Contact> = ArrayList()

    inner class ContactsViewHolder(val contactItemBinding: ContactItemBinding) :
        RecyclerView.ViewHolder(contactItemBinding.root) {
        fun bindContactToView(contact: Contact) {
            contactItemBinding.contactName.text = contact.name
            contactItemBinding.contactPhone.text = contact.phoneNumber
            contactItemBinding.profileLetter.text = getNameInitials(contact.name)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun addContacts(items: List<Contact>) {
        this.contacts.addAll(items)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearItems() {
        this.contacts.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        return ContactsViewHolder(
            ContactItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contacts[position]
        holder.bindContactToView(contact)
        holder.contactItemBinding.root.setOnClickListener { view ->
            val action =
                ContactsFragmentDirections.actionContactsFragmentToSingleContactFragment(contact.id)
            view.findNavController().navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun getNameInitials(name: String): String {
        val firstName = name.split("\\s".toRegex()).first()[0]
        var lastName = name.split("\\s".toRegex()).getOrNull(1)
        return if (lastName !=null){
            lastName = lastName[0].toString()
            firstName.plus(lastName).uppercase()
        }else{
            firstName.plus(name.split("\\s".toRegex()).first()[1].toString()).uppercase()
        }
    }
}