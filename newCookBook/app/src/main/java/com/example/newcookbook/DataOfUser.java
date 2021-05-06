package com.example.newcookbook;

public class DataOfUser {

    private String NickName;
    private String Email;



    public DataOfUser(String Email, String NickName) {
        this.Email = Email;
        this.NickName = NickName;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String Email) {
        this.Email = Email;
    }

    public String getName() {
        return NickName;
    }

    public void setName(String NickName) {
        this.NickName = NickName;
    }


}

