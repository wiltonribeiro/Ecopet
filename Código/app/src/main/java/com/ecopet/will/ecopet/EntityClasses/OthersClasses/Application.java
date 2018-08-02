package com.ecopet.will.ecopet.EntityClasses.OthersClasses;

/**
 * Created by willrcneto on 16/03/18.
 */

import com.google.firebase.database.FirebaseDatabase;

public class Application extends android.app.Application{
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}
