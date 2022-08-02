package com.jerimkaura.contacts.presentation

import android.Manifest
import android.app.AlarmManager
import android.app.Dialog
import android.app.PendingIntent
import android.content.Context.ALARM_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.jerimkaura.contacts.R
import com.jerimkaura.contacts.data.Contact
import com.jerimkaura.contacts.databinding.FragmentContactsBinding
import com.jerimkaura.contacts.receiver.AlarmReceiver
import com.jerimkaura.contacts.util.showAlert
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import java.util.*
import java.util.regex.Pattern


@AndroidEntryPoint
class ContactsFragment : Fragment(R.layout.fragment_contacts) {
    private lateinit var myContacts: List<Contact>
    private lateinit var binding: FragmentContactsBinding
    private val contactsViewModel: ContactsViewModel by viewModels()
    private lateinit var dialog: Dialog
    private val contactsAdapter: ContactsAdapter by lazy {
        ContactsAdapter()
    }

    override fun onStart() {
        super.onStart()
        contactsViewModel.allContacts.observe(viewLifecycleOwner) { contacts ->
            myContacts = contacts
            contactsAdapter.clearItems()
            contactsAdapter.addContacts(contacts)
            if (hasSendSMSPermission()){
                scheduleSMSAndNotification(myContacts)
            }else{
                requestPermission()
            }
        }
    }

    private fun hasSendSMSPermission() = ContextCompat.checkSelfPermission(
        requireContext(),
        Manifest.permission.SEND_SMS
    ) == PackageManager.PERMISSION_GRANTED

    private fun requestPermission() {
        if (!hasSendSMSPermission()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.SEND_SMS),
                PERMISSION_SEND_SMS
            )
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_SEND_SMS && grantResults.isNotEmpty()){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                showAlert(requireContext(), "SMS Permission Granted")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentContactsBinding.bind(view)
        binding.rvContacts.apply {
            hasFixedSize()
            layoutManager =
                LinearLayoutManager(
                    requireContext(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            adapter = contactsAdapter
        }
        launchAddContactDialog(binding)
    }


    private fun scheduleSMSAndNotification(myContacts: List<Contact>) {
        val alarmManager = requireActivity().getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, AlarmReceiver::class.java)
        intent.putExtra("CONTACTS", myContacts as Serializable)
        val pendingIntent =
            PendingIntent.getBroadcast(requireContext(), 0, intent, PendingIntent.FLAG_IMMUTABLE)
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, 9)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)

        val repeatInterval = AlarmManager.INTERVAL_DAY
        val cal = Calendar.getInstance()
        val triggerTime = cal.time.time
        alarmManager.setInexactRepeating(
            AlarmManager.RTC, triggerTime,
            repeatInterval, pendingIntent
        )
    }

    private fun launchAddContactDialog(binding: FragmentContactsBinding) {
        dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.add_contact_form)
        dialog.setCancelable(false)
        binding.fab.setOnClickListener {
            if (myContacts.size >= 3) {
                showAlert(
                    requireContext(),
                    "Maximum contacts is 3, delete a contact to create space"
                )
            } else {
                dialog.show()
            }
        }
        val name = dialog.findViewById<TextInputEditText>(R.id.txtName)
        val tilPhone = dialog.findViewById<TextInputLayout>(com.jerimkaura.contacts.R.id.tilPhone)
        val tilName = dialog.findViewById<TextInputLayout>(com.jerimkaura.contacts.R.id.tilName)
        val phoneNumber =
            dialog.findViewById<TextInputEditText>(R.id.txtPhoneNumber)
        val nameWatcher = object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s.toString().length < 3) {
                    tilName.error = "Name must be more at least 3 characters long"
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
                if (!Pattern.matches(PHONE_NUMBER_REGEX, s!!)
                ) {
                    tilPhone.error = "Invalid, Please enter valid phone number"
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
            if (name.text!!.isBlank()) {
                tilName.error = "Name cannot be blank."
            } else if (phoneNumber.text!!.isBlank()) {
                tilPhone.error = "Phone number cannot be blank."
            } else {
                val contact =
                    Contact(name = name.text.toString(), phoneNumber = phoneNumber.text.toString())
                println(contact)
                contactsViewModel.addContact(contact)
                resetDialogValues(phoneNumber, name, tilName, tilPhone)
                dialog.dismiss()
            }


        }
        dialog.findViewById<Button>(R.id.btnCancel).setOnClickListener {
            resetDialogValues(phoneNumber, name, tilName, tilPhone)
            dialog.dismiss()
        }
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


    companion object {
        private const val PERMISSION_SEND_SMS = 123
        const val PHONE_NUMBER_REGEX =
            "^(?:254|\\+254|0)?((?:(?:7(?:(?:[01249][0-9])|(?:5[789])|(?:6[89])))|(?:1(?:[1][0-5])))[0-9]{6})$"
    }

}