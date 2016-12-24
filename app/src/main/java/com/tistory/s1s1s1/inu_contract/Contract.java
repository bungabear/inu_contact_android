package com.tistory.s1s1s1.inu_contract;

/**
 * Created by Administrator on 2016-12-23.
 */

public class Contract {
//    db.execSQL("CREATE TABLE CONTRACT(part TEXT, dpart TEXT, position TEXT, name TEXT, task TEXT, phone TEXT NOT NULL PRIMARY KEY, email TEXT;");
    String part="", dpart="", position="", name="", task="", phone="", email="";
    int favorite=0, id=0;

    public void setData(int id, String part, String dpart, String position, String name, String task, String phone, String email, int favorite){
        this.id = id;
        this.part = part;
        this.dpart = dpart;
        this.position = position;
        this.name = name;
        this.task = task;
        this.phone = phone;
        this.email = email;
        this.favorite = favorite;
    }

    public void setPart(String part){
        this.part = part;
    }

    public void setDpart(String dpart){
        this.dpart = dpart;
    }

    public void setPosition(String position){
        this.position = position;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setTask(String task){
        this.task = task;
    }

    public void setPhone(String phone){
        this.phone = phone;
    }

    public void setEmail(String email){
        this.email = email;
    }

    public void setFavorite(int favorite){
        this.favorite = favorite;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getPart(){
        return this.part;
    }

    public int getId(){
        return this.id;
    }

    public String getDpart(){
        return this.dpart;
    }

    public String getPosition(){
        return this.position;
    }

    public String getName(){
        return  this.name;
    }

    public String getTask(){
        return this.task;
    }

    public String getPhone(){
        return this.phone;
    }

    public String getEmail(){
        return this.email;
    }

    public int getFavorite() { return this.favorite; }
}
