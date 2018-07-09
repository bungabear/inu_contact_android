/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Util

import android.content.Context
import android.view.inputmethod.InputMethodManager
import com.tistory.s1s1s1.inu_contact.Activity.MainActivity

object Util{
    private val imm = MainActivity.mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun hideKayboard(){
        if (MainActivity.actionbar_et_search.isFocused) {
            MainActivity.actionbar_et_search.clearFocus()
        }
        imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
    }
}