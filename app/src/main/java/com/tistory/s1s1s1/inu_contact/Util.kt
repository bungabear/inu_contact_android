package com.tistory.s1s1s1.inu_contact

import android.content.Context
import android.view.inputmethod.InputMethodManager

object Util{
    private val imm = MainActivity.mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun hideKayboard(){
        if (MainActivity.actionbar_et_search.isFocused) {
            MainActivity.actionbar_et_search.clearFocus()
        }
        imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
    }
}