package com.vp.firebaseapp;

/**
 * Created by Vaibhav on 12/28/2016.
 */

public class Questions {

    private String Title;
    private String ImageUri;

    public Questions(){}

    public Questions(String title, String imageUri) {
        Title = title;
        ImageUri = imageUri;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getImageUri() {
        return ImageUri;
    }

    public void setImageUri(String imageUri) {
        ImageUri = imageUri;
    }
}
