package com.tistory.s1s1s1.inu_contact.RecyclerView

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.BindingAdapter
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import com.tistory.s1s1s1.inu_contact.Activity.MainActivity
import com.tistory.s1s1s1.inu_contact.Fragment.GroupFragment
import com.tistory.s1s1s1.inu_contact.LOG_TAG
import com.tistory.s1s1s1.inu_contact.Model.Contact
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.Util.DBHelper
import com.tistory.s1s1s1.inu_contact.databinding.RecyclerContactItemBinding
import com.tistory.s1s1s1.inu_contact.databinding.RecyclerGroupItemBinding
import java.util.*


class ContactAdapter(private val contacts: ArrayList<Contact>, private var filteredContacts : ArrayList<Contact> = contacts) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {


    enum class ViewHolderType(value : Int) { Contact(0), Group(1) }

    override fun getItemViewType(position: Int): Int {
        return if(filteredContacts[position].dpart=="" && filteredContacts[position].name =="") ViewHolderType.Group.ordinal else ViewHolderType.Contact.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val type = ViewHolderType.values()[viewType]
        return when(type){
            ViewHolderType.Contact-> {
                val binding = RecyclerContactItemBinding.inflate(inflater, parent, false)
                ContactViewHolder(binding)

            }
            ViewHolderType.Group-> {
                val binding = RecyclerGroupItemBinding.inflate(inflater, parent, false)
                GroupViewHolder(binding)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = ViewHolderType.values()[getItemViewType(position)]
        val item = filteredContacts[position]
        when (type) {
            ViewHolderType.Contact -> {
                (holder as ContactViewHolder).binding.item = item
            }
            ViewHolderType.Group -> {
                (holder as GroupViewHolder).set(item)
            }
        }
    }

    inner class ContactViewHolder(val binding: RecyclerContactItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onItemClick(v : View, item : Contact){
//            Util.hideKeyboard()
            val context = v.context
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("전화걸기").setMessage("${item.part} / ${item.name} (${item.phone})로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                    .setPositiveButton("예") { _, _ ->
                        val permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)

                        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                            //권한 없음
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        } else {
                            //권한 있음
                            val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + item.phone))
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                            try {
                                context.startActivity(intent)
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }

                        }
                    }.setNegativeButton("아니오") { diag, _ -> diag.dismiss() }
            val alert = dialog.create()
            alert.show()
            val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
            nbutton.setTextColor(Color.BLACK)
            val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
            pbutton.setTextColor(Color.BLACK)
        }

        fun set(item : Contact){
            binding.item = item
            binding.listener = this
            binding.executePendingBindings()
        }
    }

    inner class GroupViewHolder(val binding: RecyclerGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, value: Boolean?) {
            view.visibility = if (binding.item!!.phone == "") View.VISIBLE else View.GONE
        }

        fun set(item : Contact){
            binding.item = item
            binding.listener = this
            binding.executePendingBindings()
        }

        fun onItemClick(v : View, item : Contact){
            Log.d(LOG_TAG, "clicked")
            val newFragment = GroupFragment.newInstance(item.part){
                DBHelper.instance!!.getContact(item.part)
            }
            MainActivity.setCurrentFragment(newFragment, "contacts")
        }
    }

    override fun getItemCount(): Int {
        return filteredContacts.size
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val str = constraint.toString()
                val result = FilterResults()
                return if(str == ""){
                    result.values = contacts
                    result
                }
                else {
                    val filtered = ArrayList<Contact>()
                    for (i in contacts) {
                        if (i.name.contains(str.toLowerCase()) || i.phone.contains(str) || i.dpart.contains(str) || i.part.contains(str)) {
                            filtered.add(i)
                        }
                    }
                    result.values = filtered
                    result
                }
            }
            override fun publishResults(constraint: CharSequence?, results: FilterResults) {
                filteredContacts = results.values as ArrayList<Contact>
                notifyDataSetChanged()
            }

        }
    }
}