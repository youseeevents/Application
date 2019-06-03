package com.example.youseeeventsv1;
import java.lang.reflect.Array;
public class User {

    private String email;
    private String uid;
    private String username;
    private Boolean isOrg = false;
    private Boolean[] preferences;

    public User(String e, String id, String u){
        this.email = e;
        this.uid = id;
        this.username = u;
    }

    public User(String e, String id, String u, boolean org){
        this.email = e;
        this.uid = id;
        this.username = u;
        this.isOrg = org;
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
