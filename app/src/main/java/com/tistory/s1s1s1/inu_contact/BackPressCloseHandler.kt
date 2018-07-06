package com.tistory.s1s1s1.inu_contact

import android.app.Activity
import android.widget.Toast

class BackPressCloseHandler(private val activity: Activity) {

    private var backKeyPressedTime: Long = 0
    private val backPressedInterval = 2000
    private var toast: Toast? = null

    fun onBackPressed() {
        if (System.currentTimeMillis() > backKeyPressedTime + backPressedInterval) {
            backKeyPressedTime = System.currentTimeMillis()
            showGuide()
            return
        }
        if (System.currentTimeMillis() <= backKeyPressedTime + backPressedInterval) {
            activity.finish()
            toast!!.cancel()
        }
    }

    private fun showGuide() {
        toast = Toast.makeText(activity,
                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT)
        toast!!.show()
    }
}