package com.example.adminpc.appstud.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.adminpc.appstud.AppStudApplication;
import com.example.adminpc.appstud.Model.Places;
import com.example.adminpc.appstud.R;
import com.squareup.picasso.Picasso;

/**
 * Created by adminPC on 18/05/2017.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{
    private Places places;

    public class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView mImageView;
        public TextView mTextView;

        public ViewHolder(View v){
            super(v);
            mImageView = (ImageView) v.findViewById(R.id.barImageView);
            mTextView = (TextView) v.findViewById(R.id.barTextView);

        }
    }


    public RecyclerViewAdapter(Places mplaces){

        places = mplaces;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_card_view, parent, false);
        // set the view's size, margins, paddings and layout parameters

        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(places.results[position].photos != null){

            Picasso.with(AppStudApplication.getContext()).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+places.results[position].photos[0].photoReference+"&key=AIzaSyBNJyNzvROoFZfTmo529RLpGO110sxUoGo").into(holder.mImageView);
        }
        holder.mTextView.setText(places.results[position].name);
    }

    @Override
    public int getItemCount() {
        return places.results.length;
    }

}
