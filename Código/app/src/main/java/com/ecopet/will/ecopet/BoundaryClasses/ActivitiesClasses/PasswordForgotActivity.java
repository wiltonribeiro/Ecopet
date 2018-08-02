package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.LoadingControl;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class PasswordForgotActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_forgot);

        final LoadingControl loadingControl = new LoadingControl(PasswordForgotActivity.this);
        Button btnNewPassword = findViewById(R.id.btnNewPassword);
        final EditText inputEmail = findViewById(R.id.inputEmail);

        btnNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = inputEmail.getText().toString().trim();
                if(email.length()<6)
                    Toast.makeText(PasswordForgotActivity.this, "Email inválido", Toast.LENGTH_SHORT).show();
                else {
                    loadingControl.startLoading();
                    FirebaseData.mAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()) {
                                finish();
                                Toast.makeText(PasswordForgotActivity.this, "Verifique sua caixa de email", Toast.LENGTH_SHORT).show();
                            }else{
                                loadingControl.finishLoading();
                                Toast.makeText(PasswordForgotActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });
    }
}
