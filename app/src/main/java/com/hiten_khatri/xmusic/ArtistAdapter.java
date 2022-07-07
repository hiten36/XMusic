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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class ArtistAdapter extends RecyclerView.Adapter<ArtistAdapter.ViewHolder> {

    private final Context context;
    private final ArrayList<MusicFiles> localDataSet;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private final TextView artistName;
        private final ImageView artistImg;

        @SuppressLint("CommitPrefEdits")
        public ViewHolder(View view) {
            super(view);

            artistName = view.findViewById(R.id.artistText);
            artistImg= view.findViewById(R.id.artistImg1);
        }

        public TextView getTextView() {
            return artistName;
        }
        public Context getContext()
        {
            return context;
        }
    }

    public ArtistAdapter(ArrayList<MusicFiles> dataSet,Context context) {
        localDataSet = dataSet;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.artist_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position).getArtist());

        viewHolder.itemView.setOnClickListener(v -> {
            Intent intent=new Intent(viewHolder.itemView.getContext(),ArtistDetails.class);
            intent.putExtra("artistName",localDataSet.get(position).getArtist());
            intent.putExtra("artistPath",localDataSet.get(position).getPath());
            viewHolder.itemView.getContext().startActivity(intent);
        });

        if(position<localDataSet.size()) {
            byte[] image = getAlbumArt(localDataSet.get(position).getPath());
            if (image != null) {
                Glide.with(context).asBitmap().load(image).apply(RequestOptions.circleCropTransform()).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).into(viewHolder.artistImg);
            } else {
                Glide.with(context).asBitmap().load(R.drawable.music1).apply(RequestOptions.circleCropTransform()).dontAnimate().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.5f).into(viewHolder.artistImg);
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
