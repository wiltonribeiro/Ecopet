package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ecopet.will.ecopet.ControlClasses.LoginControl;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;

public class MainActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    LoginControl loginControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //instancia os componentes de tela
        btnRegister = findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);

        //instancia a o controle resposavel pelo login
        loginControl = new LoginControl(MainActivity.this);

        //caso o botao login seja clicado
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vai para a tela de login
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });

        //caso o botao registro seja clicado
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //vai para a tela de registro
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        //verifica ao abrir o aplicativo se existe algum usuário já logado
        //se existir
        if (FirebaseData.mAuth.getCurrentUser()!=null){
            //inicio a tela de carregamento
            loginControl.getLoadingControl().startLoading();

            //busco o usuario já logado no banco
            loginControl.getUser();
        }

    }
}
