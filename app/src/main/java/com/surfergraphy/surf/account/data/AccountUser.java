package com.surfergraphy.surf.account.data;

import io.realm.RealmModel;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.RealmClass;

@RealmClass
public class AccountUser implements RealmModel {
    @PrimaryKey
    public String Id;
    public String Email;
    public String Password;
    public String NickName;
    public String PhoneNumber;
}
