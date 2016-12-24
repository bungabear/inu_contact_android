package com.tistory.s1s1s1.inu_contract;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016-12-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CONTRACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT, email TEXT, favorite INTEGER);");
        // 소속, 상세소속, 직위, 이름, 직무, 전화번호, 이메일
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE CONTRACT");
        onCreate(db);
    }

    public void insert(String part, String dpart, String position, String name, String task, String phone, String email, int favorite){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CONTRACT VALUES(null, '"+part+"', '"+dpart+"', '"+position+"', '"+name+"', '"+task+"', '"+phone+"', '"+email+"', "+favorite+");");
        db.close();
    }

    public void insertArr(ArrayList<Contract> contracts){
        SQLiteDatabase db = getWritableDatabase();
        for(Contract contract:contracts){
            String part = contract.part;
            String dpart = contract.dpart;
            String position = contract.position;
            String name = contract.name;
            String task = contract.task;
            String phone = contract.phone;
            String email = contract.email;
            int favorite = contract.favorite;
            db.execSQL("INSERT INTO CONTRACT VALUES(null, '"+part+"', '"+dpart+"', '"+position+"', '"+name+"', '"+task+"', '"+phone+"', '"+email+"', "+favorite+");");
        }
        db.close();
    }

    public void drop(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE CONTRACT");
        db.close();
    }

    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CONTRACT");
        db.close();
    }

    public int getCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTRACT", null);
        db.close();
        if(cursor==null) return 0;
        else return cursor.getColumnCount();
    }

    public ArrayList<Contract> getResult(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTRACT", null);
        ArrayList<Contract> contracts = new ArrayList();
        Contract contract = null;
        while (cursor.moveToNext()){
            contract = new Contract();
            contract.setPart(cursor.getString(1));
            contract.setDpart(cursor.getString(2));
            contract.setPosition(cursor.getString(3));
            contract.setName(cursor.getString(4));
            contract.setTask(cursor.getString(5));
            contract.setPhone(cursor.getString(6));
            contract.setEmail(cursor.getString(7));
            contract.setFavorite(cursor.getInt(8));

            contracts.add(contract);
        }
        return contracts;
    }

    public ArrayList<String> getPart(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT PART FROM CONTRACT ORDER BY PART ASC", null);
        ArrayList<String> parts = new ArrayList<>();
        String part = "";
        while(cursor.moveToNext()){
            part = cursor.getString(0);
            parts.add(part);
        }
        db.close();
        return parts;
    }

    public ArrayList<Contract> getContract(String part){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTRACT WHERE PART='"+part+"'", null);
        ArrayList<Contract> contracts = new ArrayList<Contract>();
        Contract contract;
        while(cursor.moveToNext()){
            contract = new Contract();
            contract.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contracts.add(contract);
        }
        db.close();
        return contracts;
    }

    public ArrayList<Contract> searchP2(String p){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT PART FROM CONTRACT WHERE PART LIKE '%"+p+"%'", null);
        ArrayList<Contract> parts = new ArrayList<>();
        Contract contract;
        while(cursor.moveToNext()){
            contract = new Contract();
            contract.setPart(cursor.getString(0));
            parts.add(contract);
        }
        db.close();
        return parts;
    }

    public ArrayList<Contract> searchC(String c){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTRACT WHERE NAME LIKE'%"+c+"%'", null);
        ArrayList<Contract> contracts = new ArrayList<>();
        Contract contract;
        while(cursor.moveToNext()){
            contract = new Contract();
            contract.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contracts.add(contract);
        }
        db.close();
        return contracts;
    }

    public ArrayList<Contract> searchC(String c, String part){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTRACT WHERE PART='"+part+"' AND NAME LIKE'%"+c+"%'", null);
        ArrayList<Contract> contracts = new ArrayList<>();
        Contract contract;
        while(cursor.moveToNext()){
            contract = new Contract();
            contract.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contracts.add(contract);
        }
        db.close();
        return contracts;
    }
}
