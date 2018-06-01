package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecopet.will.ecopet.ControlClasses.LoginControl;
import com.ecopet.will.ecopet.R;

public class MenuActivity extends AppCompatActivity {

    Button btnLogOut, btnEditProfile, btnEditPassword, btnContact;
    LoginControl loginControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        //instancio os elementos de tela
        btnLogOut = findViewById(R.id.btnLogOut);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnEditPassword = findViewById(R.id.btnEditPassword);
        btnContact = findViewById(R.id.btnContact);

        // instancio o controle responsavel pelo login
        loginControl = new LoginControl(MenuActivity.this);
        //caso o botao de logout seja clicado
        btnLogOut.setOnClickListener(
                //desloga o usuario pelo controle de login
                new View.OnClickListener() {
            @Override
            public void onClick(View view) {loginControl.logOutUser();

            }
        });

        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this,EditProfileActivity.class));
            }
        });

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this,EditPasswordActivity.class));
            }
        });

        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MenuActivity.this,ContactActivity.class));
            }
        });
    }
}
