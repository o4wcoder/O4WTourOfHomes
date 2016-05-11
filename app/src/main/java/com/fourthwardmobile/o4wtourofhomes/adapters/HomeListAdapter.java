package com.fourthwardmobile.o4wtourofhomes.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.fourthwardmobile.o4wtourofhomes.models.Home;

import java.util.ArrayList;

/**
 * Created by Chris Hare on 5/11/2016.
 */
public class HomeListAdapter extends RecyclerView.Adapter<HomeListAdapter.ViewHolder>{

    private Context mContext;
    private ArrayList<Home> mHomeList;

    public HomeListAdapter(Context context, ArrayList<Home> homeList) {

        mContext = context;
        mHomeList = homeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View view) {
            super(view);
        }

    }
}
