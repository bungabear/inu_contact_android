package com.tistory.s1s1s1.inu_contact;

import android.content.pm.PackageInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.tistory.s1s1s1.inu_contact.R;

/**
 * Created by Administrator on 2016-12-25.
 */

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        TextView tv_ver = (TextView) findViewById(R.id.tv_ver);
        PackageInfo pi = null;
        try {
            pi = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (Exception e){
            e.printStackTrace();
        }
        tv_ver.setText(pi.versionName);

        Typeface tf = Typeface.createFromAsset(getAssets(), "NanumGothicBold.ttf");
        tv_ver.setTypeface(tf);
    }
}
