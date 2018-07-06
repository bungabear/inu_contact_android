package com.tistory.s1s1s1.inu_contact.Progress

import android.content.Context
import android.support.constraint.ConstraintLayout
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.widget.TextView
import com.tistory.s1s1s1.inu_contact.R

class MyProgress(context : Context, str : String = "로딩중..."){
    private val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    private val layout = layoutInflater.inflate(R.layout.myprogress, null, false)
    private val popup = PopupWindow(layout, ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.MATCH_PARENT)
    private val textView = layout.findViewById<TextView>(R.id.progress_text)
    private var isShowing = false

    init {
        textView.text = str
    }
    fun show(){
        if(!isShowing){
            popup.animationStyle = -1 // 애니메이션 설정(-1:설정, 0:설정안함)
            popup.showAtLocation(layout, Gravity.CENTER, 0, -100)
            isShowing = true
        }
    }

    fun dismiss(){
        if(isShowing){
            isShowing = false
            popup.dismiss()
        }
    }

    fun isShowing() = isShowing
}