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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 5/25/2016.
 */
public class SponsorListAdapter extends RecyclerView.Adapter<SponsorListAdapter.SponsorListAdapterViewHolder>{

    /****************************************************************************************/
    /*                                    Constants                                         */
    /****************************************************************************************/
    private final static String TAG = SponsorListAdapter.class.getSimpleName();
    private final static int LOGO_FADE_DURATION = 500;
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
    public void onBindViewHolder(final SponsorListAdapterViewHolder holder, int position) {

        Picasso.with(mContext).load(mSponsorList.get(position).getBackgroundImageUrl()).into(holder.backgroundImageView);
        holder.logoImageView.setAlpha(0f);
        Picasso.with(mContext).load(mSponsorList.get(position).getLogoImageUrl())
                .into(holder.logoImageView, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                holder.logoImageView.animate().setDuration(LOGO_FADE_DURATION).alpha(1f);
            }

            @Override
            public void onError() {

            }
        });
        holder.nameTextView.setText(mSponsorList.get(position).getName().replaceAll("&amp;","&"));
    }


    public class SponsorListAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView backgroundImageView;
        public ImageView logoImageView;
        public TextView nameTextView;
        public TextView descriptionTextView;

        public SponsorListAdapterViewHolder(View view) {
            super(view);

            backgroundImageView = (ImageView)view.findViewById(R.id.sponsor_background_image_view);
            backgroundImageView.setOnClickListener(this);

            logoImageView = (ImageView)view.findViewById(R.id.sponsor_logo_image_view);
            nameTextView = (TextView)view.findViewById(R.id.sponsor_name_text_view);
        }

        @Override
        public void onClick(View v) {

            mClickHandler.onClick(getAdapterPosition(),this);
        }
    }

    public interface SponsorListAdapterOnClickHandler {
        void onClick(int pos, SponsorListAdapterViewHolder vh);
    }
}
