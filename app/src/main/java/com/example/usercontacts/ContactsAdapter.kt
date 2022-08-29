package com.example.usercontacts

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.usercontacts.databinding.ContactListItemBinding

class ContactsAdapter(private var items: List<Contact>, private var context: Context) :
    RecyclerView.Adapter<ContactsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            ContactListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        with(holder) {
            with(items[position]) {
                binding.contactName.text = this.name
                binding.contactNumber.text = this.number
                if (this.image != null)
                    binding.contactImage.setImageBitmap(this.image)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(val binding: ContactListItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}