package com.tistory.s1s1s1.inu_contact;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.TedPermission;
import com.tistory.s1s1s1.inu_contact.Progress.MyProgress;
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter;
import com.tistory.s1s1s1.inu_contact.Network.RetroAsync;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    public static Boolean versionCheked = false;
    public static Context mContext;
    public static DBHelper dbHelper;
    public static RecyclerView main_rv;
    private Context context = this;

    public static int rv_level; //현재 리사이클러뷰의 레벨

    private BackPressCloseHandler bpch;

    public static TextView actionbar_tv_title;
    public static EditText actionbar_et_search;

    private ImageView actionbar_iv_refresh;
    private ImageView actionbar_iv_search;
    private ImageView actionbar_iv_info;


    private Singleton singleton;
    private boolean issearch = false;
    private JsonArray result;
    private MyProgress xProgressDialog;

    private InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mContext = getApplication();
        mContext = context;

        if(imm == null) imm = (InputMethodManager) getApplication().getSystemService(Context.INPUT_METHOD_SERVICE);

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

        main_rv = (RecyclerView) findViewById(R.id.main_rv);
        main_rv.setLayoutManager(new LinearLayoutManager(this));
        main_rv.addItemDecoration(new DividerItemDecoration(context,  LinearLayoutManager.VERTICAL));

        bpch = new BackPressCloseHandler(this);


//        PermissionListener permissionListener = new PermissionListener() {
//            @Override
//            public void onPermissionGranted() {
////                Toast.makeText(mContext, "권한 요청이 허가되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//
//            @Override
//            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
////                Toast.makeText(mContext, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show();
//            }
//        };

        if (!networkCheck()) {
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
//            new TedPermission(getApplication())
//                    .setPermissionListener(permissionListener)
////                .setRationaleMessage("앱에서 바로 통화 발신을 위해서는 권한이 필요합니다.")
////                .setDeniedMessage("만일 권한 요청을 거부할 경우, 앱에서 바로 통화 발신이 불가합니다.\n\n[설정] > [권한]에서 권한을 허용해주세요.")
//                    .setPermissions(android.Manifest.permission.CALL_PHONE).check();

            if (singleton == null) singleton = Singleton.INSTANCE;
            if (dbHelper == null) dbHelper = DBHelper.Companion.getInstance(this, singleton.getDB_VERSION());


            new VerCheck().execute();
            xProgressDialog = new MyProgress(MainActivity.this, "로딩중...");

            //get DB
//            getDB();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        if(!versionCheked && networkCheck()){
//            versionCheked = true;
//            new VerCheck().execute();
//        }
    }

    private class VerCheck extends AsyncTask<Void, Void, Void> {
        String storeVer, deviceVer;
        // FIXME 액티비티 로딩이 되지 않았다고 팝업윈도우가 작동하지 않음.
//        MyProgress dialog;

        @Override
        protected void onPreExecute() {
//            dialog = new MyProgress(MainActivity.this, "버전정보 확인 중...");
//            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            storeVer = MarketVersionChecker.INSTANCE.getMarketVersion(getPackageName());
            try {
                deviceVer = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
//            dialog.dismiss();
            try {
                if (storeVer.compareTo(deviceVer) > 0) {
                    xProgressDialog.dismiss();
                    android.app.AlertDialog.Builder alt_bld = new android.app.AlertDialog.Builder(
                            context);
                    alt_bld.setMessage("새 버전이 나왔습니다. 업데이트 하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("업데이트 하러 가기",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            Intent intent = new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("https://play.google.com/store/apps/details?id=com.tistory.s1s1s1.inu_contact"));
                                            startActivity(intent);
                                            getDB();
                                        }
                                    })
                            .setNegativeButton("나중에 하기",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            getDB();
                                        }
                                    });
                    android.app.AlertDialog alert = alt_bld.create();
                    // Title for AlertDialog
                    alert.setTitle("새 버전");
                    // Icon for AlertDialog
                    alert.show();
                    Button pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
                    Button nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
                    pButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    nButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));

                } else {
                    // 업데이트 불필요
                    getDB();
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                getDB();
            }
        }
    }

    private void getDB() {
        if(xProgressDialog!=null && !xProgressDialog.isShowing()) xProgressDialog.show();

        singleton.getRetrofitSerice().contact().enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
//                progressDialog.dismiss();
                Log.d("TAG", "RETROFIT SUCCESSED");
                dbHelper.delete();
//                Log.d("TAG", response.toString());
                result = response.body().getAsJsonArray();
//                Log.d("TAG", dbHelper.toString());
                RetroAsync retroAsync = new RetroAsync(context, xProgressDialog, result, dbHelper);
                retroAsync.execute();
                Log.d("TAG", "Success, count: " + dbHelper.getDbCount());
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Log.d("TAG", "RETROFIT FAILED");
                xProgressDialog.dismiss();
                Toast.makeText(context, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
//        if(actionbar_et_search.isFocused()) actionbar_et_search.clearFocus();
//
//        if (issearch == true) {
//            ArrayList<Contact> contacts = dbHelper.getPart();
//            ContactAdapter contactAdapter = new ContactAdapter(this, contacts);
//            MainActivity.main_rv.setAdapter(contactAdapter);
//            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//            MainActivity.actionbar_tv_title.setText(R.string.app_name);
//            MainActivity.rv_level = 0;
//            MainActivity.actionbar_et_search.setText("");
//            actionbar_tv_title.setText(R.string.app_name);
//            issearch = false;
//        } else if (MainActivity.rv_level == 1) {
//            MainActivity.main_rv.removeAllViews();
//            ArrayList<Contact> contacts = dbHelper.getPart();
//            ContactAdapter contactAdapter = new ContactAdapter(this, contacts);
//            MainActivity.main_rv.setAdapter(contactAdapter);
//            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//            MainActivity.actionbar_tv_title.setText(R.string.app_name);
//            MainActivity.rv_level = 0;
////            if (MainActivity.actionbar_et_search.isFocused()) {
////                MainActivity.actionbar_et_search.clearFocus();
////            }
//        } else bpch.onBackPressed();

        ContactAdapter contactAdapter;
        if (issearch == true){
            //검색모드
            main_rv.removeAllViews();
            issearch = false;
            if(actionbar_et_search.isFocused()) actionbar_et_search.clearFocus();
            actionbar_et_search.setText("");
            switch (rv_level){
                case 0:
                    contactAdapter = new ContactAdapter(dbHelper.getPart());
                    actionbar_tv_title.setText(R.string.app_name);
                    break;
                case 1:
                    final String part = singleton.getCURRENT_PART();
                    contactAdapter = new ContactAdapter(dbHelper.getContact(part));
                    actionbar_tv_title.setText(part);
                    break;
                default:
                    contactAdapter = new ContactAdapter(null);
            }
            main_rv.setAdapter(contactAdapter);
            main_rv.setItemAnimator(new DefaultItemAnimator());
        } else if(actionbar_et_search.isFocused()) {
            actionbar_et_search.clearFocus();
        } else {
            switch (rv_level) {
                case 0:
                    bpch.onBackPressed();
                    break;
                case 1:
                    main_rv.removeAllViews();
                    contactAdapter = new ContactAdapter(dbHelper.getPart());
                    actionbar_tv_title.setText(R.string.app_name);
                    rv_level = 0;
                    main_rv.setAdapter(contactAdapter);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    break;
            }
        }

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
                    ArrayList<Contact> parts = dbHelper.searchPart(keyword);
                    ArrayList<Contact> contacts = dbHelper.searchContact(keyword);
                    ArrayList<Contact> num = dbHelper.searchNumber(keyword);
                    parts.addAll(contacts);
                    parts.addAll(num);
                    ContactAdapter contactAdapter = new ContactAdapter(parts);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    main_rv.setAdapter(contactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                } else {
                    main_rv.removeAllViews();
                    String part = singleton.getCURRENT_PART();
                    ArrayList<Contact> contacts = dbHelper.searchContact(keyword, part);
                    ArrayList<Contact> num = dbHelper.searchNumber(keyword, part);
                    contacts.addAll(num);
                    ContactAdapter contactAdapter = new ContactAdapter(contacts);
                    main_rv.setItemAnimator(new DefaultItemAnimator());
                    main_rv.setAdapter(contactAdapter);
                    MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
                    if (part.length() >= 9) {
                        part = part.substring(0, 8) + "...";
                    }
                }
            } else {
                //키워드가 공백일 때
                if (issearch == true) {
                    if (rv_level == 0){
                        main_rv.removeAllViews();
                        ArrayList<Contact> parts = dbHelper.getPart();
                        ContactAdapter contactAdapter = new ContactAdapter(parts);
                        main_rv.setItemAnimator(new DefaultItemAnimator());
                        main_rv.setAdapter(contactAdapter);
                        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//                        rv_level = 0;
                        issearch = false;
                        actionbar_tv_title.setText(R.string.app_name);
                    } else {
                        main_rv.removeAllViews();
                        String part = singleton.getCURRENT_PART();
                        ArrayList<Contact> contacts = dbHelper.getContact(part);
                        ContactAdapter contactAdapter = new ContactAdapter(contacts);
                        main_rv.setItemAnimator(new DefaultItemAnimator());
                        main_rv.setAdapter(contactAdapter);
                        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//                        rv_level = 0;
                        issearch = false;
                        actionbar_tv_title.setText(part);
                    }
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
                case R.id.actionbar_iv_info:
                    Intent intent = new Intent(context, InfoActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };

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
