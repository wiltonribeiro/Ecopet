package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.ecopet.will.ecopet.ControlClasses.LoadingControl;
import com.ecopet.will.ecopet.ControlClasses.UserPhotoControl;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {
    ImageView profileImage;
    TextView userName, userDescription;
    GridView userPhotos;
    UserPhotoControl userPhotoControl;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //recebo os dados do usuario atual para a variavel 'user'
        user = UserPhotoControl.user;

        // inicia a tela de carregamento
        final LoadingControl loadingControl = new LoadingControl(ProfileActivity.this);
        loadingControl.startLoading();

        //instancia os componentes de tela
        profileImage = findViewById(R.id.profileImage);
        userName = findViewById(R.id.userName);
        userDescription  = findViewById(R.id.userDescription);
        userPhotos = findViewById(R.id.userPhotos);

        //seta na tela os dados do usuario
        userName.setText(user.getName());
        userDescription.setText(("\""+user.getDescription()+"\""));


        //carrega a foto de perfil do usuario, ao carregar a tela de carregamento é fechada
        Picasso.with(ProfileActivity.this).load(user.getImage_url()).into(profileImage, new Callback() {
            @Override
            public void onSuccess() {
                loadingControl.finishLoading();
            }

            @Override
            public void onError() {

            }
        });

        //instancia o controle responsavel pela visualizacao das fotos do usuario
        userPhotoControl = new UserPhotoControl(ProfileActivity.this);
        //carrega as fotos do usuario
        userPhotoControl.loadUserPhotos(FirebaseData.currentUser.getUid());

    }

}
