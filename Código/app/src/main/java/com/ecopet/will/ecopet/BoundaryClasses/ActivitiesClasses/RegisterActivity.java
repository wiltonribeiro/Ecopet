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

import com.ecopet.will.ecopet.ControlClasses.RegisterControl;
import com.ecopet.will.ecopet.R;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RegisterActivity extends AppCompatActivity {

    EditText inputEmail, inputName, inputPassword, inputConfirmPassword, inputDescription;
    ImageView profileImage;
    Button btnRegister;
    LinearLayout imageArea;
    RegisterControl registerControl;
    private static int SELECT_PICTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //instancia os elementos de tela
        inputEmail = findViewById(R.id.inputEmail);
        inputName = findViewById(R.id.inputName);
        inputPassword = findViewById(R.id.inputPassword);
        inputConfirmPassword = findViewById(R.id.inputConfirmPassword);
        inputDescription = findViewById(R.id.inputDescription);
        btnRegister = findViewById(R.id.btnRegister);
        profileImage = findViewById(R.id.profileImage);
        imageArea = findViewById(R.id.selectImageArea);

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

        //caso o botao de registro seja clicado
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //valida os dados do usuario
                if(inputName.getText().toString().trim().isEmpty())
                    Toast.makeText(RegisterActivity.this, "Nome obrigatório", Toast.LENGTH_SHORT).show();
                else if(inputPassword.getText().toString().trim().isEmpty())
                    Toast.makeText(RegisterActivity.this, "Senha obrigatório", Toast.LENGTH_SHORT).show();
                else if(inputEmail.getText().toString().trim().isEmpty())
                    Toast.makeText(RegisterActivity.this, "Email obrigatório", Toast.LENGTH_SHORT).show();
                else if(!inputPassword.getText().toString().equals(inputConfirmPassword.getText().toString()))
                    Toast.makeText(RegisterActivity.this, "Senhas não coincidem", Toast.LENGTH_SHORT).show();
                else if(inputPassword.getText().toString().trim().length()<6)
                    Toast.makeText(RegisterActivity.this, "As senhas devem possuir mais de 6 caracteres", Toast.LENGTH_SHORT).show();
                else{
                    //instacia o controle responsavel pelo registro do usuario
                    registerControl = new RegisterControl(RegisterActivity.this);
                    String email = inputEmail.getText().toString().trim();
                    String password = inputPassword.getText().toString().trim();
                    String name = inputName.getText().toString();
                    String description = inputDescription.getText().toString();

                    //faz a autentifcacao com os dados passado pelo usuario
                    registerControl.authUser(email,password,name,description);
                }


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
                    profileImage.setImageBitmap(bitmap);

                } else {

                    ParcelFileDescriptor parcelFileDescriptor;
                    try {
                        parcelFileDescriptor = getContentResolver().openFileDescriptor(selectedImageURI, "r");
                        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(fileDescriptor);
                        parcelFileDescriptor.close();

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
