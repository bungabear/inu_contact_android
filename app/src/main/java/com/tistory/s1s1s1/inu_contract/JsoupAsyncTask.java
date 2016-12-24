package com.tistory.s1s1s1.inu_contract;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.tistory.s1s1s1.inu_contract.RecyclerView.ContractAdapter;
import com.tistory.s1s1s1.inu_contract.RecyclerView.MainAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-23.
 */

public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
    private String test="";

    private Context jContext;

    private DBHelper dbHelper;
//    db.execSQL("CREATE TABLE CONTRACT(part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT NOT NULL PRIMARY KEY, email TEXT;");

    ProgressDialog progressDialog;

    public JsoupAsyncTask(Context context){
        jContext = context;
        dbHelper = new DBHelper(jContext, "contract.db", null, 1);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(jContext);
        progressDialog.setMessage("서버에서 데이터를 받아오는 중입니다.");
        progressDialog.setIndeterminate(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document doc = Jsoup.connect("http://www.inu.ac.kr/cop/haksaStaffSearch/staffSearchView.do?id=inu_011001000000&section=all").get();
            Elements elements = doc.select("#contents > div > div > table > tbody > tr");
            ArrayList<Contract> contracts = new ArrayList<Contract>();
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
                Contract contract = new Contract();
                contract.setData(0, part, dpart, position, name, task, phone, email, 0);
                contracts.add(contract);
            }
            if(contracts.size()<=500){
                //파싱에 오류가 있음.
                Toast.makeText(jContext, "데이터 다운로드에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                dbHelper.insertArr(contracts);
            }
        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(jContext, "데이터 다운로드에 실패하였습니다.\n다시 시도해주세요.", Toast.LENGTH_SHORT).show();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
//        super.onPostExecute(aVoid);

//        ArrayList<Contract> contracts = dbHelper.getResult();
//        int id=0;
//        for(Contract contract:contracts){
//            String part="", dpart="", position="", name="", task="", phone="", email="";
//            part = contract.part;
//            dpart = contract.dpart;
//            position = contract.position;
//            name = contract.name;
//            task = contract.task;
//            phone = contract.phone;
//            email = contract.email;
//            test+=id+"/"+part+"/"+dpart+"/"+position+"/"+name+"/"+task+"/"+phone+"/"+email+"\n";
//            id++;
//        }
//        MainActivity.tv.setText(test);

//        ArrayList<String> parts = dbHelper.getPart();
//        for(String part:parts){
//            test+=part+"\n";
//        }

        ArrayList<String> parts = dbHelper.getPart();
        MainAdapter mainAdapter = new MainAdapter(jContext, parts);
        MainActivity.main_rv.setAdapter(mainAdapter);
        MainActivity.main_rv.setItemAnimator(new DefaultItemAnimator());
        MainActivity.actionbar_tv_title.setText(R.string.app_name);
        MainActivity.rv_level=0;
        if(MainActivity.actionbar_et_search.isFocused()){
            MainActivity.actionbar_et_search.clearFocus();
        }

        progressDialog.dismiss();
    }
}
