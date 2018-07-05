package com.tistory.s1s1s1.inu_contact.RecyclerView

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.support.constraint.ConstraintLayout
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

import java.util.ArrayList

import android.view.View.GONE
import android.widget.*
import com.tistory.s1s1s1.inu_contact.*
import com.tistory.s1s1s1.inu_contact.MainActivity.mContext


class ContactAdapter(private val Contacts: ArrayList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewHolderType(value : Int) { Contact(0), Group(1) }

    override fun getItemViewType(position: Int): Int {
        return if(Contacts[position].dpart=="" && Contacts[position].name =="") ViewHolderType.Group.ordinal else ViewHolderType.Contact.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val type = ViewHolderType.values()[viewType]
        return when(type){
            ViewHolderType.Contact-> ContactViewHolder(inflater.inflate(R.layout.contact_item, null))
            ViewHolderType.Group-> GroupViewHolder(inflater.inflate(R.layout.group_item, null))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = ViewHolderType.values()[getItemViewType(position)]
        val item = Contacts[position]
        when(type){
            ViewHolderType.Contact-> {
                (holder as ContactViewHolder).set(item)
            }

            ViewHolderType.Group-> {
                (holder as GroupViewHolder).set(item)
            }
        }
//        val (part1, dpart, position1, name, `_`, phone) = Contacts[position]
//            if (dpart == "" && name == "") {
//                //부서
//                holder.contact_item_tv_dpart.visibility = View.GONE
//                holder.contact_item_tv_name.text = "$part1"
//
//                holder.contact_item_tv_number.visibility = View.GONE
//                holder.contact_item_iv_call.visibility = GONE
//                holder.contact_item_rl.setOnClickListener {
//                    if (MainActivity.actionbar_et_search.isFocused) {
//                        MainActivity.actionbar_et_search.clearFocus()
//                    }
//                    Log.d(TAG, "${holder.itemId} + touched")
//                    var part = holder.contact_item_tv_name.text.toString()
//
//                    val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
//
//                    val Contacts = MainActivity.dbHelper.getContact(part)
//                    val ContactAdapter = ContactAdapter(mContext, Contacts)
//                    if (part.length >= 13) {
//                        part = part.substring(0, 12) + "..."
//                    }
//                    MainActivity.actionbar_tv_title.text = part
//                    MainActivity.main_rv.removeAllViews()
//                    MainActivity.main_rv.adapter = ContactAdapter
//                    MainActivity.main_rv.itemAnimator = DefaultItemAnimator()
//                    MainActivity.rv_level = 1
//                    if (singleton == null) singleton = Singleton
//                    singleton!!.CURRENT_PART = part
//                }
//            } else {
//                //사람
//            holder.contact_item_tv_dpart.visibility = View.VISIBLE
//            holder.contact_item_tv_number.visibility = View.VISIBLE
//            holder.contact_item_iv_call.visibility = View.VISIBLE
//            holder.contact_item_tv_dpart.text = "$dpart / $position1"
//            holder.contact_item_tv_name.text = name
//            holder.contact_item_tv_number.text = phone
//            if (phone.trim { it <= ' ' }.length <= 1) {
//                holder.contact_item_iv_call.visibility = GONE
//            }
//            holder.contact_item_iv_call.setOnClickListener {
//                if (MainActivity.actionbar_et_search.isFocused) {
//                    MainActivity.actionbar_et_search.clearFocus()
//                }
//
//                val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//                imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
//
//                val dialog = AlertDialog.Builder(MainActivity.mContext)
//                dialog.setTitle("전화걸기").setMessage("$part1 / $name($phone)로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
//                        .setPositiveButton("예") { dialog, which ->
//                            val permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE)
//
//                            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
//                                //권한 없음
//                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.contact_item_tv_number.text))
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                try {
//                                    mContext.startActivity(intent)
//                                } catch (e: Exception) {
//                                    e.printStackTrace()
//                                }
//
//                            } else {
//                                //권한 있음
//                                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.contact_item_tv_number.text))
//                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                try {
//                                    mContext.startActivity(intent)
//                                } catch (e: Exception) {
//                                    e.printStackTrace()
//                                }
//
//                            }
//                        }.setNegativeButton("아니오") { dialog, which -> dialog.dismiss() }
//                val alert = dialog.create()
//                alert.show()
//                val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
//                nbutton.setTextColor(Color.BLACK)
//                val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
//                pbutton.setTextColor(Color.BLACK)
//            }
//        }
    }



    class ContactViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val contact_item_iv_call = itemLayoutView.findViewById<ImageView>(R.id.contact_item_iv_call)
        val contact_item_tv_name = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_name)
        val contact_item_tv_number = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_number)
        val contact_item_tv_dpart = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_dpart)
        val contact_item_rl = itemLayoutView.findViewById<RelativeLayout>(R.id.contact_item_rl)
//        val contact_item_tv_email = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_email);

        fun set(item : Contact){
            contact_item_tv_dpart.visibility = View.VISIBLE
            contact_item_tv_number.visibility = View.VISIBLE
            contact_item_iv_call.visibility = View.VISIBLE
            var depart = "${item.dpart} / ${item.position}"
            contact_item_tv_dpart.text = depart
            contact_item_tv_name.text = item.name
            var number = if(item.phone !="")
                "${item.phone.substring(0,3)}-${item.phone.substring(3,6)}-${item.phone.substring(6)}" else ""
            contact_item_tv_number.text = number
            if (item.phone.trim { it <= ' ' }.length <= 1) {
                contact_item_iv_call.visibility = GONE
            }
            contact_item_iv_call.setOnClickListener {
                Util.hideKayboard()

                val dialog = AlertDialog.Builder(MainActivity.mContext)
                dialog.setTitle("전화걸기").setMessage("${item.part} / ${item.name} (${item.phone})로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                        .setPositiveButton("예") { dialog, which ->
                            val permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE)

                            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                //권한 없음
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact_item_tv_number.text))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                try {
                                    mContext.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            } else {
                                //권한 있음
                                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + contact_item_tv_number.text))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                try {
                                    mContext.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            }
                        }.setNegativeButton("아니오") { dialog, which -> dialog.dismiss() }
                val alert = dialog.create()
                alert.show()
                val nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
                nbutton.setTextColor(Color.BLACK)
                val pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
                pbutton.setTextColor(Color.BLACK)
            }
        }
    }

    class GroupViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val group_item_tv_name = itemLayoutView.findViewById<TextView>(R.id.group_item_tv_name)
        val group_item_parent = itemLayoutView.findViewById<ConstraintLayout>(R.id.group_item_parent)

        fun set(item : Contact){
            group_item_parent.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            group_item_tv_name.text = item.part
            group_item_parent.setOnClickListener {
                Util.hideKayboard()

                val Contacts = MainActivity.dbHelper.getContact(item.part)
                val ContactAdapter = ContactAdapter(Contacts)
                if (item.part.length >= 13) {
                    item.part = item.part.substring(0, 12) + "..."
                }
                MainActivity.actionbar_tv_title.text = item.part
                MainActivity.main_rv.removeAllViews()
                MainActivity.main_rv.adapter = ContactAdapter
                MainActivity.main_rv.itemAnimator = DefaultItemAnimator()
                MainActivity.rv_level = 1
                Singleton.CURRENT_PART = item.part
            }
        }
    }

    override fun getItemCount(): Int {
        return Contacts.size
    }
}