package com.hiten_khatri.xmusic;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static com.hiten_khatri.xmusic.MainActivity.songAlbum;
import static com.hiten_khatri.xmusic.MainActivity.songArtist;

public class artistFrag extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    public static RecyclerView artistRecycler;
    View view;
    public String mParam1;
    public String mParam2;

    public artistFrag() {}

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
        view=inflater.inflate(R.layout.fragment_artist, container, false);

        artistRecycler=view.findViewById(R.id.artistRecycler);
        artistRecycler.setNestedScrollingEnabled(false);
        artistRecycler.setHasFixedSize(true);
        artistRecycler.setItemViewCacheSize(30);
        artistRecycler.setDrawingCacheEnabled(true);
        artistRecycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        artistRecycler.setLayoutManager(new GridLayoutManager(getContext(),2));

        ArtistAdapter artistAdapter=new ArtistAdapter(songArtist,getContext());
        artistAdapter.setHasStableIds(true);
        artistAdapter.notifyDataSetChanged();
        artistRecycler.setAdapter(artistAdapter);

        return view;
    }
}