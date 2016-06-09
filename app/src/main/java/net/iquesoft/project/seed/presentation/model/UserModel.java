package net.iquesoft.project.seed.presentation.model;

import android.net.Uri;

/**
 * Class which stores all variables, such as user ID, name, etc. Here are only setters and getters.
 *
 */


public class UserModel {

    private static UserModel ourInstance;
    private Uri userPhotoUrl;
    private String userId;
    private String userName;
    private String userEmail;


    private UserModel() {

    }

    public static UserModel getInstance() {
        if (ourInstance == null) {
            ourInstance = new UserModel();
        }
        return ourInstance;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Uri getUserPhotoUrl() {
        return userPhotoUrl;
    }

    public void setUserPhotoUrl(Uri userPhotoUrl) {
        this.userPhotoUrl = userPhotoUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
