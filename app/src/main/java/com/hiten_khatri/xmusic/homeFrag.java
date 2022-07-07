package com.hiten_khatri.xmusic;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;
import static com.hiten_khatri.xmusic.MainActivity.songList;

public class homeFrag extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    View view;
    private String mParam1;
    private String mParam2;
    ImageView imageView10;
    ImageView imageView11;
    ImageView imageView12;
    ImageView imageView13;
    TextView textView;
    TextView textView22;
    TextView textView23;
    TextView textView24;
    ImageView imageView14;
    ImageView imageView15;
    ImageView imageView16;
    ImageView imageView17;
    ImageView imageView18;
    ImageView imageView19;
    ImageView imageView20;
    ImageView imageView21;
    ImageView imageView22;

    public homeFrag() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);

        imageView10=view.findViewById(R.id.imageView10);
        imageView11=view.findViewById(R.id.imageView11);
        imageView12=view.findViewById(R.id.imageView12);
        imageView13=view.findViewById(R.id.imageView13);
        imageView14=view.findViewById(R.id.imageView14);
        imageView15=view.findViewById(R.id.imageView15);
        imageView16=view.findViewById(R.id.imageView16);
        imageView17=view.findViewById(R.id.imageView17);
        imageView18=view.findViewById(R.id.imageView18);
        imageView19=view.findViewById(R.id.imageView19);
        imageView20=view.findViewById(R.id.imageView20);
        imageView21=view.findViewById(R.id.imageView21);
        imageView22=view.findViewById(R.id.imageView22);
        textView=view.findViewById(R.id.textView);
        textView22=view.findViewById(R.id.textView22);
        textView23=view.findViewById(R.id.textView23);
        textView24=view.findViewById(R.id.textView24);

        Random random=new Random();

        ArrayList<Integer> arrayList=new ArrayList<>();
        for(int i=0;i<9;i++)
        {
            arrayList.add(random.nextInt(musicFilesArrayList.size()));
        }

        byte[] image = getAlbumArt(musicFilesArrayList.get(arrayList.get(0)).getPath());
        if (image != null) {
            Glide.with(getContext()).asBitmap().load(image).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView14);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView14);
        }
        byte[] image1 = getAlbumArt(musicFilesArrayList.get(arrayList.get(1)).getPath());
        if (image1 != null) {
            Glide.with(getContext()).asBitmap().load(image1).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView15);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView15);
        }
        byte[] image2 = getAlbumArt(musicFilesArrayList.get(arrayList.get(2)).getPath());
        if (image2 != null) {
            Glide.with(getContext()).asBitmap().load(image2).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView16);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView16);
        }
        byte[] image3 = getAlbumArt(musicFilesArrayList.get(arrayList.get(3)).getPath());
        if (image3 != null) {
            Glide.with(getContext()).asBitmap().load(image).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView17);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView17);
        }
        byte[] image4 = getAlbumArt(musicFilesArrayList.get(arrayList.get(4)).getPath());
        if (image4 != null) {
            Glide.with(getContext()).asBitmap().load(image4).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView18);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView18);
        }
        byte[] image5 = getAlbumArt(musicFilesArrayList.get(arrayList.get(5)).getPath());
        if (image5 != null) {
            Glide.with(getContext()).asBitmap().load(image5).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView19);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView19);
        }
        byte[] image6 = getAlbumArt(musicFilesArrayList.get(arrayList.get(6)).getPath());
        if (image6 != null) {
            Glide.with(getContext()).asBitmap().load(image6).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView20);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView20);
        }
        byte[] image7 = getAlbumArt(musicFilesArrayList.get(arrayList.get(7)).getPath());
        if (image7 != null) {
            Glide.with(getContext()).asBitmap().load(image7).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView21);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView21);
        }
        byte[] image8 = getAlbumArt(musicFilesArrayList.get(arrayList.get(8)).getPath());
        if (image8 != null) {
            Glide.with(getContext()).asBitmap().load(image8).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView22);
        } else {
            Glide.with(getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().thumbnail(0.02f).apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(imageView22);
        }

        imageView14.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(0)));
            intent.putExtra("position",arrayList.get(0));
            startActivity(intent);
        });
        imageView15.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(1)));
            intent.putExtra("position",arrayList.get(1));
            startActivity(intent);
        });
        imageView16.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(2)));
            intent.putExtra("position",arrayList.get(2));
            startActivity(intent);
        });
        imageView17.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(3)));
            intent.putExtra("position",arrayList.get(3));
            startActivity(intent);
        });
        imageView18.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(4)));
            intent.putExtra("position",arrayList.get(4));
            startActivity(intent);
        });
        imageView19.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(5)));
            intent.putExtra("position",arrayList.get(5));
            startActivity(intent);
        });
        imageView20.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(6)));
            intent.putExtra("position",arrayList.get(6));
            startActivity(intent);
        });
        imageView21.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(7)));
            intent.putExtra("position",arrayList.get(7));
            startActivity(intent);
        });
        imageView22.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",songList.get(arrayList.get(8)));
            intent.putExtra("position",arrayList.get(8));
            startActivity(intent);
        });

        imageView10.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlayList_.class);
            startActivity(intent);
        });
        textView.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlayList_.class);
            startActivity(intent);
        });
        imageView11.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),FavActivity.class);
            startActivity(intent);
        });
        textView22.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),FavActivity.class);
            startActivity(intent);
        });
        imageView12.setOnClickListener(v -> {
            Random random1 = new Random();
            int rpos = random1.nextInt(musicFilesArrayList.size());
            Intent intent1 = new Intent(getContext(), PlaySong.class);
            intent1.putExtra("position", rpos);
            intent1.putExtra("currentSong", songList.get(rpos));
            intent1.putExtra("songList", "songAdap");
            intent1.putExtra("shuffle_bool",true);
            startActivity(intent1);
        });
        textView23.setOnClickListener(v -> {
            Random random12 = new Random();
            int rpos = random12.nextInt(musicFilesArrayList.size());
            Intent intent1 = new Intent(getContext(), PlaySong.class);
            intent1.putExtra("position", rpos);
            intent1.putExtra("currentSong", songList.get(rpos));
            intent1.putExtra("songList", "songAdap");
            intent1.putExtra("shuffle_bool",true);
            startActivity(intent1);
        });
        imageView13.setOnClickListener(v -> {
            Intent intent1 = new Intent(getContext(), PlaySong.class);
            intent1.putExtra("position", 0);
            intent1.putExtra("currentSong", songList.get(0));
            intent1.putExtra("songList", "songAdap");
            startActivity(intent1);
        });
        textView24.setOnClickListener(v -> {
            Intent intent1 = new Intent(getContext(), PlaySong.class);
            intent1.putExtra("position", 0);
            intent1.putExtra("currentSong", songList.get(0));
            intent1.putExtra("songList", "songAdap");
            startActivity(intent1);
        });



        return view;
    }

    public byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        try {
            retriever.setDataSource(uri);
            byte[] art = retriever.getEmbeddedPicture();
            retriever.release();
            return art;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}