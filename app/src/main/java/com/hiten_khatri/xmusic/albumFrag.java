package com.hiten_khatri.xmusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.hiten_khatri.xmusic.MainActivity.songAlbum;

public class albumFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView albumRecycler;
    public View view;

    public String mParam1;
    public String mParam2;

    public albumFrag() {}

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
        view = inflater.inflate(R.layout.fragment_album, container, false);
        albumRecycler=view.findViewById(R.id.albumRecycler);
        albumRecycler.setNestedScrollingEnabled(false);
        albumRecycler.setHasFixedSize(true);
        albumRecycler.setItemViewCacheSize(30);
        albumRecycler.setDrawingCacheEnabled(true);
        albumRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        albumRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        AlbumAdapter albumAdapter=new AlbumAdapter(songAlbum,getContext());
        albumAdapter.setHasStableIds(true);
        albumAdapter.notifyDataSetChanged();
        albumRecycler.setAdapter(albumAdapter);

        return view;
    }
}