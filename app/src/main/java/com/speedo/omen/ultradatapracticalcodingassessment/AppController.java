package com.speedo.omen.ultradatapracticalcodingassessment;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

// class created to start or initialization of Realm Database
public class AppController extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder().build();
        Realm.setDefaultConfiguration(realmConfiguration);
    }
}