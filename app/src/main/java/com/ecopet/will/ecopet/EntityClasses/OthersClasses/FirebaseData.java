package com.ecopet.will.ecopet.EntityClasses.OthersClasses;

import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

/**
 * Created by willrcneto on 15/03/18.
 */

public class FirebaseData {

    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference myRef = database.getReference();
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance();
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static StorageReference storageRef = storage.getReferenceFromUrl("gs://ecopet-app.appspot.com/");
    public static User currentUser = null;
}
