package com.hiten_khatri.xmusic;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;

public class songFrag extends Fragment {

    @SuppressLint("StaticFieldLeak")
    public static View view1;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView songRecycler;
    @SuppressLint("StaticFieldLeak")
    public static songAdapter songAdapter;

    public String mParam1;
    public String mParam2;

    public songFrag() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view1 = inflater.inflate(R.layout.fragment_song, container, false);
        songRecycler=view1.findViewById(R.id.songRecycler);
        songRecycler.setNestedScrollingEnabled(false);
        songRecycler.setHasFixedSize(true);
        songRecycler.setItemViewCacheSize(30);
        songRecycler.setDrawingCacheEnabled(true);
        songRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        songRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        songAdapter=new songAdapter(musicFilesArrayList,getContext());
        songAdapter.setHasStableIds(true);
        songAdapter.notifyDataSetChanged();
        songRecycler.setAdapter(songAdapter);

        return view1;
    }


}