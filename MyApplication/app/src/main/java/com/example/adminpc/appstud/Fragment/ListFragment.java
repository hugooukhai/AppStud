package com.example.adminpc.appstud.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.adminpc.appstud.Adapter.RecyclerViewAdapter;
import com.example.adminpc.appstud.AppStudApplication;
import com.example.adminpc.appstud.Model.Places;
import com.example.adminpc.appstud.R;

/**
 * Created by adminPC on 18/05/2017.
 */

public class ListFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerViewAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AppStudApplication.getContext());
        mRecyclerView.setLayoutManager(mLayoutManager);


    }

    public void setupRecyclerView(Places placesResponse){
        mAdapter = new RecyclerViewAdapter(placesResponse);
        mRecyclerView.setAdapter(mAdapter);
    }
}
