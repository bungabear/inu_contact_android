package com.tistory.s1s1s1.inu_contact;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.tistory.s1s1s1.inu_contact.MainActivity.dbHelper;
import static com.tistory.s1s1s1.inu_contact.MainActivity.main_rv;
import static com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_tv_title;
import static com.tistory.s1s1s1.inu_contact.MainActivity.actionbar_et_search;
import static com.tistory.s1s1s1.inu_contact.MainActivity.rv_level;


/**
 * Created by seonilkim on 2017. 3. 23..
 */

public class RetroAsync extends AsyncTask<Void, Void, ArrayList<Contact>> {

    private Context context;
//    private DBHelper dbHelper;
    private Call<JsonElement> contact;
    private Singleton singleton;

    private ProgressDialog progressDialog = null;

    private JsonArray result = null;
//    private TextView tv_dialog = null;

    public RetroAsync(Context context, ProgressDialog progressDialog){
        this.context = context;
        this.progressDialog = progressDialog;
    }

//    public RetroAsync(Context context, TextView tv){
//        this.context = context;
//        this.tv_dialog = tv;
//    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        singleton = Singleton.getInstance(context);
        result = new JsonArray();

        if(progressDialog != null) progressDialog.show();
//        if(tv_dialog != null) tv_dialog.setVisibility(View.VISIBLE);
    }

    @Override
    protected ArrayList<Contact> doInBackground(Void... voids) {
        contact = singleton.getRetroService().contact();
        contact.enqueue(new Callback<JsonElement>() {
            @Override
            public void onResponse(Call<JsonElement> call, Response<JsonElement> response) {
                result = response.body().getAsJsonArray();
                try {
                    for (JsonElement element : result) {
                        JsonObject object = element.getAsJsonObject();
                        String part = object.get("PART").getAsString().trim();
                        if (part.equals("null")) part = "";
                        String dpart = object.get("DPART").getAsString().trim();
                        if (dpart.equals("null")) dpart = "";
                        String name = object.get("NAME").getAsString().trim();
                        if (name.equals("null")) name = "";
                        String phone = object.get("PHONE").getAsString().trim();
                        if (phone.equals("null")) phone = "";
                        String position = object.get("POSITION").getAsString().trim();
                        if (position.equals("null")) position = "";
                        dbHelper.insert(part, dpart, position, name, "", phone, "", 0);
//                    Log.d("TAG", i+"/"+result.size()+" completed.");
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
                dbHelper.delete();
            }

            @Override
            public void onFailure(Call<JsonElement> call, Throwable t) {
                Toast.makeText(context, "데이터 다운로드에 실패하였습니다.\n새로고침을 눌러주세요.", Toast.LENGTH_SHORT).show();
            }
        });

//        System.out.print("return part");
//        Log.d("TAG", "return part");

//        ArrayList<Contact> contacts = new ArrayList<Contact>();
//        for (int i = 0; i < result.size(); i++) {
//            try {
//                JsonObject object = result.get(i).getAsJsonObject();
//                String part = object.get("PART").getAsString().trim();
//                if (part.equals("null")) part = "";
//                String dpart = object.get("DPART").getAsString().trim();
//                if (dpart.equals("null")) dpart = "";
//                String name = object.get("NAME").getAsString().trim();
//                if (name.equals("null")) name = "";
//                String phone = object.get("PHONE").getAsString().trim();
//                if (phone.equals("null")) phone = "";
//                String position = object.get("POSITION").getAsString().trim();
//                if (position.equals("null")) position = "";
////                Contact contact = new Contact();
////                contact.setData(0, part, dpart, position, name, "", phone, "", 0);
////                contacts.add(contact);
//                dbHelper.insert(part, dpart, position, name, "", phone, "", 0);
//                Log.d("TAG", i+"/"+result.size()+" completed.");
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


//        dbHelper.insertArr(contacts);
        Log.d("TAG", "return partC");
        return dbHelper.getPartC();
    }

    @Override
    protected void onPostExecute(ArrayList<Contact> contacts) {
        super.onPostExecute(contacts);

        main_rv.setAdapter(new ContactAdapter(context, contacts));
        main_rv.setItemAnimator(new DefaultItemAnimator());
        actionbar_tv_title.setText(R.string.app_name);
        rv_level = 0;
        if(actionbar_et_search.isFocused()) {
            actionbar_et_search.clearFocus();
        }

//        Log.d("TAG", "dialog dismiss");
        progressDialog.dismiss();
//        tv_dialog.setVisibility(View.GONE);
    }

    //    @Override
//    protected void onPostExecute(Void aVoid) {
////        super.onPostExecute(aVoid);
//
//        ArrayList<Contact> contacts = dbHelper.getPartC();
////        ContactAdapter contactAdapter = new ContactAdapter(context, contacts);
////        main_rv.setAdapter(contactAdapter);
//        main_rv.setAdapter(new ContactAdapter(context, contacts));
//        main_rv.setItemAnimator(new DefaultItemAnimator());
//        actionbar_tv_title.setText(R.string.app_name);
//        rv_level = 0;
//        if (actionbar_et_search.isFocused()) {
//            actionbar_et_search.clearFocus();
//        }
//
//        if(progressDialog != null) progressDialog.dismiss();
//    }

//    @Override
//    protected void onProgressUpdate(Void... values) {
//        super.onProgressUpdate(values);
//    }
}
