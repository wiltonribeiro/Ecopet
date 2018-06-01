package com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.ecopet.will.ecopet.ControlClasses.RankingControl;
import com.ecopet.will.ecopet.ControlClasses.TagControl;
import com.ecopet.will.ecopet.R;

/**
 * Created by willrcneto on 15/03/18.
 */

public class RankingFragment extends android.support.v4.app.Fragment {

    private Context context;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_ranking, container, false);
        context = rootView.getContext();

        final Spinner spinnerTag = rootView.findViewById(R.id.spinnerTag);
        final Button btnUpdateRanking = rootView.findViewById(R.id.btnUpdateRanking);

        final TagControl tagControl = new TagControl(context);
        tagControl.loadTags(spinnerTag);

        final ListView listView = rootView.findViewById(R.id.listRanking);

        final RankingControl rankingControl = new RankingControl(context, listView);

        btnUpdateRanking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = spinnerTag.getSelectedItemPosition();
                if (position!=0)
                    rankingControl.loadRanking(spinnerTag.getSelectedItem().toString(), rootView);
                else {
                    listView.setAdapter(null);
                    listView.requestLayout();
                }
            }
        });

        spinnerTag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i!=0){
                    rankingControl.loadRanking(spinnerTag.getSelectedItem().toString(), rootView);
                }else {
                    listView.setAdapter(null);
                    listView.requestLayout();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        return  rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
}

