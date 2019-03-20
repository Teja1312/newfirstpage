package com.example.project.newfirstpage;

import android.content.Context;
import android.content.SharedPreferences;

public class UserSession {

    String email;
    String password;

    String latitude;
    String longitude;
    SharedPreferences preferences;
    Context ctx;
    SharedPreferences.Editor sharedPreferencesEditor ;

    public String getLatitude() {

        return preferences.getString("latitude","");
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
        sharedPreferencesEditor.putString("latitude",this.latitude);
        sharedPreferencesEditor.commit();
        sharedPreferencesEditor.apply();


    }

    public String getLongitude() {

        return preferences.getString("longitude","");
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
        sharedPreferencesEditor.putString("longitude",this.longitude);
        sharedPreferencesEditor.commit();
        sharedPreferencesEditor.apply();

    }

    public UserSession(Context ctx)
    {
        this.ctx = ctx;
        preferences  =ctx.getSharedPreferences("UserInfo",Context.MODE_PRIVATE);
        sharedPreferencesEditor = preferences.edit();
    }

    public String getEmail() {
        return preferences.getString("Email","");
    }

    public void setEmail(String email) {
        this.email = email;
        sharedPreferencesEditor.putString("Email",this.email);
        sharedPreferencesEditor.commit();
        sharedPreferencesEditor.apply();

    }

    public String getPassword() {

        return preferences.getString("password","");
    }

    public void setPassword(String password) {
        this.password = password;
        sharedPreferencesEditor = preferences.edit();
        sharedPreferencesEditor.putString("password",this.password);
        sharedPreferencesEditor.commit();
        sharedPreferencesEditor.apply();

    }

}
