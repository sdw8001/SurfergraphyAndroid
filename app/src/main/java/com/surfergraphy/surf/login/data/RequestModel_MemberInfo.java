package com.surfergraphy.surf.login.data;

public class RequestModel_MemberInfo {
    public String Id;
    public String LoginToken;
    public String Email;
    public String JoinType;
    public String Name;
    public String ImageUrl;

    public RequestModel_MemberInfo(String id, String loginToken, String email, String joinType, String name, String imageUrl) {
        Id = id;
        LoginToken = loginToken;
        Email = email;
        JoinType = joinType;
        Name = name;
        ImageUrl = imageUrl;
    }
}
