package com.fourthwardmobile.o4wtourofhomes.adapters;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.helpers.Util;
import com.fourthwardmobile.o4wtourofhomes.models.Home;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 5/11/2016.
 */
public class FeaturedHomeListAdapter extends RecyclerView.Adapter<FeaturedHomeListAdapter.FeaturedHomeListAdapterViewHolder>{

    /*******************************************************************************/
    /*                               Constants                                     */
    /*******************************************************************************/
    private final static String TAG = FeaturedHomeListAdapter.class.getSimpleName();

    /*******************************************************************************/
    /*                               Local Data                                    */
    /*******************************************************************************/
    private Context mContext;
    private ArrayList<Home> mHomeList;
    private HomeListAdapterOnClickHandler mClickHandler;


    public FeaturedHomeListAdapter(Context context, ArrayList<Home> homeList, HomeListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mHomeList = homeList;
        mClickHandler = clickHandler;
    }

    @Override
    public FeaturedHomeListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.home_list_item,parent,false);
        final FeaturedHomeListAdapterViewHolder vh = new FeaturedHomeListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(FeaturedHomeListAdapterViewHolder holder, int position) {

        Log.e(TAG, "Bind image = " + mHomeList.get(position).getImageUrl());
        Picasso.with(mContext).load(mHomeList.get(position).getImageUrl()).into(holder.thumbnailImageView);
        ViewCompat.setTransitionName(holder.thumbnailImageView, Util.getTransitionName(mContext, position));
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    /**************************************************************************************/
    /*                                   Inner Classes                                    */
    /**************************************************************************************/
    public class FeaturedHomeListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView thumbnailImageView;

        public FeaturedHomeListAdapterViewHolder(View view) {
            super(view);

            thumbnailImageView = (ImageView)view.findViewById(R.id.home_thumbnail_image_view);
            thumbnailImageView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {

            Log.e(TAG,"HomeListAdapterViewHolder() onClick()");
          mClickHandler.onClick(getAdapterPosition(),this);
        }
    }

    public interface HomeListAdapterOnClickHandler {
        void onClick(int pos, FeaturedHomeListAdapterViewHolder vh);
    }
}
