package com.vp.firebaseapp;

/**
 * Created by Vaibhav on 12/15/2016.
 */

public class Blog {

    private String Title;
    private String Desc;
    private String ImageUri;



    private String Username;
    private String ProPic;

    public Blog(){}
    public Blog(String title, String desc, String imageUri,String username,String proPic) {
        Title = title;
        Desc = desc;
        ImageUri = imageUri;
        Username = username;
        ProPic = proPic;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDesc() {
        return Desc;
    }

    public void setDesc(String desc) {
        Desc = desc;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getProPic(){ return ProPic;}

    public void setProPic(String proPic){ProPic = proPic; }
}
