package com.jerimkaura.contacts.presentation

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jerimkaura.contacts.R
import com.jerimkaura.contacts.data.Contact
import com.jerimkaura.contacts.databinding.FragmentSingleContactBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SingleContactFragment : Fragment(R.layout.fragment_single_contact) {
    private lateinit var binding: FragmentSingleContactBinding
    private lateinit var dialog: Dialog
    private val contactsViewModel: ContactsViewModel by viewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSingleContactBinding.bind(view)
        arguments?.let { bundle ->
            val passedArguments = SingleContactFragmentArgs.fromBundle(bundle)
            contactsViewModel.getContactById(passedArguments.contactId)
                .observe(viewLifecycleOwner) { contact ->
                    binding.textViewContactName.text = contact.name
                    binding.textViewContactPhoneNumber.text = contact.phoneNumber
                    binding.nameIcon.text = getNameInitials(contact.name)
                    binding.btnDeleteContact.setOnClickListener {
                        deleteContact(contact.id)
                    }

                    binding.btnUpdateContact.setOnClickListener {
                        launchDialog(contact)
                    }
                }
        }
    }

    private fun getNameInitials(name: String): CharSequence? {
        val firstName = name.split("\\s".toRegex()).first()[0]
        var lastName = name.split("\\s".toRegex()).getOrNull(1)
        return if (lastName != null) {
            lastName = lastName[0].toString()
            firstName.plus(lastName).uppercase()
        } else {
            firstName.plus(name.split("\\s".toRegex()).first()[1].toString()).uppercase()
        }
    }

    private fun launchDialog(contact: Contact) {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.update_contact_form)
        dialog.setCancelable(false)
        dialog.show()
        val name = dialog.findViewById<TextInputEditText>(R.id.txtName)
        val tilPhone = dialog.findViewById<TextInputLayout>(com.jerimkaura.contacts.R.id.tilPhone)
        val tilName = dialog.findViewById<TextInputLayout>(com.jerimkaura.contacts.R.id.tilName)
        val phoneNumber =
            dialog.findViewById<TextInputEditText>(R.id.txtPhoneNumber)
        name.setText(contact.name)
        phoneNumber.setText(contact.phoneNumber)
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 5) {
                    tilName.error = "Name must be more than 5 character"
                } else {
                    tilName.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        val phoneNumberWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length != 10) {
                    tilPhone.error = "Invalid phone number"
                } else {
                    tilPhone.isErrorEnabled = false
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        }
        phoneNumber.addTextChangedListener(phoneNumberWatcher)
        name.addTextChangedListener(nameWatcher)

        dialog.findViewById<Button>(R.id.btnSave).setOnClickListener {
            val contactToSave =
                Contact(
                    id = contact.id,
                    name = name.text.toString(),
                    phoneNumber = phoneNumber.text.toString()
                )
            contactsViewModel.updateContact(contactToSave)
            resetDialogValues(phoneNumber, name, tilName, tilPhone)
            dialog.dismiss()

        }

        dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            resetDialogValues(phoneNumber, name, tilName, tilPhone)
            dialog.dismiss()
        }

    }

    private fun deleteContact(contactId: Long) {
        contactsViewModel.deleteContact(contactId)
        Toast.makeText(requireContext(), "Contact deleted", Toast.LENGTH_SHORT).show()
        val action = SingleContactFragmentDirections.actionSingleContactFragmentToContactsFragment()
        findNavController().navigate(action)
    }

    private fun resetDialogValues(
        phoneNumber: TextInputEditText,
        name: TextInputEditText,
        tilName: TextInputLayout,
        tilPhone: TextInputLayout
    ) {
        phoneNumber.setText("")
        name.setText("")
        tilName.isErrorEnabled = false
        tilPhone.isErrorEnabled = false
    }
}