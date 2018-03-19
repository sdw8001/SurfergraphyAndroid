package com.surfergraphy.surf.login.data;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class Member implements RealmModel {
    @PrimaryKey
    public String Id;
    public String Email;
    public String JoinType;
    public String LoginToken;
    public String PushToken;
    public String Name;
    public String ImageUrl;
    public int Grade;
    public int Wave;
    public boolean Deleted;

}
