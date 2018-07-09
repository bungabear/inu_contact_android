package com.tistory.s1s1s1.inu_contact.RecyclerView

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
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tistory.s1s1s1.inu_contact.*
import com.tistory.s1s1s1.inu_contact.Activity.MainActivity
import com.tistory.s1s1s1.inu_contact.Activity.MainActivity.mContext
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import com.tistory.s1s1s1.inu_contact.Util.Util
import java.util.*

class ContactAdapter(private val Contacts: ArrayList<Contact>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    enum class ViewHolderType(value : Int) { Contact(0), Group(1) }

    override fun getItemViewType(position: Int): Int {
        return if(Contacts[position].dpart=="" && Contacts[position].name =="") ViewHolderType.Group.ordinal else ViewHolderType.Contact.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val type = ViewHolderType.values()[viewType]
        return when(type){
            ViewHolderType.Contact-> ContactViewHolder(inflater.inflate(R.layout.contact_item, parent, false))
            ViewHolderType.Group-> GroupViewHolder(inflater.inflate(R.layout.group_item, parent,false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = ViewHolderType.values()[getItemViewType(position)]
        val item = Contacts[position]
        when (type) {
            ViewHolderType.Contact -> {
                (holder as ContactViewHolder).set(item)
            }
            ViewHolderType.Group -> {
                (holder as GroupViewHolder).set(item)
            }
        }
    }

    class ContactViewHolder(itemLayoutView: View) : RecyclerView.ViewHolder(itemLayoutView) {
        val contact_item_iv_call : ImageView = itemLayoutView.findViewById(R.id.contact_item_iv_call)
        val contact_item_tv_name : TextView = itemLayoutView.findViewById(R.id.contact_item_tv_name)
        val contact_item_tv_number : TextView = itemLayoutView.findViewById(R.id.contact_item_tv_number)
        val contact_item_tv_dpart : TextView = itemLayoutView.findViewById(R.id.contact_item_tv_dpart)
//        val contact_item_rl : RelativeLayout = itemLayoutView.findViewById(R.id.contact_item_rl)
//        val contact_item_tv_email = (TextView) itemLayoutView.findViewById(R.id.contact_item_tv_email);

        fun set(item : Contact){
            contact_item_tv_dpart.visibility = View.VISIBLE
            contact_item_tv_number.visibility = View.VISIBLE
            contact_item_iv_call.visibility = View.VISIBLE
            val depart = "${item.dpart} / ${item.position}"
            contact_item_tv_dpart.text = depart
            contact_item_tv_name.text = item.name
            val number = if(item.phone !="")
                "${item.phone.substring(0,3)}-${item.phone.substring(3,6)}-${item.phone.substring(6)}" else ""
            contact_item_tv_number.text = number
            if (item.phone.trim { it <= ' ' }.length <= 1) {
                contact_item_iv_call.visibility = GONE
            }
            contact_item_iv_call.setOnClickListener {
                Util.hideKayboard()

                val dialog = AlertDialog.Builder(MainActivity.mContext)
                dialog.setTitle("전화걸기").setMessage("${item.part} / ${item.name} (${item.phone})로 전화를 거시겠습니까?").setIcon(R.drawable.ic_call)
                        .setPositiveButton("예") { _, _ ->
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
                        }.setNegativeButton("아니오") { diag, _ -> diag.dismiss() }
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
        private val groupItemTvName : TextView = itemLayoutView.findViewById(R.id.group_item_tv_name)
        private val groupItemParent : ConstraintLayout = itemLayoutView.findViewById(R.id.group_item_parent)

        fun set(item : Contact){
            groupItemParent.layoutParams = ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT)
            groupItemTvName.text = item.part
            groupItemParent.setOnClickListener {
                Util.hideKayboard()

                val contacts = MainActivity.dbHelper.getContact(item.part)
                val contactAdapter = ContactAdapter(contacts)
                if (item.part.length >= 13) {
                    item.part = item.part.substring(0, 12) + "..."
                }
                MainActivity.actionbar_tv_title.text = item.part
                MainActivity.main_rv.removeAllViews()
                MainActivity.main_rv.adapter = contactAdapter
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