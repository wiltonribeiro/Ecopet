package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.ecopet.will.ecopet.ControlClasses.FeedControl;
import com.ecopet.will.ecopet.ControlClasses.TagControl;
import com.ecopet.will.ecopet.ControlClasses.UserPhotoControl;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.R;

/**
 * Created by willrcneto on 15/03/18.
 */

public class FeedFragment extends android.support.v4.app.Fragment {

    private Context context;
    private FeedControl feedControl;
    private TagControl tagControl;
    private Spinner spinnerTag;
    public static String tagBefore = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //exibe o fragmento de feed
        final View rootView = inflater.inflate(R.layout.fragment_feed, container, false);
        context = rootView.getContext();

        feedControl = new FeedControl(rootView);
        tagControl = new TagControl(context);

        //instacia os elemtnos da tela
        spinnerTag = rootView.findViewById(R.id.spinnerTag);
        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        Button btnProfile = rootView.findViewById(R.id.btnProfile);
        Button btnMenu = rootView.findViewById(R.id.btnMenu);

        tagControl.loadTags(spinnerTag);


        spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    rootView.findViewById(R.id.tag_unselect).setVisibility(View.INVISIBLE);
                    tagBefore = spinnerTag.getSelectedItem().toString().replace("#","");
                    feedControl.loadPhotos(tagBefore);
                } else{
                    rootView.findViewById(R.id.tag_unselect).setVisibility(View.VISIBLE);
                    rootView.findViewById(R.id.nothing_here).setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.AppColor)));

        // vai para o perfil
        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // passa ao controle de usuario o obj do usuario atual
                UserPhotoControl.user = FirebaseData.currentUser;
                startActivity(new Intent(context, ProfileActivity.class));
            }
        });


        //vai para adicionar foto
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, AddPhotoActivity.class));
            }
        });

        //vai para o menu
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, MenuActivity.class));
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!tagBefore.equals("")) {
            feedControl.loadPhotos(tagBefore);
            tagControl.setSelectedTag(spinnerTag,"#"+FeedFragment.tagBefore);
        }

    }

}

