package com.tistory.s1s1s1.inu_contact.adapter

import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.databinding.BindingAdapter
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tistory.s1s1s1.inu_contact.activity.MainActivity
import com.tistory.s1s1s1.inu_contact.fragment.ContactFragment
import com.tistory.s1s1s1.inu_contact.model.RoomContact
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.util.Util
import com.tistory.s1s1s1.inu_contact.databinding.RecyclerContactItemBinding
import com.tistory.s1s1s1.inu_contact.databinding.RecyclerGroupItemBinding


class ContactAdapter(private val contacts: ArrayList<RoomContact>, private var filteredContacts : ArrayList<RoomContact> = contacts) : RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    companion object {
        private var allList = MainActivity.DB_Contacts.roomContactDAO().getAll()
    }

    enum class ViewHolderType { Contact, Group }

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
                (holder as ContactViewHolder).set(item)
            }
            ViewHolderType.Group -> {
                (holder as GroupViewHolder).set(item)
            }
        }
    }

    inner class ContactViewHolder(private val binding: RecyclerContactItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun onItemClick(v : View, item : RoomContact){
            Util.hideKeyboard(MainActivity.searchBar!!)
            val context = v.context
            val dialog = AlertDialog.Builder(context)
            dialog.setTitle("전화걸기").setMessage("${item.part} / ${item.name} (${item.phone})로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                    .setPositiveButton("예") { _, _ ->
                        val permissionCheck = ContextCompat.checkSelfPermission(context, android.Manifest.permission.CALL_PHONE)

                        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                            //권한 없음
//                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.phone))
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                            try {
//                                context.startActivity(intent)
//                            } catch (e: Exception) {
//                                e.printStackTrace()
//                            }

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
            alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(Color.BLACK)
            alert.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(Color.BLACK)
            alert.show()
        }

        fun set(item : RoomContact){
            binding.item = item
            binding.listener = this
            binding.executePendingBindings()
        }
    }

    inner class GroupViewHolder(private val binding: RecyclerGroupItemBinding) : RecyclerView.ViewHolder(binding.root) {

        @BindingAdapter("android:visibility")
        fun setVisibility(view: View, value: Boolean?) {
            view.visibility = if (binding.item!!.phone == "") View.VISIBLE else View.GONE
        }

        fun set(item : RoomContact){
            binding.item = item
            binding.listener = this
            binding.executePendingBindings()
        }

        fun onItemClick(v : View, item : RoomContact){
            Util.hideKeyboard(MainActivity.searchBar!!)
            val newFragment = ContactFragment.newInstance(item.part){
                ArrayList(MainActivity.DB_Contacts.roomContactDAO().getContactInPart(item.part))
            }
            val savedText = MainActivity.searchBar!!.text.toString()
            MainActivity.searchBar!!.setText("")
            MainActivity.setCurrentFragment(newFragment, savedText)
        }
    }

    override fun getItemCount(): Int {
        return filteredContacts.size
    }

    fun filter(str : String, part : String = "") {
        val filtered = ArrayList<RoomContact>()
        filteredContacts =
                // 검색 취소
                if(str == ""){
                    contacts
                }
                // 부서 안에서 검색
                else if(part != ""){
                    for (i in contacts) {
                        if (i.name.toLowerCase().contains(str.toLowerCase()) || i.phone.contains(str) || i.dpart.contains(str) || i.part.contains(str) || i.position.contains(str)) {
                            filtered.add(i)
                        }
                    }
                    filtered
                }
                // 전체 검색
                else {
                    // 그룹 + 개인
                    for (i in contacts) {
                        if (i.part.contains(str)) {
                            filtered.add(i)
                        }
                    }
                    for (i in allList) {
                        if (i.name.toLowerCase().contains(str.toLowerCase()) || i.phone.contains(str) || i.dpart.contains(str) || i.part.contains(str) || i.position.contains(str)) {
                            filtered.add(i)
                        }
                    }
                    filtered
                }
        notifyDataSetChanged()
    }
}