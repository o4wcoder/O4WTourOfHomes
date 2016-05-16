package com.fourthwardmobile.o4wtourofhomes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.activities.MainActivity;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 5/11/2016.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>{

    /*******************************************************************************/
    /*                               Constants                                     */
    /*******************************************************************************/
    private final static String TAG = HomeListAdapter.class.getSimpleName();

    /*******************************************************************************/
    /*                               Local Data                                    */
    /*******************************************************************************/
    private Context mContext;
    private ArrayList<Home> mHomeList;

    public HomeListAdapter(Context context, ArrayList<Home> homeList) {

        mContext = context;
        mHomeList = homeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_item,parent,false);
        final ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Log.e(TAG,"Bind image = " + mHomeList.get(position).getImageUrl());
      Picasso.with(mContext).load(mHomeList.get(position).getImageUrl()).into(holder.thumbnailImageView);
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView thumbnailImageView;

        public ViewHolder(View view) {
            super(view);

            thumbnailImageView = (ImageView)view.findViewById(R.id.home_thumbnail_image_view);
        }


    }
}
