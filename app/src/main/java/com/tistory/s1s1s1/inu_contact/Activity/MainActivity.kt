/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentManager
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import com.tistory.s1s1s1.inu_contact.Fragment.GroupFragment
import com.tistory.s1s1s1.inu_contact.LOG_TAG
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter
import com.tistory.s1s1s1.inu_contact.Util.DBHelper
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import com.tistory.s1s1s1.inu_contact.Util.Util
import kotlinx.android.synthetic.main.custom_actionbar.*


//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.TedPermission;

class MainActivity : AppCompatActivity() {

    companion object {

        lateinit var actionBarTextView: TextView
        lateinit var actionBarSearchView: EditText
        lateinit var mFragmentManager : FragmentManager
        lateinit var currentFragment : GroupFragment
        fun setCurrentFragment(fragment : GroupFragment, tag :String = ""){
            currentFragment = fragment
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right )
            fragmentTransaction.replace(R.id.main_fragment, fragment, tag)
            if(mFragmentManager.fragments.size > 0) fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()

        }
    }

    private var groupFragment : GroupFragment? = null
    private var dbHelper : DBHelper? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFragmentManager = supportFragmentManager
        actionBarTextView = actionbar_tv_title
        actionBarSearchView = actionbar_et_search
        dbHelper = DBHelper.getInstance(this,Singleton.DB_VERSION)

        actionbar_et_search.setOnEditorActionListener(TextView.OnEditorActionListener { textView, i, keyEvent ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                Util.hideKeyboard(this)
                return@OnEditorActionListener true
            }
            false
        })

        actionbar_et_search.addTextChangedListener(mTextWatcher)
        actionbar_iv_refresh.setOnClickListener(mClickListener)
        actionbar_iv_search.setOnClickListener(mClickListener)
        actionbar_iv_info.setOnClickListener(mClickListener)
        actionbar_iv_map.setOnClickListener(mClickListener)

        groupFragment = GroupFragment.newInstance { dbHelper!!.part }
        setCurrentFragment(groupFragment!!, "Groups")
    }

    private var mClickListener = View.OnClickListener { v ->
        val id = v.id
        Util.hideKeyboard(this)
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

    var mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {  }
        override fun onTextChanged(charSequence: CharSequence, start: Int, end1: Int, count: Int) {
            val keyword = actionbar_et_search.text.toString()
            Log.d(LOG_TAG, keyword)
            (currentFragment.mRV!!.adapter as ContactAdapter).filter.filter(keyword)
        }
        override fun afterTextChanged(editable: Editable) {  }
    }

    override fun onBackPressed() {
        // 검색중이면 검색을 먼저 취소하고, 그 이후 Stack Pop
        if(actionbar_et_search.text.toString() != ""){
            actionbar_et_search.setText("")
            (currentFragment.mRV!!.adapter as ContactAdapter).filter.filter("")
        }
        else if(mFragmentManager.backStackEntryCount > 0){
            mFragmentManager.popBackStack()
            currentFragment = mFragmentManager.findFragmentById(R.id.main_fragment) as GroupFragment
        }
        else {
            super.onBackPressed()
        }
    }

    fun setTitle(title : String){
        actionbar_tv_title.text = title
    }

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
