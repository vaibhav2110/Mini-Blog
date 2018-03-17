package com.vp.firebaseapp;

/**
 * Created by Vaibhav on 12/28/2016.
 */
public class Branch {
    private String name;
    private int imageResourceId;

    public static final Branch[] branchs = {
            new Branch("CSE", R.drawable.css),
            new Branch("IT",R.drawable.it),
            new Branch("ECE",R.drawable.ece),
            new Branch("EEE",R.drawable.eee),
            new Branch("CIVIL",R.drawable.civil),

            new Branch("MECH",R.drawable.mechanical)}
            ;

    private Branch(String name, int imageResourceId) {
        this.name = name;
        this.imageResourceId = imageResourceId;
    }

    public String getName() {
        return name;}

    public int getImageResourceId(){
        return imageResourceId;
    }
}
