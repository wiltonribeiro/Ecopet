package com.ecopet.will.ecopet.ControlClasses;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses.PageAdapterRanking;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.UserLikeRanking;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by willrcneto on 04/04/18.
 */

public class RankingControl {
    private Context context;
    private static int count;
    private ListView lvDetail;
    private List<UserLikeRanking> userLikeRankings;
    HashMap<String,Integer> users;
    String tagToUpdate;

    public RankingControl(Context context, ListView lvDetail) {
        this.lvDetail = lvDetail;
        this.context = context;
        this.userLikeRankings = new ArrayList<>();
        this.users = new HashMap<>();
    }

    private Task setRanking(List<UserLikeRanking> userLikeRankings){
        HashMap<String, UserLikeRanking> ranking = new HashMap<>();
        for (UserLikeRanking userLikeRanking: userLikeRankings){
            ranking.put(userLikeRanking.getUserKey(),userLikeRanking);
        }

        return FirebaseData.myRef.child("ranking").child(tagToUpdate.replace("#","")).setValue(ranking);
    }

    private void getRanking(String tag, final View view){
        FirebaseData.myRef.child("ranking").child(tag).orderByChild("likes").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<UserLikeRanking> userLikeRankings = new ArrayList<>();
                for(DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    userLikeRankings.add(postSnapshot.getValue(UserLikeRanking.class));
                }

                if (dataSnapshot.getChildrenCount() == userLikeRankings.size())
                    showRanking(userLikeRankings,view);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void loadRanking(String tag, final View view){
        count = 0;
        userLikeRankings.clear();
        lvDetail.setAdapter(null);
        lvDetail.requestLayout();
        users.clear();
        tagToUpdate = tag;
        view.findViewById(R.id.loading).setVisibility(View.VISIBLE);

        tag = tag.replace("#","");
        final String finalTag = tag;


        if (TagControl.getTimeDifference(tagToUpdate)) {

            FirebaseData.myRef.child("tag_photo").child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    final int childrenCount = (int) dataSnapshot.getChildrenCount();

                    if (dataSnapshot.exists()) {

                        view.findViewById(R.id.nothing_here).setVisibility(View.GONE);

                        for (DataSnapshot postSnapShot : dataSnapshot.getChildren()) {
                            FirebaseData.myRef.child("photos").child(postSnapShot.getKey()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    Photo photo = dataSnapshot.getValue(Photo.class);
                                    if (!users.containsKey(photo.getUid_user())) {
                                        users.put(photo.getUid_user(), 0);
                                    }

                                    count++;
                                    int likes = 0;
                                    if (photo.getLikes() != null)
                                        likes = photo.getLikes().size();

                                    users.put(photo.getUid_user(), users.get(photo.getUid_user()) + likes);

                                    if (count == childrenCount) {
                                        sortHash(users, view, finalTag);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } else {
                        view.findViewById(R.id.loading).setVisibility(View.GONE);
                        view.findViewById(R.id.nothing_here).setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(context, "" + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            getRanking(tag, view);
        }
    }


    private void sortHash(HashMap<String,Integer> hashMap, View view, String tag){
        for(String key : hashMap.keySet()){
            userLikeRankings.add(new UserLikeRanking(key, hashMap.get(key)));
        }

        Collections.sort(userLikeRankings);
        getUserName(userLikeRankings, view, tag);
    }

    private static int count2;
    private void getUserName(final List<UserLikeRanking> userLikeRankings, final View view, final String tag){
        count2 = 0;
        for (final UserLikeRanking userLikeRanking: userLikeRankings){
            FirebaseData.myRef.child("users").child(userLikeRanking.getUserKey()).child("data").child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userLikeRanking.setUserName(dataSnapshot.getValue().toString());
                    count2++;
                    if(count2==userLikeRankings.size()){
                        setRanking(userLikeRankings).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful())
                                    getRanking(tag,view);
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    private void showRanking(List<UserLikeRanking> userLikeRankings, View view){
        lvDetail.setAdapter(new PageAdapterRanking(context, userLikeRankings));
        view.findViewById(R.id.loading).setVisibility(View.GONE);
        view.findViewById(R.id.nothing_here).setVisibility(View.GONE);

        TagControl.setLastUpdate(tagToUpdate);
    }

}
