package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.widget.ImageView;
import android.widget.Toast;

import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

/**
 * Created by willrcneto on 04/04/18.
 */

public class AddPhotoControl {
    private Activity activity;
    private LoadingControl loadingControl;

    public AddPhotoControl(Activity activity) {
        this.activity = activity;
        this.loadingControl = new LoadingControl(activity);
    }

    public void addPhoto(String description, String tag, ImageView imageSelected){
        //inicia o carregamento da página
        loadingControl.startLoading();

        //instancia um obj foto com as caracteristicas passadas
        final Photo photo = new Photo();
        photo.setReports(0);
        photo.setDescription(description);
        photo.setName_user(FirebaseData.currentUser.getName());
        photo.setTag(tag);

        // instancia o controle da Tag
        final TagControl tagControl = new TagControl(activity);

        // gera um key/id para a imagem
        String key = FirebaseData.myRef.push().getKey();
        photo.setUid_photo(key);

        //seta o id do responsavel pela publicao da foto
        photo.setUid_user(FirebaseData.mAuth.getCurrentUser().getUid());

        //aplica uma escuta para o envio da imagem ao banco
        uploadImage(imageSelected,key).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //checa o envio da imagem
                if (task.isSuccessful()) {
                    task.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();
                            // recebe a url da imagem enviada ao servidor e seta na foto
                            photo.setImage_url(downloadUrl.toString());

                            // envia os dados da foto ao nó de fotos do banco
                            FirebaseData.myRef.child("photos").child(photo.getUid_photo()).setValue(photo).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // se o envio funcionar
                                    if(task.isSuccessful()) {
                                        //cadastra uma referencia a foto enviada no nó de fotos do usuário
                                        FirebaseData.myRef.child("users").child(FirebaseData.mAuth.getCurrentUser().getUid())
                                                .child("photos").child(photo.getUid_photo()).child("data_time").setValue(ServerValue.TIMESTAMP).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {

                                                //desliga a tela de carregamento
                                                loadingControl.finishLoading();

                                                if (task.isSuccessful()){
                                                    //sai da tela de adicionar foto
                                                    activity.finish();
                                                    FeedControl.lastPhoto = "";
                                                    //adiciona uma referencia da foto enviada ao nó de foto por tag
                                                    tagControl.addPhotoTag(photo);
                                                }

                                                //erro
                                                else
                                                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();


                                            }
                                        });
                                    }
                                    else {
                                        //erro
                                        loadingControl.finishLoading();
                                        Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }
                    });
                } else {
                    //erro
                    loadingControl.finishLoading();
                    Toast.makeText(activity,task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // prepara o ambiente para upload da imagem ao servidor
    private UploadTask uploadImage(ImageView imageView, String key){
        StorageReference storageReference = FirebaseData.storageRef;

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 40, baos);
        byte[] data = baos.toByteArray();

        StorageReference ImagesRef = storageReference.child("images/user/"+FirebaseData.mAuth.getCurrentUser().getUid()+"/photos/"+key+".jpg");
        UploadTask uploadTask = ImagesRef.putBytes(data);

        return uploadTask;
    }
}
