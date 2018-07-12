/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Activity

import android.arch.persistence.room.Room
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.FragmentManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.JsonElement
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.Fragment.ContactFragment
import com.tistory.s1s1s1.inu_contact.Model.AppDatabase
import com.tistory.s1s1s1.inu_contact.Model.RoomContact
import com.tistory.s1s1s1.inu_contact.R
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter
import com.tistory.s1s1s1.inu_contact.Util.Singleton
import com.tistory.s1s1s1.inu_contact.Util.Util
import kotlinx.android.synthetic.main.custom_actionbar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference

//import com.gun0912.tedpermission.PermissionListener;
//import com.gun0912.tedpermission.TedPermission;

class MainActivity : AppCompatActivity() {
    companion object {
        private lateinit var wrActionBarTextView: WeakReference<TextView>
        private lateinit var wrActionBarSearchView: WeakReference<EditText>
        private lateinit var wrCurrentFragment : WeakReference<ContactFragment>
        lateinit var DB_Contacts : AppDatabase
        lateinit var mFragmentManager : FragmentManager
        fun setCurrentFragment(fragment : ContactFragment, tag :String = ""){
            wrCurrentFragment = WeakReference(fragment)
            val fragmentTransaction = mFragmentManager.beginTransaction()
            fragmentTransaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out, android.R.anim.slide_in_left, android.R.anim.slide_out_right )
            fragmentTransaction.replace(R.id.main_fragment, fragment, tag)
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
        val searchBar: EditText?
            get() =  wrActionBarSearchView.get()

        val actionBatTitle: TextView?
            get() =  wrActionBarTextView.get()
    }

    private val mContext : Context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        DB_Contacts = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "DB_Contacts").allowMainThreadQueries().build()
        mFragmentManager = supportFragmentManager
        wrActionBarTextView = WeakReference(actionbar_tv_title)
        wrActionBarSearchView = WeakReference(actionbar_et_search)

        actionbar_et_search.setOnEditorActionListener(TextView.OnEditorActionListener { _, i, _ ->
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
        refresh()
    }

    // 프레그먼트를 전부 삭제하고 그룹 목록을 다시 불러와 표시함.
    private fun refresh(){
        getDB{
            val count = mFragmentManager.backStackEntryCount
            for(i in 0..count){
                mFragmentManager.popBackStack()
            }
            val newFragment = ContactFragment.newInstance {
                val list = ArrayList<RoomContact>()
                DB_Contacts.roomContactDAO().getPartList().forEach {
                    list.add(RoomContact(part=it))
                }
                list
            }
            setCurrentFragment(newFragment, "")
        }
    }

    private var mClickListener = View.OnClickListener { v ->
        val id = v.id
        Util.hideKeyboard(this)
        when (id) {
            R.id.actionbar_iv_refresh -> {
                val dialog = AlertDialog.Builder(v.context).create()
                if (Util.networkCheck(mContext)) {
                    dialog.setTitle("새로고침")
                    dialog.setMessage("데이터를 새로고침 하시겠습니까?\n")
                    dialog.setIcon(R.drawable.ic_refresh_pc)
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "예") { _, _ -> refresh() }
                    dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "아니오") { _, _ -> dialog.dismiss() }
                    dialog.show()
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                            .setTextColor(Color.BLACK)
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                            .setTextColor(Color.BLACK)
                } else {
                    dialog.setMessage(getString(R.string.no_internet))
                    dialog.setButton(AlertDialog.BUTTON_POSITIVE, "확인") { _, _ -> dialog.dismiss() }
                    dialog.getButton(DialogInterface.BUTTON_POSITIVE)
                            .setTextColor(Color.BLACK)
                    dialog.show()
                }
            }
            R.id.actionbar_iv_info -> startActivity(Intent(this, InfoActivity::class.java))
            R.id.actionbar_iv_map -> startActivity(Intent(this, MapActivity::class.java))
        }
    }

    // 검색 처리
    private var mTextWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {  }
        override fun onTextChanged(charSequence: CharSequence, start: Int, end1: Int, count: Int) {
            val keyword = actionbar_et_search.text.toString()
//            Log.d(LOG_TAG, keyword)
            val current = (mFragmentManager.findFragmentById(R.id.main_fragment) as ContactFragment)
            (current.mRV!!.adapter as ContactAdapter).filter(keyword)
        }
        override fun afterTextChanged(editable: Editable) {  }
    }

    // 뒤로가기 버튼 처리.
    // 검색중이면 검색을 먼저 취소하고, 그 이후 Stack Pop
    // BaackStack 0은 빈 화면이므로, 1일때 finish()
    override fun onBackPressed() {
//        Log.d(LOG_TAG, "${mFragmentManager.backStackEntryCount}")
        if(actionbar_et_search.text.toString() != ""){
            actionbar_et_search.setText("")
            (wrCurrentFragment.get()!!.mRV!!.adapter as ContactAdapter).filter("")
        }
        else if(mFragmentManager.backStackEntryCount > 1){
            val savedText = mFragmentManager.findFragmentById(R.id.main_fragment).tag
            mFragmentManager.popBackStack()
            wrCurrentFragment = WeakReference(mFragmentManager.findFragmentById(R.id.main_fragment) as ContactFragment)
            actionbar_et_search.setText(savedText)
        }
        else {
            finish()
        }
    }

    // 데이터를 네트워크에서 가져오는 함수.
    // 데이터 저장후 doit 실행
    private fun getDB(doit : (()->Unit)? = null) {
        Singleton.retrofitService.contact().enqueue(object : Callback<JsonElement> {
            override fun onResponse(call: Call<JsonElement>, response: Response<JsonElement>) {
                DB_Contacts.roomContactDAO().deleteAll()
                val result = response.body()!!.asJsonArray
                var json: JsonObject
                val list = ArrayList<RoomContact>()
                for (i in 0 until result.size()) {
                    json = result[i].asJsonObject
                    list.add(RoomContact(i, json))
                }
                DB_Contacts.roomContactDAO().insertAll(list)
                doit!!()
            }

            override fun onFailure(call: Call<JsonElement>, t: Throwable) {
                Log.d("LOG_TAG", "RETROFIT FAILED")
                Snackbar.make(this@MainActivity.currentFocus, "데이터 업데이트에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show()
            }
        })
    }
}

