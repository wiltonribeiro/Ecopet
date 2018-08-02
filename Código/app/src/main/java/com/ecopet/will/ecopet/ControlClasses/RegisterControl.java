package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.HomeActivity;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by willrcneto on 16/04/18.
 */

public class RegisterControl {

    private Activity activity;
    private Context context;
    private LoadingControl loadingControl;

    public RegisterControl (Context context) {
        this.activity = (Activity) context;
        this.context = context;
        this.loadingControl = new LoadingControl(activity);
    }

    //realiza a autentificacao do usuario
    public void authUser(String email, String password, String name, String description){

        // instancia um onj de usuario com os dados passados
        final User user = new User();
        user.setName(name);
        user.setDescription(description);
        user.setEmail(email);

        //inicia tela de carregamento
        loadingControl.startLoading();

        //faz a autenticacao do usuario pelo firebase com o email e senha passados
        FirebaseData.mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //caso dê certo
                if(task.isSuccessful()){
                    //registra o usuario no banco
                    registerUser(user);
                }
                else {
                    Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    loadingControl.finishLoading();
                }

            }
        });
    }

    //registra o usuario no banco
    public void registerUser(final User user){
        loadingControl.startLoading();
        //agora que o usuario esta logado, ele possui um id proprio
        user.setUid(FirebaseData.mAuth.getCurrentUser().getUid());

        //pega a imagem correspondente
        ImageView image_profile = activity.findViewById(R.id.profileImage);

        //aplica uma escuta para o envio da imagem ao servidor
        uploadImage(image_profile, user).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //caso a imagem seja enviada ao servidor
                if (task.isSuccessful()) {
                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            //recolhe a url de destino da imagem
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            //seta a url da imagem como imagem de perfil do usuario
                            user.setImage_url(downloadUrl.toString());
                            //envia os dados do usuario ao banco
                            FirebaseData.myRef.child("users").child(user.getUid()).child("data").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    //verifica se o envio deu certo
                                    if(task.isSuccessful()) {
                                        //seleciona como usuario atual os dados do usuario recem enviados
                                        FirebaseData.currentUser = user;
                                        //direciona o usuario para tela home
                                        activity.finishAffinity();
                                        activity.startActivity(new Intent(context, HomeActivity.class));
                                    }
                                    else
                                        Toast.makeText(context, task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                                }
                            });
                        }
                    });
                } else {
                    loadingControl.finishLoading();
                    Toast.makeText(context,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // prepara o ambiente para upload da imagem ao servidor
    private UploadTask uploadImage(ImageView imageView, User user){
        StorageReference storageReference = FirebaseData.storageRef;

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] data = baos.toByteArray();

        StorageReference ImagesRef = storageReference.child("images/user/"+user.getUid()+"/"+user.getUid()+"_profile_image.jpg");
        UploadTask uploadTask = ImagesRef.putBytes(data);

        return uploadTask;
    }
}
