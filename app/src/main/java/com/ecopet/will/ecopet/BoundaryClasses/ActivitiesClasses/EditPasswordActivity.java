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
import com.google.firebase.auth.AuthResult;

public class EditPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);

        final EditText inputPassword = findViewById(R.id.inputPassword);
        final EditText inputOldPassword = findViewById(R.id.inputOldPassword);
        final EditText inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        Button btnEditPassword = findViewById(R.id.btnEditPassword);

        btnEditPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final String password = inputPassword.getText().toString();
                final String old_password = inputOldPassword.getText().toString();
                final String confirm_password = inputConfirmPassword.getText().toString();

                if(!password.equals(confirm_password))
                    Toast.makeText(EditPasswordActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                else if(password.trim().length()<6 || old_password.trim().length()<6)
                    Toast.makeText(EditPasswordActivity.this, "Senhas precisa ter acima de 6 caracteres", Toast.LENGTH_SHORT).show();
                else{
                    final LoadingControl loadingControl = new LoadingControl(EditPasswordActivity.this);
                    loadingControl.startLoading();
                    FirebaseData.mAuth.signInWithEmailAndPassword(FirebaseData.mAuth.getCurrentUser().getEmail(),old_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                FirebaseData.mAuth.getCurrentUser().updatePassword(password).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            finish();
                                            Toast.makeText(EditPasswordActivity.this, "Senha atualizada", Toast.LENGTH_SHORT).show();
                                        } else{
                                            loadingControl.finishLoading();
                                            Toast.makeText(EditPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });
                            } else {
                                loadingControl.finishLoading();
                                Toast.makeText(EditPasswordActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }
}
