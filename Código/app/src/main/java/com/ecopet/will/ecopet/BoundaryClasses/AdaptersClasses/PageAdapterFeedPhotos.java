package com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.EditPhotoActivity;
import com.ecopet.will.ecopet.BoundaryClasses.ActivitiesClasses.ReportActivity;
import com.ecopet.will.ecopet.ControlClasses.FeedControl;
import com.ecopet.will.ecopet.EntityClasses.OthersClasses.FirebaseData;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.User;
import com.ecopet.will.ecopet.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 16/03/18.
 */

public class PageAdapterFeedPhotos extends BaseAdapter {

    private List<Photo> myList = new ArrayList<>();
    private LayoutInflater inflater;
    private Context context;

    public PageAdapterFeedPhotos(Context context, List<Photo> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public Photo getItem(int position) {
        return myList.get(getCount() - position - 1);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_feed_photo, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final Photo currentListData = getItem(position);
        mViewHolder.outputDescriptionPhoto.setText(("\""+currentListData.getDescription()+"\""));
        Picasso.with(context).load(currentListData.getImage_url()).placeholder(context.getResources().getDrawable(R.drawable.progress)).into(mViewHolder.imageShown);
        mViewHolder.outputLikes.setText((currentListData.getLikes().size()+" curtidas"));

        if(currentListData.getLikes().containsKey(FirebaseData.mAuth.getCurrentUser().getUid()))
            mViewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.heart_colorful));
        else
            mViewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.heart_blank));

        mViewHolder.btnLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!currentListData.getLikes().containsKey(FirebaseData.mAuth.getCurrentUser().getUid())){
                    FeedControl.likePhoto(currentListData);
                    mViewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.heart_colorful));
                    mViewHolder.outputLikes.setText((currentListData.getLikes().size()+" curtidas"));
                }
                else{
                    FeedControl.unlikePhoto(currentListData);
                    mViewHolder.btnLike.setBackground(context.getResources().getDrawable(R.drawable.heart_blank));
                    mViewHolder.outputLikes.setText((currentListData.getLikes().size()+" curtidas"));
                }

            }
        });

        mViewHolder.btnReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popup = new PopupMenu(context, mViewHolder.btnReport);
                popup.getMenuInflater().inflate(R.menu.menu_home, popup.getMenu());

                popup.show();//showing popup menu

                if(!currentListData.getUid_user().equals(FirebaseData.currentUser.getUid()))
                    popup.getMenu().getItem(0).setVisible(false);

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        if(item.getTitle().equals("Denunciar")){
                            if(FirebaseData.currentUser.getReports().containsKey(currentListData.getUid_photo()))
                                Toast.makeText(context, "Você já denunciou essa imagem", Toast.LENGTH_SHORT).show();
                            else{
                                ReportActivity.photo = currentListData;
                                context.startActivity(new Intent(context,ReportActivity.class));
                            }
                        } else if(item.getTitle().equals("Editar")){
                            EditPhotoActivity.photo = currentListData;
                            context.startActivity(new Intent(context,EditPhotoActivity.class));
                        }
                        return true;
                    }
                });
            }
        });


        mViewHolder.outputNameUser.setText(currentListData.getName_user());
        mViewHolder.imageShown.setVisibility(View.VISIBLE);
        String url = FirebaseData.currentUser.getImage_url().replace(FirebaseData.currentUser.getUid(),currentListData.getUid_user());
        Picasso.with(context).load(url).into(mViewHolder.imageUser);


        return convertView;
    }

    private class MyViewHolder {
        TextView outputNameUser, outputDescriptionPhoto, outputLikes;
        Button btnLike, btnReport;
        ImageView imageShown, imageUser;
        public MyViewHolder(View item) {
            btnReport = item.findViewById(R.id.btnReport);
            outputNameUser = item.findViewById(R.id.outputNameUser);
            outputDescriptionPhoto = item.findViewById(R.id.outputDescriptionPhoto);
            outputLikes = item.findViewById(R.id.outputLikes);
            btnLike = item.findViewById(R.id.btnLike);
            imageShown= item.findViewById(R.id.imageShown);
            imageUser = item.findViewById(R.id.imageUser);
        }
    }
}

