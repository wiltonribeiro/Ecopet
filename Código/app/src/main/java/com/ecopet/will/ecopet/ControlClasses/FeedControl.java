package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses.PageAdapterFeedPhotos;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Created by willrcneto on 15/03/18.
 */

public class FeedControl {

    private View view;
    static String lastPhoto = "";
    static String lastTag = "";
    private ArrayList<Photo> myList;
    private ListView lvDetail;
    private LoadingControl loadingControl;

    public FeedControl(View view) {
        this.view = view;
        this.myList = new ArrayList<>();
        lastPhoto = "";
        lvDetail = view.findViewById(R.id.listPhotos);
        loadingControl = new LoadingControl(view);
    }

    //responsavel por curtir a photo
    public static void likePhoto(final Photo photo){
        //formata a data de curtida
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        String currentDateAndTime = sdf.format(new Date());

        // adiciona ao mapa de likes de uma foto, aquele que acabou de a curtir
        photo.getLikes().put(FirebaseData.mAuth.getCurrentUser().getUid(), currentDateAndTime);

        //envia ao banco a interação do like
        FirebaseData.myRef.child("photos").child(photo.getUid_photo()).child("likes")
                .child(FirebaseData.mAuth.getCurrentUser().getUid()).
                child("date_time")
                .setValue(photo.getLikes().put(FirebaseData.mAuth.getCurrentUser().getUid(),currentDateAndTime))
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    public static void unlikePhoto(final Photo photo){

        //remove do mapa de likes de uma foto, aquele que a está descurtindo
        photo.getLikes().remove(FirebaseData.mAuth.getCurrentUser().getUid());

        //remove do banco a interação do like
        FirebaseData.myRef.child("photos").child(photo.getUid_photo()).child("likes")
                .child(FirebaseData.mAuth.getCurrentUser().getUid())
                .removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
            }
        });
    }

    private void listenerEndList(){
        lvDetail.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

                if (lvDetail.getAdapter() != null && lvDetail.getCount() >1 && lvDetail.getLastVisiblePosition() ==
                        lvDetail.getAdapter().getCount() -1 && lvDetail.getChildAt(lvDetail.getChildCount() - 1)  != null &&
                        lvDetail.getChildAt(lvDetail.getChildCount() - 1).getBottom() <= lvDetail.getHeight()){
                        loadPhotos(lastTag);
                }
            }
        });
    }

    public ArrayList<Photo> getMyList() {
        return myList;
    }

    public void loadPhotos(final String tag){

        if(!tag.equals(lastTag)){
            lastPhoto = "";
            loadingControl.startBlankLoading();
        }

        Query myRef;
        if(!lastPhoto.equals(""))
            myRef = FirebaseData.myRef.child("tag_photo").child(tag).orderByKey().endAt(lastPhoto).limitToLast(6);
        else {
            myList.clear();
            myRef = FirebaseData.myRef.child("tag_photo").child(tag).orderByKey().startAt(lastPhoto).limitToLast(6);
        }

        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.i("teste",""+dataSnapshot.getChildrenCount());
                if (dataSnapshot.exists()) {
                    view.findViewById(R.id.nothing_here).setVisibility(View.GONE);

                    final long count = dataSnapshot.getChildrenCount();
                    final int initialListSize =  myList.size();

                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if(!postSnapshot.getKey().equals(lastPhoto)) {
                            FirebaseData.myRef.child("photos").child(postSnapshot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Photo photo = dataSnapshot.getValue(Photo.class);
                                    if (!lastPhoto.equals(""))
                                        myList.add(0, photo);
                                    else
                                        myList.add(photo);

                                    if(count == myList.size()-initialListSize || (count == myList.size()-initialListSize+1 && !lastPhoto.equals(""))){
                                        if(lastPhoto.equals(""))
                                            lvDetail.setAdapter(new PageAdapterFeedPhotos(view.getContext(), myList));
                                        else
                                            lvDetail.requestLayout();

                                        view.findViewById(R.id.loadingFeed).setVisibility(View.GONE);

                                        if(myList.size()!=0 )
                                            lastPhoto = myList.get(0).getUid_photo();

                                        loadingControl.finishLoading();
                                        lastTag = tag;
                                        listenerEndList();
                                    }

                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }

                    }

                } else {
                    myList.clear();
                    lvDetail.requestLayout();
                    //caso nao exista foto alguma, mostra dela de ausencia de fotos
                    loadingControl.finishLoading();
                    view.findViewById(R.id.nothing_here).setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(view.getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
