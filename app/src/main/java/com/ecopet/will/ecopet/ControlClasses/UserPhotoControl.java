package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.GridView;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses.PageAdapterUserPhotos;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

/**
 * Created by willrcneto on 25/03/18.
 */

public class UserPhotoControl {

    private LoadingControl loadingControl;
    private Activity activity;
    public static User user;

    public UserPhotoControl(Activity activity) {
        this.activity = activity;
        this.loadingControl = new LoadingControl(activity);
    }

    //remove do banco a foto selecionada
    public void deletePhoto(final Photo photo){
        FirebaseData.myRef.child("photos").child(photo.getUid_photo()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // se for removida
                if(task.isSuccessful()) {
                    // deleta foto do armazenamento da arquivos do firebase
                    StorageReference storageReference = FirebaseData.storageRef;
                    StorageReference desertRef = storageReference.child("images/user/" + photo.getUid_user() + "/" +"/photos/"+ photo.getUid_photo() + ".jpg");
                    FirebaseData.myRef.child("users").child(photo.getUid_user()).child("photos").child(photo.getUid_photo()).removeValue();
                    FirebaseData.myRef.child("tag_photo").child(photo.getTag().replace("#","")).child(photo.getUid_photo()).removeValue();
                    desertRef.delete();
                    FeedControl.lastPhoto = "";
                    //carrega novamente as fotos do usuario (agora sem a foto excluida)
                    loadUserPhotos(photo.getUid_user());
                } else {
                    Toast.makeText(activity, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void loadUserPhotos(String user_uid){
        //instancia os elementos de tela responsaveis pelo carregmanto das fotos
        final UserPhotoControl userPhotoControl = this;
        final ArrayList<Photo> myList = new ArrayList<>();
        final GridView userPhotos = activity.findViewById(R.id.userPhotos);

        //faz a busca das referencia de fotos postadas pelo usuario
        FirebaseData.myRef.child("users").child(user_uid).child("photos").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // verifica se há fotos do usuário
                if (dataSnapshot.exists()) {
                    // armazena em 'size' a quantidade de fotos do usuario
                    final int size = (int)dataSnapshot.getChildrenCount();
                    activity.findViewById(R.id.nothing_here).setVisibility(View.GONE);
                    loadingControl.startBlankLoading();

                    // pesquisa pelo id de cada foto postada pelo usuario no nó onde estão armazenadas todas as fotos
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        FirebaseData.myRef.child("photos").child(postSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //recebe os dados da foto postada
                                Photo photo = dataSnapshot.getValue(Photo.class);
                                //adiciona a foto encontrada em uma lista
                                myList.add(photo);

                                //verifica quando todas as pesquisas estiverem terminadas
                                if (size == myList.size()){
                                    //tela de carregamento fechada
                                    loadingControl.finishLoading();
                                    //adapto a o grid de fotos a lista que contem as fotos
                                    userPhotos.setAdapter(new PageAdapterUserPhotos(activity, myList, userPhotoControl));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Toast.makeText(activity, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    // exibe tela que não existe nenhuma foto cadastrada
                } else {
                    myList.clear();
                    userPhotos.requestLayout();
                    activity.findViewById(R.id.nothing_here).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                //Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
