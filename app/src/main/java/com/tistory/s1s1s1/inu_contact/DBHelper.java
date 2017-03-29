package com.tistory.s1s1s1.inu_contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE CONTACT(_id INTEGER PRIMARY KEY AUTOINCREMENT, part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT, email TEXT, favorite INTEGER);");
//        db.execSQL("CREATE TABLE FAVORITE(_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT");
        // 소속, 상세소속, 직위, 이름, 직무, 전화번호, 이메일
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE CONTACT");
        onCreate(db);
    }

    public void insert(String part, String dpart, String position, String name, String task, String phone, String email, int favorite){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO CONTACT VALUES(null, '"+part+"', '"+dpart+"', '"+position+"', '"+name+"', '"+task+"', '"+phone+"', '"+email+"', "+favorite+");");
        db.close();
    }

    public void insertArr(ArrayList<Contact> contacts){
        SQLiteDatabase db = getWritableDatabase();
        for(Contact contact:contacts){
            String part = contact.part;
            String dpart = contact.dpart;
            String position = contact.position;
            String name = contact.name;
            String task = contact.task;
            String phone = contact.phone;
            String email = contact.email;
            int favorite = contact.favorite;
            db.execSQL("INSERT INTO CONTACT VALUES(null, '"+part+"', '"+dpart+"', '"+position+"', '"+name+"', '"+task+"', '"+phone+"', '"+email+"', "+favorite+");");
        }
        db.close();
    }

    public void drop(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DROP TABLE CONTACT");
        db.close();
    }

    public void delete(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM CONTACT");
        db.close();
    }

    public int getDBCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACT", null);
        int cnt = cursor.getCount();
        db.close();
        cursor.close();
        if(cursor==null) return 0;
        else return cnt;
    }

    public ArrayList<Contact> getResult(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACT", null);
        ArrayList<Contact> contacts = new ArrayList();
        Contact contact = null;
        while (cursor.moveToNext()){
            contact = new Contact();
            contact.setPart(cursor.getString(1));
            contact.setDpart(cursor.getString(2));
            contact.setPosition(cursor.getString(3));
            contact.setName(cursor.getString(4));
            contact.setTask(cursor.getString(5));
            contact.setPhone(cursor.getString(6));
            contact.setEmail(cursor.getString(7));
            contact.setFavorite(cursor.getInt(8));

            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    public ArrayList<String> getPart(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT PART FROM CONTACT", null);
        ArrayList<String> parts = new ArrayList<>();
        String part = "";
        while(cursor.moveToNext()){
            part = cursor.getString(0);
            parts.add(part);
        }
        db.close();
        cursor.close();
        return parts;
    }

    public ArrayList<Contact> getPartC(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT PART FROM CONTACT", null);
        ArrayList<Contact> parts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setPart(cursor.getString(0));
            parts.add(contact);
        }
        db.close();
        cursor.close();
        return parts;
    }

    public ArrayList<Contact> getContact(String part){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACT WHERE PART='"+part+"'", null);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        cursor.close();
        db.close();
        return contacts;
    }

    public ArrayList<Contact> searchP2(String p){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT DISTINCT PART FROM CONTACT WHERE PART LIKE '%"+p+"%'", null);
        ArrayList<Contact> parts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setPart(cursor.getString(0));
            parts.add(contact);
        }
        cursor.close();
        db.close();
        return parts;
    }

    public ArrayList<Contact> searchC(String c){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACT WHERE NAME LIKE'%"+c+"%'", null);
        ArrayList<Contact> contacts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        db.close();
        cursor.close();
        return contacts;
    }

    public ArrayList<Contact> searchC(String c, String part){
        //부서 내부에서 검색할때
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM CONTACT WHERE PART='"+part+"' AND NAME LIKE'%"+c+"%'", null);
        ArrayList<Contact> contacts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        db.close();
        cursor.close();
        return contacts;
    }
}
