package com.ecopet.will.ecopet.BoundaryClasses.AdaptersClasses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ecopet.will.ecopet.EntityClasses.DataClasses.UserLikeRanking;
import com.ecopet.will.ecopet.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by willrcneto on 16/03/18.
 */

public class PageAdapterRanking extends BaseAdapter {
    List<UserLikeRanking> myList = new ArrayList<>();
    LayoutInflater inflater;
    Context context;
    MyViewHolder mViewHolder;

    public PageAdapterRanking(Context context, List<UserLikeRanking> myList) {
        this.myList = myList;
        this.context = context;
        inflater = LayoutInflater.from(this.context);
    }


    @Override
    public int getCount() {
        return myList.size();
    }

    @Override
    public UserLikeRanking getItem(int position) {
        return myList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final MyViewHolder mViewHolder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.layout_user_ranking, parent, false);
            mViewHolder = new MyViewHolder(convertView);
            convertView.setTag(mViewHolder);

        } else {
            mViewHolder = (MyViewHolder) convertView.getTag();
        }

        final UserLikeRanking currentListData = getItem(position);

        mViewHolder.tvNameUser.setText(currentListData.getUserName());
        mViewHolder.tvLikes.setText(String.valueOf(currentListData.getLikes()));
        mViewHolder.tvPosition.setText((String.valueOf(position+1)+"º"));

        return convertView;
    }

    private class MyViewHolder {
        TextView tvPosition, tvNameUser, tvLikes;
        public MyViewHolder(View item) {
            tvPosition = item.findViewById(R.id.tvPosition);
            tvNameUser = item.findViewById(R.id.tvNameUser);
            tvLikes = item.findViewById(R.id.tvCountLikes);
        }
    }
}

