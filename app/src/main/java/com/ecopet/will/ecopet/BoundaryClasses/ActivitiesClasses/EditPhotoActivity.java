package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.LoadingControl;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class EditPhotoActivity extends AppCompatActivity {

    public static Photo photo;
    private EditText textPhoto;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_photo);

        final LoadingControl loadingControl = new LoadingControl(EditPhotoActivity.this);
        textPhoto = findViewById(R.id.text_image);
        btnSave = findViewById(R.id.btnSaveEdit);
        textPhoto.setText(photo.getDescription());

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newText = textPhoto.getText().toString();
                loadingControl.startLoading();
                FirebaseData.myRef.child("photos").child(photo.getUid_photo()).child("description").setValue(newText).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        loadingControl.finishLoading();

                        if(task.isSuccessful())
                            finish();
                        else
                            Toast.makeText(EditPhotoActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }
}
