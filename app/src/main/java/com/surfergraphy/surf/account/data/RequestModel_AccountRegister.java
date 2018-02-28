package com.surfergraphy.surf.account.data;

public class RequestModel_AccountRegister {
    public String Email;
    public String Password;
    public String ConfirmPassword;
    public String PhoneNumber;
    public String NickName;

    public RequestModel_AccountRegister(String email, String password, String confirmPassword, String phoneNumber, String nickName) {
        Email = email;
        Password = password;
        ConfirmPassword = confirmPassword;
        PhoneNumber = phoneNumber;
        NickName = nickName;
    }
}
