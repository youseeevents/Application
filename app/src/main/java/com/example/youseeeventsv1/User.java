package com.example.youseeeventsv1;
import java.lang.reflect.Array;
public class User {

    private String email;
    private String uid;
    private String username;
    private Boolean isOrg = false;

    public User(String e, String id, String u){
        this.email = e;
        this.uid = id;
        this.username = u;
    }

    public String getUsername(){
        return username;
    }
    public String getEmail(){
        return email;
    }
    private Boolean getIsOrg() { return isOrg; }
    public String getUid(){
        return uid;
    }
}
