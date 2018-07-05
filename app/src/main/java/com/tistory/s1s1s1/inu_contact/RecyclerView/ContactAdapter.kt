package com.tistory.s1s1s1.inu_contact.RecyclerView

/**
 * Created by Administrator on 2016-12-24.
 */

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView

import com.tistory.s1s1s1.inu_contact.Contact
import com.tistory.s1s1s1.inu_contact.MainActivity
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.Singleton

import java.util.ArrayList

import android.view.View.GONE

class ContactAdapter(internal var mContext: Context, internal var Contacts: ArrayList<Contact>) : RecyclerView.Adapter<ContactAdapter.ViewHolder>() {
    internal var singleton: Singleton? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactAdapter.ViewHolder {
        val itemLayoutView = LayoutInflater.from(parent.context).inflate(R.layout.contact_item_new, null)
        return ViewHolder(itemLayoutView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (part1, dpart, position1, name, _, phone) = Contacts[position]
        if (dpart == "" && name == "") {
            //부서
            holder.contact_item_tv_dpart.visibility = View.GONE
            holder.contact_item_tv_name.text = ""+part1
            holder.contact_item_tv_number.visibility = View.GONE
            holder.contact_item_iv_call.visibility = GONE
            holder.contact_item_rl.setOnClickListener {
                if (MainActivity.actionbar_et_search.isFocused) {
                    MainActivity.actionbar_et_search.clearFocus()
                }

                var part = holder.contact_item_tv_name.text.toString()

                val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)

                val Contacts = MainActivity.dbHelper.getContact(part)
                val ContactAdapter = ContactAdapter(mContext, Contacts)
                if (part.length >= 13) {
                    part = part.substring(0, 12) + "..."
                }
                MainActivity.actionbar_tv_title.text = part
                MainActivity.main_rv.removeAllViews()
                MainActivity.main_rv.adapter = ContactAdapter
                MainActivity.main_rv.itemAnimator = DefaultItemAnimator()
                MainActivity.rv_level = 1
                if (singleton == null) singleton = Singleton
                singleton!!.CURRENT_PART = part
            }
        } else {
            //사람
            holder.contact_item_tv_dpart.visibility = View.VISIBLE
            holder.contact_item_tv_number.visibility = View.VISIBLE
            holder.contact_item_iv_call.visibility = View.VISIBLE
            holder.contact_item_tv_dpart.text = "$dpart / $position1"
            holder.contact_item_tv_name.text = name
            holder.contact_item_tv_number.text = phone
            if (phone.trim { it <= ' ' }.length <= 1) {
                holder.contact_item_iv_call.visibility = GONE
            }
            holder.contact_item_iv_call.setOnClickListener {
                if (MainActivity.actionbar_et_search.isFocused) {
                    MainActivity.actionbar_et_search.clearFocus()
                }

                val imm = mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)

                val dialog = AlertDialog.Builder(MainActivity.mContext)
                dialog.setTitle("전화걸기").setMessage("$part1 / $name($phone)로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                        .setPositiveButton("예") { dialog, which ->
                            val permissionCheck = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CALL_PHONE)

                            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                                //권한 없음
                                val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + holder.contact_item_tv_number.text))
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                                try {
                                    mContext.startActivity(intent)
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }

                            } else {
                                //권한 있음
                                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:" + holder.contact_item_tv_number.text))
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

    class ViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        var contact_item_tv_dpart: TextView
        var contact_item_tv_name: TextView
        var contact_item_tv_number: TextView
        //        public TextView contact_item_tv_email;
        var contact_item_iv_call: ImageView
        var contact_item_rl: RelativeLayout

        init {
            contact_item_iv_call = itemLayoutView.findViewById<ImageView>(R.id.contact_item_iv_call)
            contact_item_tv_name = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_name)
            contact_item_tv_number = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_number)
            contact_item_tv_dpart = itemLayoutView.findViewById<TextView>(R.id.contact_item_tv_dpart)
            //            contact_item_tv_email = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_email);
            contact_item_rl = itemLayoutView.findViewById<RelativeLayout>(R.id.contact_item_rl)
        }
    }

    override fun getItemCount(): Int {
        return Contacts.size
    }
}