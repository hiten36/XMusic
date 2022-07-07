package com.hiten_khatri.xmusic;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<MusicFiles> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private final TextView albumName;
        private final ImageView albumImg;

        @SuppressLint("CommitPrefEdits")
        public ViewHolder(View view) {
            super(view);

            albumName = view.findViewById(R.id.albumText);
            albumImg= view.findViewById(R.id.albumImg1);
        }

        public TextView getTextView() {
            return albumName;
        }
        public Context getContext()
        {
            return context;
        }
    }

    public AlbumAdapter(ArrayList<MusicFiles> dataSet,Context context) {
        localDataSet = dataSet;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.album_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position).getAlbum());

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(viewHolder.itemView.getContext(),AlbumDetails.class);
            intent.putExtra("albumName",localDataSet.get(position).getAlbum());
            intent.putExtra("albumPath",localDataSet.get(position).getPath());
            viewHolder.itemView.getContext().startActivity(intent);
        });

        if(position<localDataSet.size()) {
            byte[] image = getAlbumArt(localDataSet.get(position).getPath());
            if (image != null) {
                Glide.with(context).asBitmap().load(image).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).fitCenter().centerCrop().into(viewHolder.albumImg);
            } else {
                Glide.with(context).asBitmap().load(R.drawable.music1).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).fitCenter().centerCrop().into(viewHolder.albumImg);
            }
        }
    }

    @Override
    public long getItemId(int position) {
        return localDataSet.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
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
