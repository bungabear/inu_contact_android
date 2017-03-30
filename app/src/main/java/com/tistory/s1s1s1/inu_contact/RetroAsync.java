package com.tistory.s1s1s1.inu_contact;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;

import com.apkfuns.xprogressdialog.XProgressDialog;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter;

import java.util.ArrayList;

import retrofit2.Call;

import static com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_et_search;
import static com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_tv_title;
import static com.tistory.s1s1s1.inu_contact.MainActivity.main_rv;
import static com.tistory.s1s1s1.inu_contact.MainActivity.rv_level;


/**
 * Created by seonilkim on 2017. 3. 23..
 */

public class RetroAsync extends AsyncTask<Void, Void, Void> {

    private Context context;
    private Singleton singleton;
    private XProgressDialog xProgressDialog = null;
    private JsonArray result = null;
    private DBHelper dbHelper;

    public RetroAsync(Context context, XProgressDialog xProgressDialog, JsonArray jsonArray, DBHelper dbHelper) {
        this.context = context;
        this.xProgressDialog = xProgressDialog;
        this.result = jsonArray;
        this.dbHelper = dbHelper;
    }

    @Override
    protected void onPreExecute() {
        if(singleton == null) singleton.getInstance(context);
    }

    @Override
    protected Void doInBackground(Void... voids) {
        JsonObject object;
        for(int i=0;i<result.size();i++){
            object = result.get(i).getAsJsonObject();
            dbHelper.insert(object);
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        main_rv.setAdapter(new ContactAdapter(context, dbHelper.getPart()));
        main_rv.setItemAnimator(new DefaultItemAnimator());
        actionbar_tv_title.setText(R.string.app_name);
        rv_level = 0;
        if (actionbar_et_search.isFocused()) {
            actionbar_et_search.clearFocus();
        }
        xProgressDialog.dismiss();
    }
}


