package com.tistory.s1s1s1.inu_contract;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tistory.s1s1s1.inu_contract.RecyclerView.ContractAdapter;
import com.tistory.s1s1s1.inu_contract.RecyclerView.MainAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static Context mContext;

    public static Context mContext2;

    //    private boolean isfirst = true;
    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private Date updatedate;

    public static DBHelper dbHelper;

    public static RecyclerView main_rv;

    public static int rv_level; //현재 리사이클러뷰의 레벨

    private BackPressCloseHandler bpch;

    public static TextView actionbar_tv_title;
    private ImageView actionbar_iv_refresh;
    public static EditText actionbar_et_search;
    private ImageView actionbar_iv_search;
    private ImageView actionbar_iv_info;

    private boolean issearch=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplication();
        mContext2 = MainActivity.this;

        actionbar_tv_title = (TextView) findViewById(R.id.actionbar_tv_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "NanumGothicBold.ttf");
        actionbar_tv_title.setTypeface(tf);
        actionbar_iv_refresh = (ImageView) findViewById(R.id.actionbar_iv_refresh);
        actionbar_iv_refresh.setOnClickListener(mClickListener);
        actionbar_et_search = (EditText) findViewById(R.id.actionbar_et_search);
        actionbar_et_search.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode==event.KEYCODE_ENTER){
                    return true;
                }
                return false;
            }
        });
        actionbar_iv_search = (ImageView) findViewById(R.id.actionbar_iv_search);
        actionbar_iv_search.setOnClickListener(mClickListener);
        actionbar_iv_info = (ImageView) findViewById(R.id.actionbar_iv_info);
        actionbar_iv_info.setOnClickListener(mClickListener);


        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        editor = prefs.edit();

        dbHelper = new DBHelper(getApplicationContext(), "contract.db", null, 1);

        main_rv = (RecyclerView) findViewById(R.id.main_rv);
        main_rv.setLayoutManager(new LinearLayoutManager(this));

        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
//                Toast.makeText(mContext, "권한 요청이 허가되었습니다.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//                Toast.makeText(mContext, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        bpch = new BackPressCloseHandler(this);

        if(dbHelper.getDBCount()==0){
            if(networkCheck()==false){
                AlertDialog dialog = new AlertDialog.Builder(this).create();
                dialog.setMessage(getString(R.string.no_internet));
                dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                });
                dialog.setCancelable(false);
                dialog.show();
                Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                pbutton.setTextColor(Color.BLACK);
            } else {
                new TedPermission(mContext)
                        .setPermissionListener(permissionListener)
//                .setRationaleMessage("앱에서 바로 통화 발신을 위해서는 권한이 필요합니다.")
//                .setDeniedMessage("만일 권한 요청을 거부할 경우, 앱에서 바로 통화 발신이 불가합니다.\n\n[설정] > [권한]에서 권한을 허용해주세요.")
                        .setPermissions(android.Manifest.permission.CALL_PHONE).check();

                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
                jsoupAsyncTask.execute();
            }
        } else {
            //db가 이미 있을 경우
            new TedPermission(mContext)
                    .setPermissionListener(permissionListener)
//                .setRationaleMessage("앱에서 바로 통화 발신을 위해서는 권한이 필요합니다.")
//                .setDeniedMessage("만일 권한 요청을 거부할 경우, 앱에서 바로 통화 발신이 불가합니다.\n\n[설정] > [권한]에서 권한을 허용해주세요.")
                    .setPermissions(android.Manifest.permission.CALL_PHONE).check();

            if(networkCheck()==true) {
                updateCheck();
            }

            ArrayList<String> parts = dbHelper.getPart();
            MainAdapter mainAdapter = new MainAdapter(getApplication(), parts);
            main_rv.setAdapter(mainAdapter);
            main_rv.setItemAnimator(new DefaultItemAnimator());
            MainActivity.actionbar_tv_title.setText(R.string.app_name);
            rv_level = 0;
        }
    }

    @Override
    public void onBackPressed() {
        if(issearch==true){
            ArrayList<String> parts = dbHelper.getPart();
            MainAdapter mainAdapter = new MainAdapter(mContext, parts);
            MainActivity.main_rv.setAdapter(mainAdapter);
            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
            MainActivity.actionbar_tv_title.setText(R.string.app_name);
            MainActivity.rv_level=0;
            if(MainActivity.actionbar_et_search.isFocused()){
                MainActivity.actionbar_et_search.clearFocus();
            }
            MainActivity.actionbar_et_search.setText("");
            issearch=false;
        } else if (MainActivity.rv_level == 1) {
            ArrayList<String> parts = dbHelper.getPart();
            MainAdapter mainAdapter = new MainAdapter(getApplication(), parts);
            MainActivity.main_rv.removeAllViews();
            MainActivity.main_rv.setAdapter(mainAdapter);
            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
            MainActivity.actionbar_tv_title.setText(R.string.app_name);
            MainActivity.rv_level = 0;
            if(MainActivity.actionbar_et_search.isFocused()){
                MainActivity.actionbar_et_search.clearFocus();
            }
        } else if(MainActivity.actionbar_et_search.isFocused()){
            MainActivity.actionbar_et_search.clearFocus();
        } else bpch.onBackPressed();


//        super.onBackPressed();
    }

    ImageView.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            InputMethodManager imm = (InputMethodManager)mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);
            if(MainActivity.actionbar_et_search.isFocused()){
                MainActivity.actionbar_et_search.clearFocus();
            }

            switch (id) {
                case R.id.actionbar_iv_refresh:
                    final AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
                    if(networkCheck()==true) {
                        dialog.setTitle("새로고침");
                        dialog.setMessage("데이터를 새로고침 하시겠습니까?\n(데이터는 한 달 주기로 자동으로 업데이트됩니다.)");
                        dialog.setIcon(R.drawable.ic_refresh_pc);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
                                jsoupAsyncTask.execute();

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                Date date = new Date();
                                String today = sdf.format(date);
                                editor.putString("lastupdate", today);
                                editor.commit();
                            }
                        });
                        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "아니오", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                        Button nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
                        nbutton.setTextColor(Color.BLACK);
                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(Color.BLACK);
                    } else {
                        dialog.setMessage(getString(R.string.no_internet));
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialog.dismiss();
                            }
                        });
                        dialog.setCancelable(false);
                        dialog.show();
                        Button pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                        pbutton.setTextColor(Color.BLACK);
                    }
                    break;
                case R.id.actionbar_iv_search:
                    if(rv_level==0) {
                        String keyword = actionbar_et_search.getText().toString();
                        issearch = true;
                        main_rv.removeAllViews();
                        ArrayList<Contract> parts = dbHelper.searchP2(keyword);
                        ArrayList<Contract> contracts = dbHelper.searchC(keyword);

                        parts.addAll(contracts);

                        ContractAdapter contractAdapter = new ContractAdapter(getApplication(), parts);
                        main_rv.setItemAnimator(new DefaultItemAnimator());
                        main_rv.setAdapter(contractAdapter);
                        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                        MainActivity.actionbar_tv_title.setText("검색");
                    } else {
                        String keyword = actionbar_et_search.getText().toString();
                        issearch = true;
                        main_rv.removeAllViews();
                        String part = actionbar_tv_title.getText().toString();
                        ArrayList<Contract> contracts = dbHelper.searchC(keyword, part);

                        ContractAdapter contractAdapter = new ContractAdapter(getApplication(), contracts);
                        main_rv.setItemAnimator(new DefaultItemAnimator());
                        main_rv.setAdapter(contractAdapter);
                        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                        actionbar_tv_title.setText(part+" - 검색");
                    }
                    break;
                case R.id.actionbar_iv_info:
//                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
//                    startActivity(intent);
                    break;
            }
        }
    };

    public void updateCheck(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        if(!prefs.contains("lastupdate")) {
            Date date = new Date();
            String sdate = sdf.format(date);
            editor.putString("lastupdate", sdate);
            editor.commit();
        }

        Date today = new Date();
        String lastupdate = prefs.getString("lastupdate", "2016-01-01");
        Date lastupdate2 = new Date();
        try {
            lastupdate2 = sdf.parse(lastupdate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        long between = ((today.getTime() - lastupdate2.getTime()) / 1000) / (60 * 60 * 24);
        if (between >= 30) {
            //한 달 간격으로 자동 업데이트.
//                Toast.makeText(mContext, "30일이 지났습니다. 업데이트 하시겠습니까?", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.mContext2);
            dialog.setTitle("데이터 업데이트").setMessage("데이터 업데이트가 있습니다. 진행하시겠습니까?\n(아니오를 선택한 경우 수동으로 업데이트 해주세요.)").setIcon(R.drawable.ic_refresh_pc)
                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
                            jsoupAsyncTask.execute();
                        }
                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = dialog.create();
            alert.show();
            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
            nbutton.setTextColor(Color.BLACK);
            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
            pbutton.setTextColor(Color.BLACK);

            Date date = new Date();
            String todayy = sdf.format(date);
            editor.putString("lastupdate", todayy);
            editor.commit();
        }
//        else {
//            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
//            jsoupAsyncTask.execute();
//        }
    }

    public Boolean networkCheck() {
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo phone = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (phone.isConnected() || wifi.isConnected()) { //무선 데이터 네트워크 또는 Wifi연결이 되어있는 상태
            return true;
        } else { //연결되어 있지 않음
            return false;
        }

    }
}
