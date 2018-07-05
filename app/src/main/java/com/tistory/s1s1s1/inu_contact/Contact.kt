package com.tistory.s1s1s1.inu_contact

/**
 * Created by Administrator on 2016-12-23.
 */

data class Contact(
        var id: Int = 0,
        var part : String = "",
        var dpart : String = "",
        var position : String = "",
        var name : String = "",
        var task : String = "",
        var phone : String = "",
        var email : String = "",
        var favorite : Int = 0
        ) {
    //    db.execSQL("CREATE TABLE CONTRACT(part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT NOT NULL PRIMARY KEY, email TEXT;");
//    var dpart = ""
//    var position = ""
//    var name = ""
//    var task = ""
//    var phone = ""
//    var email = ""
//    var favorite = 0
//    var id = 0

//    fun setData(id: Int, part: String, dpart: String, position: String, name: String, task: String, phone: String, email: String, favorite: Int) {
//        this.id = id
//        this.part = part
//        this.dpart = dpart
//        this.position = position
//        this.name = name
//        this.task = task
//        this.phone = phone
//        this.email = email
//        this.favorite = favorite
//    }
}
