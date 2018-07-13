package com.tistory.s1s1s1.inu_contact.model

import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.Database
import com.tistory.s1s1s1.inu_contact.util.Singleton.DB_VERSION

@Database(entities = [RoomContact::class], version = DB_VERSION)
abstract class AppDatabase : RoomDatabase() {
    abstract fun roomContactDAO(): RoomContactDAO
}