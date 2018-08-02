package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.EditProfileControl;
import com.ecopet.will.ecopet.ControlClasses.LoadingControl;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class EditProfileActivity extends AppCompatActivity {

    EditText inputName, inputDescription;
    ImageView profileImage;
    Button btnUpdate;
    LinearLayout imageArea;
    EditProfileControl editProfileControl;
    LoadingControl loadingControl;
    User user;
    boolean image_selected = false;
    private static int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        loadingControl = new LoadingControl(EditProfileActivity.this);
        loadingControl.startLoading();

        btnUpdate = findViewById(R.id.btnUpdateProfile);
        inputName = findViewById(R.id.inputName);
        inputDescription = findViewById(R.id.inputDescription);
        imageArea = findViewById(R.id.selectImageArea);
        profileImage = findViewById(R.id.profileImage);

        editProfileControl = new EditProfileControl(EditProfileActivity.this);

        user = FirebaseData.currentUser;
        Picasso.with(EditProfileActivity.this).load(user.getImage_url()).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                inputName.setText(user.getName());
                inputDescription.setText(user.getDescription());
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputName.getText().toString().trim().isEmpty())
                    Toast.makeText(EditProfileActivity.this, "Nome obrigatório", Toast.LENGTH_SHORT).show();
                else {
                    FirebaseData.currentUser.setName(inputName.getText().toString().trim());
                    FirebaseData.currentUser.setDescription(inputDescription.getText().toString().trim());

                    if (image_selected)
                        editProfileControl.updateUserWithImage();
                    else
                        editProfileControl.updateUserWithoutImage();
                }
            }
        });

        //abre a galeria para a busca de imagem
        imageArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PICTURE);
            }
        });


    }


    // recebe a imagem da galeria e exibe para o usuario a imagem selecionada
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {

                Uri selectedImageURI = data.getData();
                if (Build.VERSION.SDK_INT < 19){

                    String selectedImagePath = getPath(selectedImageURI);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    image_selected = true;
                    profileImage.setImageBitmap(bitmap);

                } else {

                    ParcelFileDescriptor parcelFileDescriptor;
                    try {

                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageURI, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

                        image_selected = true;
                        profileImage.setImageBitmap(bitmap);

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    public String getPath(Uri uri) {
        if (uri == null) {
            return null;
        }
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        return uri.getPath();
    }
}
