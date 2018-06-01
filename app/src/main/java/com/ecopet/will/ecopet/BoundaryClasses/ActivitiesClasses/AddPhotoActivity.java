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
import android.widget.Spinner;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.AddPhotoControl;
import com.ecopet.will.ecopet.ControlClasses.TagControl;
import com.ecopet.will.ecopet.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class AddPhotoActivity extends AppCompatActivity {

    Button btnSelectPhoto, btnAddImage;
    ImageView imageSelected;
    EditText inputImageDescription;
    Spinner spinnerTags;
    boolean imageChanged;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);

        // instacia dos elementos da tela de adicionar foto
        btnAddImage = findViewById(R.id.btnAddImage);
        btnSelectPhoto = findViewById(R.id.btnSelectPhoto);
        imageSelected = findViewById(R.id.imageSelected);
        spinnerTags = findViewById(R.id.spinnerTag);
        inputImageDescription = findViewById(R.id.inputImageDescription);
        imageChanged = false;

        //carregamento as tags disponiveis
        TagControl tagControl = new TagControl(AddPhotoActivity.this);
        tagControl.loadTags(spinnerTags);

        //busca a imagem na galeria
        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
            }
        });

        btnAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // se houver alguma imagem selecionada
                if(imageChanged && spinnerTags.getSelectedItemPosition() != 0){

                    String descriptionPhoto = inputImageDescription.getText().toString();
                    String tagPhoto = spinnerTags.getSelectedItem().toString();
                    ImageView imageSelected = findViewById(R.id.imageSelected);


                    //instacia o controle responsavel por adicionar foto
                    AddPhotoControl addPhotoControl = new AddPhotoControl(AddPhotoActivity.this);
                    addPhotoControl.addPhoto(descriptionPhoto, tagPhoto, imageSelected);

                } else if(!imageChanged){
                    Toast.makeText(AddPhotoActivity.this, "Necessário selecionar imagem", Toast.LENGTH_SHORT).show();
                } else if(spinnerTags.getSelectedItemPosition()==0){
                    Toast.makeText(AddPhotoActivity.this, "Necessário selecionar uma tag", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    // recebe a imagem da galeria e exibe para o usuario a imagem selecionada
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {

                Uri selectedImageURI = data.getData();
                if (Build.VERSION.SDK_INT < 19){

                    String selectedImagePath = getPath(selectedImageURI);
                    Bitmap bitmap = BitmapFactory.decodeFile(selectedImagePath);
                    imageChanged = true;
                    imageSelected.setImageBitmap(bitmap);

                } else {

                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageURI, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();
                        imageChanged = true;
                        imageSelected.setImageBitmap(bitmap);

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
