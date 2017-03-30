package com.tistory.s1s1s1.inu_contact;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.JsonObject;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016-12-23.
 */

public class DBHelper extends SQLiteOpenHelper {

    private volatile static DBHelper instance;
    private static SQLiteDatabase writable, readable;
    private static String TABLE_ID = "CONTACT";
//
//    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
//        super(context, name, factory, version);
//    }

    public static DBHelper getInstance(Context context, int version){
        if(instance==null) {
            synchronized (DBHelper.class) {
                if(instance==null){
                    instance = new DBHelper(context, version);
                }
            }
        }
        return instance;
    }

    private DBHelper(Context context, int version){
        super(context, "contact.db", null, version);
        writable = this.getWritableDatabase();
        readable = this.getReadableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_ID+"(_id INTEGER PRIMARY KEY AUTOINCREMENT, part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT, email TEXT, favorite INTEGER);");
        // 소속, 상세소속, 직위, 이름, 직무, 전화번호, 이메일
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE CONTACT");
        onCreate(db);
    }

    public void insert(JsonObject object){
        String part = "";
        if (!object.get("PART").isJsonNull()) {
            part = object.get("PART").getAsString();
        }
        String dpart = "";
        if (!object.get("DPART").isJsonNull()) {
            dpart = object.get("DPART").getAsString();
        }
        String position = "";
        if (!object.get("POSITION").isJsonNull()) {
            position = object.get("POSITION").getAsString();
        }
        String name = "";
        if (!object.get("NAME").isJsonNull()) {
            name = object.get("NAME").getAsString();
        }
        String phone = "";
        if (!object.get("PHONE").isJsonNull()) {
            phone = object.get("PHONE").getAsString();
        }
        String task = "";
        String email = "";
        writable.execSQL("insert into "+TABLE_ID+" values(null, '"+part+"', '"+dpart+"', '"+position+"', '"+name+"', '"+task+"', '"+phone+"', '"+email+"', 0);");
    }

    public void drop(){
        writable.execSQL("DROP TABLE CONTACT");
    }
    public void delete(){
        writable.execSQL("DELETE FROM CONTACT");
    }

    public int getDBCount(){
        Cursor cursor = readable.rawQuery("SELECT * FROM CONTACT", null);
        int cnt = cursor.getCount();
        cursor.close();
        if(cursor==null) return 0;
        else return cnt;
    }

    public ArrayList<Contact> getPart(){
        Cursor cursor = readable.rawQuery("SELECT DISTINCT PART FROM CONTACT", null);
        ArrayList<Contact> parts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setPart(cursor.getString(0));
            parts.add(contact);
        }
        cursor.close();
        return parts;
    }

//    public ArrayList<Contact> getPartC(){
////        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = readable.rawQuery("SELECT DISTINCT PART FROM CONTACT", null);
//        ArrayList<Contact> parts = new ArrayList<>();
//        Contact contact;
//        while(cursor.moveToNext()){
//            contact = new Contact();
//            contact.setPart(cursor.getString(0));
//            parts.add(contact);
//        }
////        db.close();
//        cursor.close();
//        return parts;
//    }

    public ArrayList<Contact> getContact(String part){
        Cursor cursor = readable.rawQuery("SELECT * FROM CONTACT WHERE PART='"+part+"'", null);
        ArrayList<Contact> contacts = new ArrayList<Contact>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    public ArrayList<Contact> searchPart(String part){
        Cursor cursor = readable.rawQuery("SELECT DISTINCT PART FROM CONTACT WHERE PART LIKE '%"+part+"%'", null);
        ArrayList<Contact> parts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setPart(cursor.getString(0));
            parts.add(contact);
        }
        cursor.close();
        return parts;
    }

//    public ArrayList<Contact> searchP2(String p){
////        SQLiteDatabase db = getReadableDatabase();
//        Cursor cursor = readable.rawQuery("SELECT DISTINCT PART FROM CONTACT WHERE PART LIKE '%"+p+"%'", null);
//        ArrayList<Contact> parts = new ArrayList<>();
//        Contact contact;
//        while(cursor.moveToNext()){
//            contact = new Contact();
//            contact.setPart(cursor.getString(0));
//            parts.add(contact);
//        }
//        cursor.close();
//        return parts;
//    }

    public ArrayList<Contact> searchContact(String person){
        Cursor cursor = readable.rawQuery("SELECT * FROM CONTACT WHERE NAME LIKE'%"+person+"%'", null);
        ArrayList<Contact> contacts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }

    public ArrayList<Contact> searchContact(String person, String part){
        //부서 내부에서 검색할때
        Cursor cursor = readable.rawQuery("SELECT * FROM CONTACT WHERE PART='"+part+"' AND NAME LIKE'%"+person+"%'", null);
        ArrayList<Contact> contacts = new ArrayList<>();
        Contact contact;
        while(cursor.moveToNext()){
            contact = new Contact();
            contact.setData(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8));
            contacts.add(contact);
        }
        cursor.close();
        return contacts;
    }
}
