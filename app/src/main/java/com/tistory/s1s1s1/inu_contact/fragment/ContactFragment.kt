/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.EditorInfo
import com.tistory.s1s1s1.inu_contact.activity.MainActivity
import com.tistory.s1s1s1.inu_contact.model.RoomContact
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.adapter.ContactAdapter
import kotlinx.android.synthetic.main.fragment_contact.view.*

class ContactFragment : Fragment(){

    private lateinit var mLayout : ViewGroup
    private var callback : (() -> ArrayList<RoomContact>?)? = null
    var mRV : RecyclerView? = null
    var part = ""

    companion object {
        fun newInstance(part: String ="", callback: (() -> ArrayList<RoomContact>?)? = null) : ContactFragment{
            val fragment = ContactFragment()
            fragment.callback = callback
            fragment.part = part
            return fragment
        }
    }

    private var isFirst = true

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if(isFirst) {
            MainActivity.actionBatTitle!!.setText("")
            MainActivity.searchBar!!.onEditorAction(EditorInfo.IME_ACTION_DONE)

            mLayout = inflater.inflate(R.layout.fragment_contact, container, false) as ViewGroup
            mRV = mLayout.fragment_contact_rv
            mRV!!.layoutManager = LinearLayoutManager(context)
            mRV!!.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
            reset()

            isFirst = false
        }
        return mLayout
    }

    private fun reset(){
        mRV!!.layoutAnimation = AnimationUtils.loadLayoutAnimation(context, R.anim.recycler_contact_layoutanimation)
        if(callback != null){
            mRV!!.adapter = ContactAdapter(callback?.invoke()!!)
            mRV!!.adapter.notifyDataSetChanged()
            mRV!!.scheduleLayoutAnimation()
        }
        (mRV!!.adapter as ContactAdapter).filter("")
    }

    override fun onResume() {
        super.onResume()
        MainActivity.actionBatTitle!!.text = if(part == "") getString(R.string.app_name) else if(part.length >= 13) part.substring(0, 12) + "..." else part
        (mRV!!.adapter as ContactAdapter).filter("")
    }
}