package com.example.usercontacts

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.usercontacts.databinding.ActivityMainBinding
import com.example.usercontacts.Contact as Contact1

class MainActivity : AppCompatActivity() {

    companion object {
        const val PERMISSIONS_REQUEST_READ_CONTACTS = 100
    }

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnReadContacts.setOnClickListener {
            loadContacts()
        }

    }

    private fun loadContacts() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(Manifest.permission.READ_CONTACTS),
                PERMISSIONS_REQUEST_READ_CONTACTS
            )
        } else {
            getContacts()
        }
    }

    @SuppressLint("Range")
    private fun getContacts() {
        val contactsList: MutableList<Contact1> = ArrayList()
        val curserObject = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        )

        curserObject?.let {
            while (it.moveToNext()) {
                val name =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                val phoneNumber =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val photoUri =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                var image: Bitmap? = null
                photoUri?.let {
                    image = when {
                        Build.VERSION.SDK_INT < 28 -> MediaStore.Images.Media.getBitmap(
                            this.contentResolver,
                            Uri.parse(photoUri)
                        )
                        else -> {
                            val source =
                                ImageDecoder.createSource(this.contentResolver, Uri.parse(photoUri))
                            ImageDecoder.decodeBitmap(source)
                        }
                    }
                }

                val contactObj = Contact1(name, phoneNumber, image)
                contactsList.add(contactObj)
            }
        }
        binding.contactsList.layoutManager = LinearLayoutManager(this)
        binding.contactsList.adapter = ContactsAdapter(contactsList, this)

        curserObject?.close()

    }
}