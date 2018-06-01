package com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.ecopet.will.ecopet.ControlClasses.UserPhotoControl;
import com.ecopet.will.ecopet.EntityClasses.DataClasses.Photo;
import com.ecopet.will.ecopet.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 16/03/18.
 */

public class PageAdapterUserPhotos extends BaseAdapter {
    List<Photo> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    MyViewHolder mViewHolder;
    UserPhotoControl userPhotoControl;

    public PageAdapterUserPhotos(Context context, List<Photo> myList, UserPhotoControl userPhotoControl) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.userPhotoControl = userPhotoControl;
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
            convertView = inflater.inflate(R.layout.layout_user_photo, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }


        final Photo currentListData = getItem(position);
        Picasso.with(context).load(currentListData.getImage_url()).placeholder(R.drawable.progress).into(mViewHolder.imageShown);

        final PhotoOptions photoOptions = new PhotoOptions(context,userPhotoControl,currentListData);


        mViewHolder.imageShown.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                photoOptions.activatePhotoOption();
                return false;
            }
        });


//        android.view.ViewGroup.LayoutParams mParams = mViewHolder.imageShown.getLayoutParams();
//        mParams.height = mViewHolder.imageShown.getWidth();
//        mViewHolder.imageShown.setLayoutParams(mParams);

        return convertView;
    }

    private class MyViewHolder {
        ImageView imageShown;
        public MyViewHolder(View item) {
            imageShown= item.findViewById(R.id.imageShown);
        }
    }

    private class PhotoOptions implements Animation.AnimationListener{
        private RelativeLayout layoutPhotoOptions;
        private Button btnCancel, btnDelete, btnShare;
        private int ordem;
        private Animation animation;
        private UserPhotoControl userPhotoControl;
        private Photo photo;

        private PhotoOptions(Context context, UserPhotoControl userPhotoControl, Photo photo){
            this.layoutPhotoOptions = ((Activity) context).findViewById(R.id.layoutPhotoOptions);
            this.btnCancel = ((Activity) context).findViewById(R.id.btnCancel);
            this.btnDelete = ((Activity) context).findViewById(R.id.btnDelete);
            this.btnShare = ((Activity) context).findViewById(R.id.btnShare);
            this.userPhotoControl = userPhotoControl;
            this.photo = photo;
        }

        private void activatePhotoOption(){
            ordem = 1;
            layoutPhotoOptions.setVisibility(View.VISIBLE);
            btnCancel.setVisibility(View.INVISIBLE);
            btnDelete.setVisibility(View.INVISIBLE);
            btnShare.setVisibility(View.INVISIBLE);

            btnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    layoutPhotoOptions.setVisibility(View.GONE);
                }
            });

            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(context,R.style.Theme_AppCompat_DialogWhenLarge)
                            .setTitle("Deletar Foto")
                            .setMessage("Tem certeza que deseja excluir essa imagem ? Essa ação não poderá ser desfeita.")
                            .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    userPhotoControl.deletePhoto(photo);
                                    layoutPhotoOptions.setVisibility(View.GONE);
                                }
                            })
                            .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // do nothing
                                }
                            })
                            .show();
                }
            });

            animation = AnimationUtils.loadAnimation(context, R.anim.center_top);
            animation.setAnimationListener(this);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.startAnimation(animation);

        }


        @Override
        public void onAnimationStart(Animation animation) {
            ordem++;
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            if(ordem==2){
                animation = AnimationUtils.loadAnimation(context, R.anim.center_top);
                animation.setAnimationListener(this);
                btnShare.setVisibility(View.VISIBLE);
                btnShare.startAnimation(animation);
            }
            else if(ordem==3){
                animation = AnimationUtils.loadAnimation(context, R.anim.center_top);
                animation.setAnimationListener(this);
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.startAnimation(animation);
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}

