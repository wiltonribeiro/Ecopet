package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by willrcneto on 21/04/18.
 */

public class EditProfileControl {
    private Activity activity;
    private LoadingControl loadingControl;
    private int count_edit_photo;

    public EditProfileControl(Activity activity) {
        this.count_edit_photo = 0;
        this.activity = activity;
        this.loadingControl = new LoadingControl(activity);
    }

    //registra o usuario no banco
    public void updateUserWithImage(){
        loadingControl.startLoading();
        final User user = FirebaseData.currentUser;

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
                                    //fecha a tela de login
                                    loadingControl.finishLoading();
                                    //verifica se o envio deu certo
                                    if(task.isSuccessful()) {
                                        //direciona o usuario para tela home
                                        updateNameUserPhoto(user.getName());
                                    }
                                    else
                                        Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } else {
                    loadingControl.finishLoading();
                    Toast.makeText(activity,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void updateUserWithoutImage(){
        final User user = FirebaseData.currentUser;
        loadingControl.startLoading();
        //envia os dados do usuario ao banco
        FirebaseData.myRef.child("users").child(user.getUid()).child("data").setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                //fecha a tela de login
                loadingControl.finishLoading();

                //verifica se o envio deu certo
                if(task.isSuccessful()) {
                    //direciona o usuario para tela home
                    updateNameUserPhoto(user.getName());
                }
                else
                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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

    private void updateNameUserPhoto(final String name){
        FirebaseData.myRef.child("users").child(FirebaseData.currentUser.getUid()).child("photos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // verifica se há fotos do usuário
                if (dataSnapshot.exists()) {
                    // armazena em 'size' a quantidade de fotos do usuario
                    final int size = (int)dataSnapshot.getChildrenCount();

                    // pesquisa pelo id de cada foto postada pelo usuario no nó onde estão armazenadas todas as fotos
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseData.myRef.child("photos").child(postSnapshot.getKey()).child("name_user").setValue(name).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                count_edit_photo++;

                                if(count_edit_photo==size)
                                    activity.finish();
                            }
                        });
                    }
                    // exibe tela que não existe nenhuma foto cadastrada
                } else {
                    activity.finish();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}