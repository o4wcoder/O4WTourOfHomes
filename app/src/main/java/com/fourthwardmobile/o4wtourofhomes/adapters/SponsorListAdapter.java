package com.fourthwardmobile.o4wtourofhomes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.fourthwardmobile.o4wtourofhomes.R;
import com.fourthwardmobile.o4wtourofhomes.models.Sponsor;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 5/25/2016.
 */
public class SponsorListAdapter extends RecyclerView.Adapter<SponsorListAdapter.SponsorListAdapterViewHolder>{

    /****************************************************************************************/
    /*                                    Constants                                         */
    /****************************************************************************************/
    private final static String TAG = SponsorListAdapter.class.getSimpleName();

    /****************************************************************************************/
    /*                                    Local Data                                        */
    /****************************************************************************************/
    private Context mContext;
    private ArrayList<Sponsor> mSponsorList;
    private SponsorListAdapterOnClickHandler mClickHandler;

    public SponsorListAdapter(Context context, ArrayList<Sponsor> sponsorList, SponsorListAdapterOnClickHandler clickHandler) {

        mContext = context;
        mSponsorList = sponsorList;
        mClickHandler = clickHandler;
    }
    @Override
    public SponsorListAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.sponsor_list_item,parent,false);
        final SponsorListAdapterViewHolder vh = new SponsorListAdapterViewHolder(view);
        return vh;
    }

    @Override
    public int getItemCount() {
        return mSponsorList.size();
    }

    @Override
    public void onBindViewHolder(SponsorListAdapterViewHolder holder, int position) {

    }


    public class SponsorListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView backgroundImageView;
        public ImageView logoImageView;
        public TextView nameTextView;
        public TextView descriptionTextView;

        public SponsorListAdapterViewHolder(View view) {
            super(view);
        }

        @Override
        public void onClick(View v) {

        }
    }

    public interface SponsorListAdapterOnClickHandler {
        void onClick(int pos, SponsorListAdapterViewHolder vh);
    }
}
