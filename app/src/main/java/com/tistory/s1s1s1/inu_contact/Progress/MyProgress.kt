package com.tistory.s1s1s1.inu_contact.Progress

import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.PopupWindow
import android.view.LayoutInflater
import android.widget.TextView
import com.tistory.s1s1s1.inu_contact.R


class MyProgress(val context : Activity, val str : String = "로딩중..."){

    private val popup = PopupWindow(context)
    val root = (context as Activity).window.decorView.findViewById<ViewGroup>(android.R.id.content)
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val layout = layoutInflater.inflate(R.layout.myprogress, null)
    private val textview = layout.findViewById<TextView>(R.id.progress_text)
    private var isShowing = false

    init {
        popup.contentView = layout
        popup.isFocusable = true

    }
    fun show(){
        isShowing = true
        textview.text = str
        popup.showAtLocation(layout, Gravity.NO_GRAVITY, 0,0)
    }

    fun dismiss(){
        isShowing = false
        popup.dismiss()
    }

    fun isShowing() = isShowing
}