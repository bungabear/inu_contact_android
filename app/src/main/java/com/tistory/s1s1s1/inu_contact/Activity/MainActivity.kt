/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import com.tistory.s1s1s1.inu_contact.Fragment.GroupFragment
import com.tistory.s1s1s1.inu_contact.LOG_TAG
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.Util.DBHelper
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import kotlinx.android.synthetic.main.main_actionbar.*

//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.TedPermission;

class MainActivity : AppCompatActivity() {

    val mContext : Context = this

    val groupFragment : Fragment = GroupFragment()
    val fm : FragmentManager = supportFragmentManager
//    val imm = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DBHelper.getInstance(this,Singleton.DB_VERSION)
        actionbar_et_search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
//                imm.hideSoftInputFromWindow(actionbar_et_search.windowToken, 0)
                return@OnEditorActionListener true
            }
            false
        })

//        actionbar_et_search.addTextChangedListener(mTextWatcher)
        actionbar_iv_refresh.setOnClickListener(mClickListener)
        actionbar_iv_search.setOnClickListener(mClickListener)
        actionbar_iv_info.setOnClickListener(mClickListener)
        actionbar_iv_map.setOnClickListener(mClickListener)

        val fragmentTransaction = fm.beginTransaction()
        fragmentTransaction.replace(R.id.main_fragment, groupFragment)
        fragmentTransaction.commit()

//        fm
//                .beginTransaction()
//                .hide(mImageViewerFragment)
//                .show( mTextViewerFragment )
//                .addToBackStack("TEXT_VIEWER_BACKSTACK")
//                .commit();




    }

    private var mClickListener = View.OnClickListener { v ->
        val id = v.id
//        imm!!.hideSoftInputFromWindow(actionbar_et_search.windowToken, 0)
        if (actionbar_et_search.isFocused) {
            actionbar_et_search.clearFocus()
        }

        when (id) {
//            R.id.actionbar_iv_refresh -> {
//                val dialog = AlertDialog.Builder(v.context).create()
//                if (networkCheck()!!) {
//                    dialog.setTitle("새로고침")
//                    dialog.setMessage("데이터를 새로고침 하시겠습니까?\n")
//                    dialog.setIcon(R.drawable.ic_refresh_pc)
//                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "예") { dialogInterface, i -> getDB() }
//                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "아니오") { dialogInterface, i -> dialog.dismiss() }
//                    dialog.show()
//                    val nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
//                    nbutton.setTextColor(Color.BLACK)
//                    val pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    pbutton.setTextColor(Color.BLACK)
//                } else {
//                    dialog.setMessage(getString(R.string.no_internet))
//                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인") { dialogInterface, i -> dialog.dismiss() }
//                    dialog.setCancelable(false)
//                    dialog.show()
//                    val pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    pbutton.setTextColor(Color.BLACK)
//                }
//            }
            R.id.actionbar_iv_info -> startActivity(Intent(this, InfoActivity::class.java))
            R.id.actionbar_iv_map -> startActivity(Intent(this, MapActivity::class.java))
        }
    }

    override fun onBackPressed() {
        Log.d(LOG_TAG, "backstack count "+fm.backStackEntryCount)
//        super.onBackPressed()
    }
    //    private val context = this
//
//    private var bpch: BackPressCloseHandler? = null
//
//    private var actionbar_iv_refresh: ImageView? = null
//    private var actionbar_iv_search: ImageView? = null
//    private var actionbar_iv_info: ImageView? = null
//    private var actionbar_iv_map: ImageView? = null
//
//
//    private var singleton: Singleton? = null
//    private var issearch = false
//    private var result: JsonArray? = null
//    private val xProgressDialog: MyProgress? = null
//
//    private var imm: InputMethodManager? = null
//
//    internal var mTextWatcher: TextWatcher = object : TextWatcher {
//        //검색 edittext
//        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//
//        }
//        override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
//            actionbar_tv_title.text = "검색"
//            val keyword = actionbar_et_search.text.toString()
//            if (keyword != "") {
//                issearch = true
//                if (rv_level == 0) {
//                    main_rv.removeAllViews()
//                    val parts = dbHelper!!.searchPart(keyword)
//                    val contacts = dbHelper!!.searchContact(keyword)
//                    val num = dbHelper!!.searchNumber(keyword)
//                    parts.addAll(contacts)
//                    parts.addAll(num)
//                    val contactAdapter = ContactAdapter(parts)
//                    main_rv.adapter = contactAdapter
//                } else {
//                    main_rv.removeAllViews()
//                    var part = singleton!!.CURRENT_PART
//                    val contacts = dbHelper!!.searchContact(keyword, part)
//                    val num = dbHelper!!.searchNumber(keyword, part)
//                    contacts.addAll(num)
//                    val contactAdapter = ContactAdapter(contacts)
//                    main_rv.adapter = contactAdapter
//                    if (part.length >= 9) {
//                        part = part.substring(0, 8) + "..."
//                    }
//                }
//            } else {
//                //키워드가 공백일 때
//                if (issearch) {
//                    if (rv_level == 0) {
//                        main_rv.removeAllViews()
//                        val parts = dbHelper!!.part
//                        val contactAdapter = ContactAdapter(parts)
//                        main_rv.adapter = contactAdapter
//                        //                        rv_level = 0;
//                        issearch = false
//                        actionbar_tv_title.setText(R.string.app_name)
//                    } else {
//                        main_rv.removeAllViews()
//                        val part = singleton!!.CURRENT_PART
//                        val contacts = dbHelper!!.getContact(part)
//                        val contactAdapter = ContactAdapter(contacts)
//                        main_rv.adapter = contactAdapter
//                        //                        rv_level = 0;
//                        issearch = false
//                        actionbar_tv_title.text = part
//                    }
//                }
//
//
//            }
//        }
//
//        override fun afterTextChanged(editable: Editable) {
//
//        }
//    }
//
//    internal var mClickListener = View.OnClickListener { v ->
//        val id = v.id
//        imm!!.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
//        if (MainActivity.actionbar_et_search.isFocused) {
//            MainActivity.actionbar_et_search.clearFocus()
//        }
//
//        when (id) {
//            R.id.actionbar_iv_refresh -> {
//                val dialog = AlertDialog.Builder(v.context).create()
//                if (networkCheck()!!) {
//                    dialog.setTitle("새로고침")
//                    dialog.setMessage("데이터를 새로고침 하시겠습니까?\n")
//                    dialog.setIcon(R.drawable.ic_refresh_pc)
//                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "예") { dialogInterface, i -> getDB() }
//                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "아니오") { dialogInterface, i -> dialog.dismiss() }
//                    dialog.show()
//                    val nbutton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
//                    nbutton.setTextColor(Color.BLACK)
//                    val pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    pbutton.setTextColor(Color.BLACK)
//                } else {
//                    dialog.setMessage(getString(R.string.no_internet))
//                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인") { dialogInterface, i -> dialog.dismiss() }
//                    dialog.setCancelable(false)
//                    dialog.show()
//                    val pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
//                    pbutton.setTextColor(Color.BLACK)
//                }
//            }
//            R.id.actionbar_iv_info -> startActivity(Intent(context, InfoActivity::class.java))
//            R.id.actionbar_iv_map -> startActivity(Intent(context, MapActivity::class.java))
//        }
//    }

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//        main_fragment
//        //        mContext = getApplication();
//        mContext = context
//
//        if (imm == null) imm = application.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
//
//        actionbar_tv_title = findViewById<View>(R.id.actionbar_tv_title) as TextView
//        val tf = Typeface.createFromAsset(assets, "NanumGothicBold.ttf")
//        actionbar_tv_title.typeface = tf
//        actionbar_iv_refresh = findViewById<View>(R.id.actionbar_iv_refresh) as ImageView
//        actionbar_iv_refresh!!.setOnClickListener(mClickListener)
//        actionbar_et_search = findViewById<View>(R.id.actionbar_et_search) as EditText
//        actionbar_et_search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
//            if (i == EditorInfo.IME_ACTION_SEARCH) {
//                imm!!.hideSoftInputFromWindow(MainActivity.actionbar_et_search.windowToken, 0)
//                return@OnEditorActionListener true
//            }
//            false
//        })
//        actionbar_et_search.addTextChangedListener(mTextWatcher)
//        actionbar_iv_search = findViewById<View>(R.id.actionbar_iv_search) as ImageView
//        actionbar_iv_search!!.setOnClickListener(mClickListener)
//        actionbar_iv_info = findViewById<View>(R.id.actionbar_iv_info) as ImageView
//        actionbar_iv_info!!.setOnClickListener(mClickListener)
//        actionbar_iv_map = findViewById(R.id.actionbar_iv_map)
//        actionbar_iv_map!!.setOnClickListener(mClickListener)
//
//        main_rv = findViewById<View>(R.id.main_rv) as RecyclerView
//        main_rv.layoutManager = LinearLayoutManager(this)
//        main_rv.addItemDecoration(DividerItemDecoration(context, LinearLayoutManager.VERTICAL))
//
//        bpch = BackPressCloseHandler(this)
//
//
//        //        PermissionListener permissionListener = new PermissionListener() {
//        //            @Override
//        //            public void onPermissionGranted() {
//        ////                Toast.makeText(mContext, "권한 요청이 허가되었습니다.", Toast.LENGTH_SHORT).show();
//        //            }
//        //
//        //            @Override
//        //            public void onPermissionDenied(ArrayList<String> deniedPermissions) {
//        ////                Toast.makeText(mContext, "권한 요청이 거부되었습니다.", Toast.LENGTH_SHORT).show();
//        //            }
//        //        };
//
//        if (networkCheck()!! == false) {
//            val dialog = AlertDialog.Builder(this).create()
//            dialog.setMessage(getString(R.string.no_internet))
//            dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인") { dialogInterface, i -> finish() }
//            dialog.setCancelable(false)
//            dialog.show()
//            val pbutton = dialog.getButton(DialogInterface.BUTTON_POSITIVE)
//            pbutton.setTextColor(Color.BLACK)
//        } else {
//            //            new TedPermission(getApplication())
//            //                    .setPermissionListener(permissionListener)
//            ////                .setRationaleMessage("앱에서 바로 통화 발신을 위해서는 권한이 필요합니다.")
//            ////                .setDeniedMessage("만일 권한 요청을 거부할 경우, 앱에서 바로 통화 발신이 불가합니다.\n\n[설정] > [권한]에서 권한을 허용해주세요.")
//            //                    .setPermissions(android.Manifest.permission.CALL_PHONE).check();
//
//            if (singleton == null) singleton = Singleton
//            if (dbHelper == null) dbHelper = DBHelper.getInstance(this, singleton!!.DB_VERSION)
//
//
//            //            new VerCheck().execute();
//            //            xProgressDialog = new MyProgress(MainActivity.this, "로딩중...");
//
//            //            get DB
//            getDB()
//
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        //        if(!versionCheked && networkCheck()){
//        //            versionCheked = true;
//        //            new VerCheck().execute();
//        //        }
//    }
//
//    private inner class VerCheck : AsyncTask<Void, Void, Void>() {
//        internal var storeVer: String? = null
//        internal var deviceVer: String? = null
//        // FIXME 액티비티 로딩이 되지 않았다고 팝업윈도우가 작동하지 않음.
//        //        MyProgress dialog;
//
//        override fun onPreExecute() {
//            //            dialog = new MyProgress(MainActivity.this, "버전정보 확인 중...");
//            //            dialog.show();
//        }
//
//        override fun doInBackground(vararg voids: Void): Void? {
//            storeVer = MarketVersionChecker.getMarketVersion(packageName)
//            try {
//                deviceVer = packageManager.getPackageInfo(packageName, 0).versionName
//            } catch (e: PackageManager.NameNotFoundException) {
//                e.printStackTrace()
//            }
//
//            return null
//        }
//
//        override fun onPostExecute(aVoid: Void) {
//            //            dialog.dismiss();
//            try {
//                if (storeVer!!.compareTo(deviceVer!!) > 0) {
//                    xProgressDialog!!.dismiss()
//                    val alt_bld = android.app.AlertDialog.Builder(
//                            context)
//                    alt_bld.setMessage("새 버전이 나왔습니다. 업데이트 하시겠습니까?")
//                            .setCancelable(false)
//                            .setPositiveButton("업데이트 하러 가기"
//                            ) { dialog, id ->
//                                val intent = Intent(
//                                        Intent.ACTION_VIEW,
//                                        Uri.parse("https://play.google.com/store/apps/details?id=com.tistory.s1s1s1.inu_contact"))
//                                startActivity(intent)
//                                getDB()
//                            }
//                            .setNegativeButton("나중에 하기"
//                            ) { dialog, id -> getDB() }
//                    val alert = alt_bld.create()
//                    // Title for AlertDialog
//                    alert.setTitle("새 버전")
//                    // Icon for AlertDialog
//                    alert.show()
//                    val pButton = alert.getButton(DialogInterface.BUTTON_POSITIVE)
//                    val nButton = alert.getButton(DialogInterface.BUTTON_NEGATIVE)
//                    pButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
//                    nButton.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary))
//
//                } else {
//                    // 업데이트 불필요
//                    getDB()
//                }
//            } catch (e: NullPointerException) {
//                e.printStackTrace()
//                getDB()
//            }
//
//        }
//    }
//
//    private fun getDB() {
//        if (xProgressDialog != null && !xProgressDialog.isShowing()) xProgressDialog.show()
//
//        singleton!!.retrofitService.contact().enqueue(object : Callback<JsonElement> {
//            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
//                //                progressDialog.dismiss();
//                Log.d("LOG_TAG", "RETROFIT SUCCESSED")
//                dbHelper!!.delete()
//                //                Log.d("LOG_TAG", response.toString());
//                result = response.body()!!.asJsonArray
//                //                Log.d("LOG_TAG", dbHelper.toString());
//                val retroAsync = RetroAsync(xProgressDialog, result!!, dbHelper!!)
//                retroAsync.execute()
//                Log.d("LOG_TAG", "Success, count: " + dbHelper!!.dbCount)
//            }
//
//            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
//                Log.d("LOG_TAG", "RETROFIT FAILED")
//                xProgressDialog!!.dismiss()
//                Toast.makeText(context, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    override fun onBackPressed() {
//        //        if(actionbar_et_search.isFocused()) actionbar_et_search.clearFocus();
//        //
//        //        if (issearch == true) {
//        //            ArrayList<Contact> contacts = dbHelper.getPart();
//        //            ContactAdapter contactAdapter = new ContactAdapter(this, contacts);
//        //            MainActivity.main_rv.setAdapter(contactAdapter);
//        //            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//        //            MainActivity.actionbar_tv_title.setText(R.string.app_name);
//        //            MainActivity.rv_level = 0;
//        //            MainActivity.actionbar_et_search.setText("");
//        //            actionbar_tv_title.setText(R.string.app_name);
//        //            issearch = false;
//        //        } else if (MainActivity.rv_level == 1) {
//        //            MainActivity.main_rv.removeAllViews();
//        //            ArrayList<Contact> contacts = dbHelper.getPart();
//        //            ContactAdapter contactAdapter = new ContactAdapter(this, contacts);
//        //            MainActivity.main_rv.setAdapter(contactAdapter);
//        //            MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
//        //            MainActivity.actionbar_tv_title.setText(R.string.app_name);
//        //            MainActivity.rv_level = 0;
//        ////            if (MainActivity.actionbar_et_search.isFocused()) {
//        ////                MainActivity.actionbar_et_search.clearFocus();
//        ////            }
//        //        } else bpch.onBackPressed();
//
//        val contactAdapter: ContactAdapter
//        if (issearch) {
//            //검색모드
//            main_rv.removeAllViews()
//            issearch = false
//            if (actionbar_et_search.isFocused) actionbar_et_search.clearFocus()
//            actionbar_et_search.setText("")
//            when (rv_level) {
//                0 -> {
//                    contactAdapter = ContactAdapter(dbHelper!!.part)
//                    actionbar_tv_title.setText(R.string.app_name)
//                }
//                1 -> {
//                    val part = singleton!!.CURRENT_PART
//                    contactAdapter = ContactAdapter(dbHelper!!.getContact(part))
//                    actionbar_tv_title.text = part
//                }
//                else -> contactAdapter = ContactAdapter(null!!)
//            }
//            main_rv.adapter = contactAdapter
//        } else if (actionbar_et_search.isFocused) {
//            actionbar_et_search.clearFocus()
//        } else {
//            when (rv_level) {
//                0 -> bpch!!.onBackPressed()
//                1 -> {
//                    main_rv.removeAllViews()
//                    contactAdapter = ContactAdapter(dbHelper!!.part)
//                    actionbar_tv_title.setText(R.string.app_name)
//                    rv_level = 0
//                    main_rv.adapter = contactAdapter
//                }
//            }
//        }
//
//    }
//
//    fun networkCheck(): Boolean? {
//        val manager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
//        val phone = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
//        val wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
//
//        return if (phone.isConnected || wifi.isConnected) { //무선 데이터 네트워크 또는 Wifi연결이 되어있는 상태
//            true
//        } else { //연결되어 있지 않음
//            false
//        }
//
//    }
//
//    companion object {
//
//        var versionCheked: Boolean? = false
//        var mContext: Context = this
//        var dbHelper: DBHelper? = null
//
//
//        var rv_level: Int = 0 //현재 리사이클러뷰의 레벨
//
//        var actionbar_tv_title: TextView
//        var actionbar_et_search: EditText
//    }
}
