package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.FeedFragment;
import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.HomeActivity;
import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.MainActivity;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by willrcneto on 16/04/18.
 */

public class LoginControl {

    private Activity activity;
    private LoadingControl loadingControl;

    public LoginControl (Activity activity) {
        this.activity = activity;
        this.loadingControl = new LoadingControl(activity);
    }

    public LoadingControl getLoadingControl() {
        return loadingControl;
    }

    //desloga usuario
    public void logOutUser(){
        //desloga do firebase
        FirebaseData.mAuth.signOut();
        //fecha todas as telas abertas
        activity.finishAffinity();
        //vai para a tela inicial do aplicativo
        activity.startActivity(new Intent(activity, MainActivity.class));
    }

    public void loginUser(String email, String password){
        // inicia tela de carregamento
        loadingControl.startLoading();

        //tenta logar no firebase com os dados passados
        FirebaseData.mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //verifica resultado
                if (task.isSuccessful()) {
                    //busca os dados do usuario
                    getUser();
                }
                else{
                    loadingControl.finishLoading();
                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    // busca os dados do usuario no banco
    public void getUser(){
        FirebaseData.myRef.child("users").child(FirebaseData.mAuth.getCurrentUser().getUid()).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // adiciona como usuario atual o usuario buscado
                FirebaseData.currentUser = dataSnapshot.getValue(User.class);
                //finaliza a tela de carregamento
                loadingControl.finishLoading();
                //fecha as telas abertas
                activity.finishAffinity();
                //direcionado para a tela home
                FeedFragment.tagBefore = "";
                activity.startActivity(new Intent(activity,HomeActivity.class));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
