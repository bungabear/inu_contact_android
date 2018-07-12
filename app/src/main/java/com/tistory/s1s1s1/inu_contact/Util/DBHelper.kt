/*
 * Copyright (c) 2018. Minjae Son
 */

package com.tistory.s1s1s1.inu_contact.Util

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.google.gson.JsonObject
import com.tistory.s1s1s1.inu_contact.Model.Contact
import java.util.*

class DBHelper private constructor(val context: Context, val version: Int) : SQLiteOpenHelper(context, "contact.db", null, version) {
    companion object {
        var instance : DBHelper? = null
        fun getInstance(context : Context, version : Int) : DBHelper{
            val ret = instance ?: DBHelper(context, version)
            instance = ret
            return ret
        }
    }

    val writable: SQLiteDatabase = this.writableDatabase
    val readable: SQLiteDatabase = this.readableDatabase
    val TABLE_ID = "CONTACT"

    val dbCount: Int
        get() {
            val cursor = readable!!.rawQuery("SELECT * FROM CONTACT", null)
            val cnt = cursor!!.count
            cursor.close()
            return cnt
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

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE $TABLE_ID(_id INTEGER PRIMARY KEY AUTOINCREMENT, part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT, email TEXT, favorite INTEGER);")
        // 소속, 상세소속, 직위, 이름, 직무, 전화번호, 이메일
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE CONTACT")
        onCreate(db)
    }

    fun insert(obj: JsonObject) {
        var part = ""
        if (!obj.get("PART").isJsonNull) {
            part = obj.get("PART").asString
        }
        var dpart = ""
        if (!obj.get("DPART").isJsonNull) {
            dpart = obj.get("DPART").asString
        }
        var position = ""
        if (!obj.get("POSITION").isJsonNull) {
            position = obj.get("POSITION").asString
        }
        var name = ""
        if (!obj.get("NAME").isJsonNull) {
            name = obj.get("NAME").asString
        }
        var phone = ""
        if (!obj.get("PHONE").isJsonNull) {
            phone = obj.get("PHONE").asString
            phone = phone.replace("-","")
        }
        val task = ""
        val email = ""
        if(part == "") part ="기타"
        writable!!.execSQL("insert into $TABLE_ID values(null, '$part', '$dpart', '$position', '$name', '$task', '$phone', '$email', 0);")
    }

//    fun drop() {
//        writable!!.execSQL("DROP TABLE CONTACT")
//    }

    fun delete() {
        writable!!.execSQL("DELETE FROM CONTACT")
    }

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

    @JvmOverloads
    fun searchContact(person: String, part :String = ""): ArrayList<Contact> {
        //부서 내부에서 검색할때 part 사용.
        val query = if(part == "") "SELECT * FROM CONTACT WHERE NAME LIKE'%$person%'" else "SELECT * FROM CONTACT WHERE PART='$part' AND NAME LIKE'%$person%'"
        val cursor = readable!!.rawQuery(query, null)
        val contacts = ArrayList<Contact>()
        var contact: Contact
        while (cursor.moveToNext()) {
            contact = Contact(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getInt(8))
            contacts.add(contact)
        }
        cursor.close()
        return contacts
    }

    @JvmOverloads
    fun searchNumber(phone: String, part :String = ""): ArrayList<Contact> {
        val str = phone.replace("-", "")
        val query = if(part == "") "SELECT * FROM CONTACT WHERE phone LIKE'%$str%'" else "SELECT * FROM CONTACT WHERE PART='$part' AND phone LIKE'%$str%'"
        val cursor = readable!!.rawQuery(query, null)
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
