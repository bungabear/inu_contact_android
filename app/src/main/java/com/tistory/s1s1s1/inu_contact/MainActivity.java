package com.tistory.s1s1s1.inu_contact;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {


    public static Context mContext;
    public static Context mContext2;
    public static DBHelper dbHelper;
    public static RecyclerView main_rv;

    public static int rv_level; //현재 리사이클러뷰의 레벨

    private BackPressCloseHandler bpch;

    public static TextView actionbar_tv_title;
    private ImageView actionbar_iv_refresh;
    public static EditText actionbar_et_search;
    private ImageView actionbar_iv_search;
    private ImageView actionbar_iv_info;


    //    private Call<JsonElement> contact;
    private Singleton singleton;

    TextView tv_dialog;

    private boolean issearch = false;

//    private ContactAdapter contactAdapter;

//    private RestClient restClient;
//    private RequestParams params = new RequestParams();

    ProgressDialog progressDialog;
//    private Handler mHandler;
//    XProgressDialog xProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = getApplication();
        mContext2 = MainActivity.this;

//        restClient = new RestClient(MainActivity.this);
//        restClient = new AsyncHttpClient();

//        singleton = Singleton.getInstance(this);

        tv_dialog = (TextView) findViewById(R.id.tv_dialog);

        actionbar_tv_title = (TextView) findViewById(R.id.actionbar_tv_title);
        Typeface tf = Typeface.createFromAsset(getAssets(), "NanumGothicBold.ttf");
        actionbar_tv_title.setTypeface(tf);
        actionbar_iv_refresh = (ImageView) findViewById(R.id.actionbar_iv_refresh);
        actionbar_iv_refresh.setOnClickListener(mClickListener);
        actionbar_et_search = (EditText) findViewById(R.id.actionbar_et_search);
        actionbar_et_search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_SEARCH) {
//                    actionbar_iv_search.performClick();
                    InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        actionbar_et_search.addTextChangedListener(mTextWatcher);
        actionbar_iv_search = (ImageView) findViewById(R.id.actionbar_iv_search);
        actionbar_iv_search.setOnClickListener(mClickListener);
        actionbar_iv_info = (ImageView) findViewById(R.id.actionbar_iv_info);
        actionbar_iv_info.setOnClickListener(mClickListener);


//        prefs = PreferenceManager.getDefaultSharedPreferences(this);
//        editor = prefs.edit();

        dbHelper = new DBHelper(getApplicationContext(), "contact.db", null, 1);

        main_rv = (RecyclerView) findViewById(R.id.main_rv);
        main_rv.setLayoutManager(new LinearLayoutManager(this));

        bpch = new BackPressCloseHandler(this);


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
        if (networkCheck() == false) {
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

            //get DB
            getDB();

//            progressDialog  = new ProgressDialog(this);
//            progressDialog.show();
        }
    }
    private void getDB() {

//        progressDialog = new ProgressDialog(this);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("데이터를 불러오는 중입니다...");
//        progressDialog.setCanceledOnTouchOutside(false);
//        progressDialog.show();


        if (singleton == null) singleton = Singleton.getInstance(this);

//        xProgressDialog = new XProgressDialog(this, "로딩 중...", XProgressDialog.THEME_CIRCLE_PROGRESS);
//        xProgressDialog.show();

        progressDialog = singleton.Loading(this);
        progressDialog.show();

        singleton.getRetroService().contact().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                progressDialog.dismiss();
                Log.d("TAG", "RETROFIT SUCCESSED");
                dbHelper.delete();
                JsonArray jsonArray = response.body().getAsJsonArray();
                String size = String.valueOf(jsonArray.size());
                Log.d("TAG", "jsonarray : " + size);
                JsonObject object;
                for (int i = 0; i < jsonArray.size(); i++) {
                    try {
                        object = jsonArray.get(i).getAsJsonObject();
                        String part = "";
                        if (!object.get("PART").isJsonNull()) {
                            part = object.get("PART").getAsString();
                        }
                        String dpart = "";
                        if (!object.get("DPART").isJsonNull()) {
                            dpart = object.get("DPART").getAsString();
                        }
                        String position = "";
                        if (!object.get("POSITION").isJsonNull()) {
                            position = object.get("POSITION").getAsString();
                        }
                        String name = "";
                        if (!object.get("NAME").isJsonNull()) {
                            name = object.get("NAME").getAsString();
                        }
                        String phone = "";
                        if (!object.get("PHONE").isJsonNull()) {
                            phone = object.get("PHONE").getAsString();
                        }
                        dbHelper.insert(part, dpart, position, name, "", phone, "", 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

//                main_rv.setAdapter(new ContactAdapter(MainActivity.this, dbHelper.getPartC()));
//                main_rv.setItemAnimator(new DefaultItemAnimator());
//                actionbar_tv_title.setText(R.string.app_name);
//                rv_level = 0;
//                if (actionbar_et_search.isFocused()) {
//                    actionbar_et_search.clearFocus();
//                }

//                if(xProgressDialog.isShowing()) xProgressDialog.dismiss();
                progressDialog.dismiss();
                Log.d("TAG", "Success, count: " + dbHelper.getDBCount());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", "RETROFIT FAILED");
                progressDialog.dismiss();
//                if(xProgressDialog.isShowing()) xProgressDialog.dismiss();
                Toast.makeText(MainActivity.this, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (issearch == true) {
//            ArrayList<String> parts = dbHelper.getPart();
//            MainAdapter mainAdapter = new MainAdapter(mContext, parts);
//            MainActivity.main_rv.setAdapter(mainAdapter);
            ArrayList<Contact> contacts = dbHelper.getPartC();
            ContactAdapter contactAdapter = new ContactAdapter(mContext, contacts);
            MainActivity.main_rv.setAdapter(contactAdapter);
            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
            MainActivity.actionbar_tv_title.setText(R.string.app_name);
            MainActivity.rv_level = 0;
            if (MainActivity.actionbar_et_search.isFocused()) {
                MainActivity.actionbar_et_search.clearFocus();
            }
            MainActivity.actionbar_et_search.setText("");
            actionbar_tv_title.setText(R.string.app_name);
            issearch = false;
        } else if (MainActivity.rv_level == 1) {
//            ArrayList<String> parts = dbHelper.getPart();
//            MainAdapter mainAdapter = new MainAdapter(getApplication(), parts);
            MainActivity.main_rv.removeAllViews();
//            MainActivity.main_rv.setAdapter(mainAdapter);
            ArrayList<Contact> contacts = dbHelper.getPartC();
            ContactAdapter contactAdapter = new ContactAdapter(mContext, contacts);
            MainActivity.main_rv.setAdapter(contactAdapter);
            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
            MainActivity.actionbar_tv_title.setText(R.string.app_name);
            MainActivity.rv_level = 0;
            if (MainActivity.actionbar_et_search.isFocused()) {
                MainActivity.actionbar_et_search.clearFocus();
            }
        } else if (MainActivity.actionbar_et_search.isFocused()) {
            MainActivity.actionbar_et_search.clearFocus();
        } else bpch.onBackPressed();


//        super.onBackPressed();
    }

    TextWatcher mTextWatcher = new TextWatcher() {
        //검색 edittext
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            actionbar_tv_title.setText("검색");
            String keyword = actionbar_et_search.getText().toString();
            if (!keyword.equals("")) {
                issearch = true;
                if (rv_level == 0) {
                    main_rv.removeAllViews();
                    ArrayList<Contact> parts = dbHelper.searchP2(keyword);
                    ArrayList<Contact> contacts = dbHelper.searchC(keyword);

                    parts.addAll(contacts);

                    ContactAdapter contactAdapter = new ContactAdapter(getApplication(), parts);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    main_rv.setAdapter(contactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//                    MainActivity.actionbar_tv_title.setText("검색");
                } else {
                    main_rv.removeAllViews();
                    if (singleton == null) singleton = Singleton.getInstance(MainActivity.this);
                    String part = singleton.getCurrentPart();
                    ArrayList<Contact> contacts = dbHelper.searchC(keyword, part);

                    ContactAdapter contactAdapter = new ContactAdapter(getApplication(), contacts);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    main_rv.setAdapter(contactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                    if (part.length() >= 9) {
                        part = part.substring(0, 8) + "...";
                    }
//                    actionbar_tv_title.setText(part + " - 검색");
                }
            } else {
                if (issearch == true) {
                    main_rv.removeAllViews();
                    ArrayList<Contact> parts = dbHelper.getPartC();
//                    ArrayList<Contact> contacts = dbHelper.searchC(keyword);

//                    parts.addAll(contacts);

                    ContactAdapter contactAdapter = new ContactAdapter(getApplication(), parts);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    main_rv.setAdapter(contactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                    rv_level = 0;
                    issearch = false;
                    actionbar_tv_title.setText(R.string.app_name);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    };

    ImageView.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(MainActivity.actionbar_et_search.getWindowToken(), 0);
            if (MainActivity.actionbar_et_search.isFocused()) {
                MainActivity.actionbar_et_search.clearFocus();
            }

            switch (id) {
                case R.id.actionbar_iv_refresh:
                    final AlertDialog dialog = new AlertDialog.Builder(v.getContext()).create();
                    if (networkCheck() == true) {
                        dialog.setTitle("새로고침");
                        dialog.setMessage("데이터를 새로고침 하시겠습니까?\n");
                        dialog.setIcon(R.drawable.ic_refresh_pc);
                        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getDB();
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
//                case R.id.actionbar_iv_search:
//                    if(actionbar_et_search.getText().length()==0){
//                        Toast.makeText(MainActivity.this, "검색어를 입력해주세요.", Toast.LENGTH_SHORT).show();
//                    }else {
//                        if (rv_level == 0) {
//                            String keyword = actionbar_et_search.getText().toString();
//                            issearch = true;
//                            main_rv.removeAllViews();
//                            ArrayList<Contact> parts = dbHelper.searchP2(keyword);
//                            ArrayList<Contact> contacts = dbHelper.searchC(keyword);
//
//                            parts.addAll(contacts);
//
//                            ContactAdapter contactAdapter = new ContactAdapter(getApplication(), parts);
//                            main_rv.setItemAnimator(new DefaultItemAnimator());
//                            main_rv.setAdapter(contactAdapter);
//                            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
////                            MainActivity.actionbar_tv_title.setText("검색");
//                        } else {
//                            String keyword = actionbar_et_search.getText().toString();
//                            issearch = true;
//                            main_rv.removeAllViews();
//                            String part = actionbar_tv_title.getText().toString();
//                            ArrayList<Contact> contacts = dbHelper.searchC(keyword, part);
//
//                            ContactAdapter contactAdapter = new ContactAdapter(getApplication(), contacts);
//                            main_rv.setItemAnimator(new DefaultItemAnimator());
//                            main_rv.setAdapter(contactAdapter);
//                            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//                            if (part.length() >= 9) {
//                                part = part.substring(0, 8) + "...";
//                            }
////                            actionbar_tv_title.setText(part + " - 검색");
//                        }
//                    }
//                    break;
                case R.id.actionbar_iv_info:
                    Intent intent = new Intent(MainActivity.this, InfoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

//    public void updateCheck(){
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
//
//        if(!prefs.contains("lastupdate")) {
//            Date date = new Date();
//            String sdate = sdf.format(date);
//            editor.putString("lastupdate", sdate);
//            editor.commit();
//        }
//
//        Date today = new Date();
//        String lastupdate = prefs.getString("lastupdate", "2016-01-01");
//        Date lastupdate2 = new Date();
//        try {
//            lastupdate2 = sdf.parse(lastupdate);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        long between = ((today.getTime() - lastupdate2.getTime()) / 1000) / (60 * 60 * 24);
//        if (between >= 30) {
//            //한 달 간격으로 자동 업데이트.
////                Toast.makeText(mContext, "30일이 지났습니다. 업데이트 하시겠습니까?", Toast.LENGTH_SHORT).show();
//            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.mContext2);
//            dialog.setTitle("데이터 업데이트").setMessage("데이터 업데이트가 있습니다. 진행하시겠습니까?\n(아니오를 선택한 경우 수동으로 업데이트 해주세요.)").setIcon(R.drawable.ic_refresh_pc)
//                    .setPositiveButton("예", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
//                            jsoupAsyncTask.execute();
//                        }
//                    }).setNegativeButton("아니오", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialog, int which) {
//                    dialog.dismiss();
//                }
//            });
//            AlertDialog alert = dialog.create();
//            alert.show();
//            Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
//            nbutton.setTextColor(Color.BLACK);
//            Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
//            pbutton.setTextColor(Color.BLACK);
//
//            Date date = new Date();
//            String todayy = sdf.format(date);
//            editor.putString("lastupdate", todayy);
//            editor.commit();
//        }
//        else {
//            JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(MainActivity.this);
//            jsoupAsyncTask.execute();
//        }
//    }

    public Boolean networkCheck() {
        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo phone = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (phone.isConnected() || wifi.isConnected()) { //무선 데이터 네트워크 또는 Wifi연결이 되어있는 상태
            return true;
        } else { //연결되어 있지 않음
            return false;
        }

    }
}
