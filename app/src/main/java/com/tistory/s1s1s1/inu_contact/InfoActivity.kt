package com.tistory.s1s1s1.inu_contact

import android.content.pm.PackageInfo
import android.graphics.Typeface
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.TextView

/**
 * Created by Administrator on 2016-12-25.
 */

class InfoActivity : AppCompatActivity() {

    val tvVer = findViewById<TextView>(R.id.tv_ver)
    var pi: PackageInfo? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)


        try {
            pi = packageManager.getPackageInfo(packageName, 0)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        tvVer.text = pi!!.versionName

        val tf = Typeface.createFromAsset(assets, "NanumGothicBold.ttf")
        tvVer.typeface = tf
    }
}
