package com.tistory.s1s1s1.inu_contact.Model

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query

@Dao
interface RoomContactDAO{
    @Insert
    fun insertAll(contacts : List<RoomContact> )

    @Query("SELECT * FROM RoomContact")
    fun getAll(): List<RoomContact>

    @Query("SELECT DISTINCT PART FROM RoomContact")
    fun getPartList(): List<String>

    // For Search All
    @Query("SELECT * FROM RoomContact WHERE NAME LIKE :name ")
    fun searchName(name: String): List<RoomContact>

    @Query("SELECT * FROM RoomContact WHERE PHONE LIKE :phone")
    fun searchPhone(phone: String): List<RoomContact>

    @Query("SELECT * FROM RoomContact WHERE PHONE LIKE :phone")
    fun searchPart(phone: String): List<RoomContact>

    // For Search In Part
    @Query("SELECT * FROM RoomContact WHERE PART = :part")
    fun getContactInPart(part: String): List<RoomContact>

    @Query("SELECT * FROM RoomContact WHERE NAME LIKE :name AND PART = :part")
    fun searchNameInPart(name: String, part : String): List<RoomContact>

    @Query("SELECT * FROM RoomContact WHERE PHONE LIKE :phone AND PART = :part")
    fun searchPhoneInPart(phone: String, part : String): List<RoomContact>

    @Query("DELETE FROM RoomContact")
    fun deleteAll()
}