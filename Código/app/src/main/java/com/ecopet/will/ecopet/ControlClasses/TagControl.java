package com.ecopet.will.ecopet.ControlClasses;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.FeedFragment;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Tag;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Created by willrcneto on 04/04/18.
 */

public class TagControl {
    private Context context;
    private static HashMap<String, Tag> tags = new HashMap<>();

    public TagControl(Context context) {
        this.context = context;
        searchTags();
    }

    private void searchTags(){
        FirebaseData.myRef.child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final Date current_date = new Date();

                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    Tag tag = postSnapshot.getValue(Tag.class);
                    try {
                        Date dateDeadline = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(tag.getDeadline());
                        if(current_date.before(dateDeadline)) {
                            tags.put("#"+postSnapshot.getKey(),tag);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setSelectedTag(Spinner spinner, String tag){
        spinner.setSelection(getIndex(spinner, tag));
    }

    private int getIndex(Spinner spinner, String myString){

        for (int i=0;i<spinner.getAdapter().getCount();i++){
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)){
                return i;
            }
        }
        return 0;
    }

    public void loadTags(final Spinner spinner){

        final Date current_date = new Date();
        final List<String> tagsList = new ArrayList<>();

        if(tags.size()>0)
            tags.clear();

        tagsList.add("Carregando...");
        adapterSpinner(spinner,tagsList);
        tagsList.clear();
        tagsList.add("Selecionar Tag");

        FirebaseData.myRef.child("tags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    tagsList.clear();
                    tagsList.add("Selecionar Tag");

                    for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                        Tag tag = postSnapshot.getValue(Tag.class);
                        try {
                            Date dateDeadline = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH).parse(tag.getDeadline());
                            if(current_date.before(dateDeadline)) {
                                tags.put("#" + postSnapshot.getKey(),tag);
                                tagsList.add("#" + postSnapshot.getKey());
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                        if(tagsList.size()+1==dataSnapshot.getChildrenCount()) {
                            adapterSpinner(spinner, tagsList);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void addPhotoTag(Photo photo){
        String tag = photo.getTag().replace("#","");
        FirebaseData.myRef.child("tag_photo").child(tag).child(photo.getUid_photo()).child("user").setValue(photo.getUid_user());
    }

    private void adapterSpinner(Spinner spinner, List<String> list){
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(context, R.layout.support_simple_spinner_dropdown_item, list);
        spinnerAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(spinnerAdapter);
    }

    static void setLastUpdate(String tag){

            if (getTimeDifference(tag)) {
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH);
                String currentDateAndTime = sdf.format(new Date());
                tags.get(tag).setLast_update(currentDateAndTime);
                FirebaseData.myRef.child("tags").child(tag.replace("#", "")).child("last_update").setValue(currentDateAndTime);
            }
    }

    static boolean getTimeDifference(String tag){
        Date currentUpdate = new Date();

        try {
            Date dateLastUpdate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.ENGLISH).parse(tags.get(tag).getLast_update());
            long diff = currentUpdate.getTime() - dateLastUpdate.getTime();
            long diffMinutes = diff / (60 * 1000) % 60;

            return diffMinutes > 1;//mudaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaar

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }
}
