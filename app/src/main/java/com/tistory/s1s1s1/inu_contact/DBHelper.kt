package com.tistory.s1s1s1.inu_contact

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.google.gson.JsonObject

import java.util.ArrayList

/**
 * Created by Administrator on 2016-12-23.
 */

class DBHelper private constructor(context: Context, version: Int) : SQLiteOpenHelper(context, "contact.db", null, version) {

    companion object {

        @Volatile
        private var instance: DBHelper? = null
        private var writable: SQLiteDatabase? = null
        private var readable: SQLiteDatabase? = null
        private val TABLE_ID = "CONTACT"
        //
        //    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        //        super(context, name, factory, version);
        //    }

        fun getInstance(context: Context, version: Int): DBHelper? {
            if (instance == null) {
                synchronized(DBHelper::class.java) {
                    if (instance == null) {
                        instance = DBHelper(context, version)
                    }
                }
            }
            return instance
        }
    }

    val dbCount: Int
        get() {
            val cursor = readable!!.rawQuery("SELECT * FROM CONTACT", null)
            val cnt = cursor!!.count
            cursor.close()
            return if (cursor == null)
                0
            else
                cnt
        }

    val part: ArrayList<Contact>
        get() {
            val cursor = readable!!.rawQuery("SELECT DISTINCT PART FROM CONTACT", null)
            val parts = ArrayList<Contact>()
            var contact: Contact
            while (cursor.moveToNext()) {
                contact = Contact()
                contact.part = cursor.getString(0)
                parts.add(contact)
            }
            cursor.close()
            return parts
        }

    init {
        writable = this.writableDatabase
        readable = this.readableDatabase
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_ID(_id INTEGER PRIMARY KEY AUTOINCREMENT, part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT, email TEXT, favorite INTEGER);")
        // 소속, 상세소속, 직위, 이름, 직무, 전화번호, 이메일
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE CONTACT")
        onCreate(db)
    }

    fun insert(`object`: JsonObject) {
        var part = ""
        if (!`object`.get("PART").isJsonNull) {
            part = `object`.get("PART").asString
        }
        var dpart = ""
        if (!`object`.get("DPART").isJsonNull) {
            dpart = `object`.get("DPART").asString
        }
        var position = ""
        if (!`object`.get("POSITION").isJsonNull) {
            position = `object`.get("POSITION").asString
        }
        var name = ""
        if (!`object`.get("NAME").isJsonNull) {
            name = `object`.get("NAME").asString
        }
        var phone = ""
        if (!`object`.get("PHONE").isJsonNull) {
            phone = `object`.get("PHONE").asString
        }
        val task = ""
        val email = ""
        writable!!.execSQL("insert into $TABLE_ID values(null, '$part', '$dpart', '$position', '$name', '$task', '$phone', '$email', 0);")
    }

    fun drop() {
        writable!!.execSQL("DROP TABLE CONTACT")
    }

    fun delete() {
        writable!!.execSQL("DELETE FROM CONTACT")
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

    fun getContact(part: String): ArrayList<Contact> {
        val cursor = readable!!.rawQuery("SELECT * FROM CONTACT WHERE PART='$part'", null)
        val contacts = ArrayList<Contact>()
        var contact: Contact
        while (cursor.moveToNext()) {
            contact = Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8))
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }

    fun searchPart(part: String): ArrayList<Contact> {
        val cursor = readable!!.rawQuery("SELECT DISTINCT PART FROM CONTACT WHERE PART LIKE '%$part%'", null)
        val parts = ArrayList<Contact>()
        var contact: Contact
        while (cursor.moveToNext()) {
            contact = Contact()
            contact.part = cursor.getString(0)
            parts.add(contact)
        }
        cursor.close()
        return parts
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

    fun searchContact(person: String): ArrayList<Contact> {
        val cursor = readable!!.rawQuery("SELECT * FROM CONTACT WHERE NAME LIKE'%$person%'", null)
        val contacts = ArrayList<Contact>()
        var contact: Contact
        while (cursor.moveToNext()) {
            contact = Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8))
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }

    fun searchContact(person: String, part: String): ArrayList<Contact> {
        //부서 내부에서 검색할때
        val cursor = readable!!.rawQuery("SELECT * FROM CONTACT WHERE PART='$part' AND NAME LIKE'%$person%'", null)
        val contacts = ArrayList<Contact>()
        var contact: Contact
        while (cursor.moveToNext()) {
            contact = Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8))
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }


}
