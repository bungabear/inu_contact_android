package com.tistory.s1s1s1.inu_contact.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.JsonObject

@Entity
data class RoomContact(
        @PrimaryKey
        var id: Int = 0,
        var dpart : String = "",
        var part : String = "",
        var position : String = "",
        var name : String = "",
        var task : String = "",
        var phone : String = "",
        var email : String = "",
        var favorite : Int = 0
) {
    constructor(id : Int , json : JsonObject) : this(){
        this.id = id
        if (!json.get("PART").isJsonNull) {
            part = json.get("PART").asString
        }
        if (!json.get("DPART").isJsonNull) {
            dpart = json.get("DPART").asString
        }
        if (!json.get("POSITION").isJsonNull) {
            position = json.get("POSITION").asString
        }
        if (!json.get("NAME").isJsonNull) {
            name = json.get("NAME").asString
        }
        if (!json.get("PHONE").isJsonNull) {
            phone = json.get("PHONE").asString
            phone = phone.replace("-","")
        }
        if(part == "") part ="기타"
    }
}
