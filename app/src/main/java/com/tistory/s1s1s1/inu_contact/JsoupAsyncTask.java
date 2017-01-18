package com.tistory.s1s1s1.inu_contact;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.widget.Toast;

import com.tistory.s1s1s1.inu_contact.RecyclerView.ContactAdapter;
import com.tistory.s1s1s1.inu_contact.RecyclerView.MainAdapter;
import com.tistory.s1s1s1.inu_contact.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-23.
 */

public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
    private String test="";

    private Context jContext;

    private DBHelper dbHelper;
//    db.execSQL("CREATE TABLE CONTACT(part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT NOT NULL PRIMARY KEY, email TEXT;");

    ProgressDialog progressDialog;

    public JsoupAsyncTask(Context context){
        jContext = context;
        dbHelper = new DBHelper(jContext, "contact.db", null, 1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        if(progressDialog==null) {
            progressDialog = new ProgressDialog(jContext);
            progressDialog.setMessage("데이터를 불러오는 중입니다...");
            progressDialog.setIndeterminate(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        } else {
            progressDialog.dismiss();
        }

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document doc = Jsoup.connect("http://www.inu.ac.kr/cop/haksaStaffSearch/staffSearchView.do?id=inu_011001000000&section=all").get();
            Elements elements = doc.select("#contents > div > div > table > tbody > tr");
            ArrayList<Contact> contacts = new ArrayList<Contact>();
            for(Element element:elements){
                String part="", dpart="", position="", name="", task="", phone="", email="";
                part = element.select("td:nth-child(1)").text();
                dpart = element.select("td:nth-child(2)").text();
                position = element.select("td:nth-child(3)").text();
                name = element.select("td:nth-child(4)").text();
                task = element.select("td:nth-child(5)").text();
                phone = element.select("td:nth-child(6)").text();
                email = element.select("td.bdrNone > a").attr("href").replace("mailto:", "").trim();
                if(email.equals(".")) email="";
                if(!email.contains("@")) email="";
                Contact contact = new Contact();
                contact.setData(0, part, dpart, position, name, task, phone, email, 0);
                contacts.add(contact);
            }
            if(contacts.size()<=500){
                //파싱에 오류가 있음.
//                Toast.makeText(jContext, "데이터 다운로드에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.delete();
                dbHelper.insertArr(contacts);
            }
        } catch (Exception e){
            e.printStackTrace();
//            Toast.makeText(jContext, "데이터 다운로드에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);

//        ArrayList<Contact> contacts = dbHelper.getResult();
//        int id=0;
//        for(Contact contact:contacts){
//            String part="", dpart="", position="", name="", task="", phone="", email="";
//            part = contact.part;
//            dpart = contact.dpart;
//            position = contact.position;
//            name = contact.name;
//            task = contact.task;
//            phone = contact.phone;
//            email = contact.email;
//            test+=id+"/"+part+"/"+dpart+"/"+position+"/"+name+"/"+task+"/"+phone+"/"+email+"\n";
//            id++;
//        }
//        MainActivity.tv.setText(test);

//        ArrayList<String> parts = dbHelper.getPart();
//        for(String part:parts){
//            test+=part+"\n";
//        }

//        ArrayList<String> parts = dbHelper.getPart();
//        MainAdapter mainAdapter = new MainAdapter(jContext, parts);
//        MainActivity.main_rv.setAdapter(mainAdapter);
//
        ArrayList<Contact> contacts = dbHelper.getPartC();
        ContactAdapter contactAdapter = new ContactAdapter(jContext, contacts);
        MainActivity.main_rv.setAdapter(contactAdapter);

        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
        MainActivity.actionbar_tv_title.setText(R.string.app_name);
        MainActivity.rv_level=0;
        if(MainActivity.actionbar_et_search.isFocused()){
            MainActivity.actionbar_et_search.clearFocus();
        }
        
        if(contactAdapter.getItemCount()==0){
            Toast.makeText(jContext, "데이터 다운로드에 실패하였습니다.\n새로고침을 눌러 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }

        progressDialog.dismiss();
    }
}
