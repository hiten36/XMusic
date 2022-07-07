package com.hiten_khatri.xmusic;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaMetadataRetriever;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.hiten_khatri.xmusic.MainActivity.posi;
import static com.hiten_khatri.xmusic.MainActivity.songList;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.album;
import static com.hiten_khatri.xmusic.PlaySong.artist;
import static com.hiten_khatri.xmusic.PlaySong.author;
import static com.hiten_khatri.xmusic.PlaySong.genre;
import static com.hiten_khatri.xmusic.PlaySong.duration;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.timernum;
import static com.hiten_khatri.xmusic.PlaySong.title;
import static com.hiten_khatri.xmusic.PlaySong.bitrate;
import static com.hiten_khatri.xmusic.PlaySong.year;

public class songAdapter extends RecyclerView.Adapter<songAdapter.ViewHolder> {

    private final Context context;
    public ArrayList<MusicFiles> localDataSet;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private Context context;
        private final TextView songName;
        private final ImageView songImg;
        private final ImageView more;
        public static ArrayList<Integer> queueList;
        public static String playNext="";
        public Gson gson;

        @SuppressLint("CommitPrefEdits")
        public ViewHolder(View view) {
            super(view);

            songName = view.findViewById(R.id.songName);
            songImg= view.findViewById(R.id.songImg);
            more= view.findViewById(R.id.menu_main);
            queueList=new ArrayList<>();
            gson=new Gson();
        }

        public TextView getTextView() {
            return songName;
        }
        public Context getContext()
        {
            return context;
        }
        public ImageView getImageView1(){
            return more;
        }
        public ArrayList getQueueList(){
            return queueList;
        }
        public void setPlayNext(String s2)
        {
            playNext=s2;
        }
        public Gson getGson()
        {
            return gson;
        }
    }

    public songAdapter(ArrayList<MusicFiles> dataSet,Context context) {
        localDataSet = dataSet;
        this.context=context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.song_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"NonConstantResourceId", "SetTextI18n"})
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.getTextView().setText(localDataSet.get(position).getTitle());

        byte[] image = getAlbumArt(localDataSet.get(position).getPath());
        if (image != null) {
            Glide.with(viewHolder.itemView.getContext()).asBitmap().load(image).dontAnimate().dontTransform().diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(viewHolder.songImg);
        } else {
            Glide.with(viewHolder.itemView.getContext()).asBitmap().load(R.drawable.music1).diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(true).dontAnimate().dontTransform().centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(viewHolder.songImg);
        }

        viewHolder.itemView.setOnClickListener(v -> {
            int pos=songList.indexOf(localDataSet.get(position).getTitle());
            Intent intent=new Intent(viewHolder.itemView.getContext(),PlaySong.class);
            intent.putExtra("position",pos);
            intent.putExtra("songList","songAdap");
            intent.putExtra("currentSong",localDataSet.get(position).getTitle());
            viewHolder.itemView.getContext().startActivity(intent);
        });

        viewHolder.getImageView1().setOnClickListener(v2 -> {
            PopupMenu popupMenu=new PopupMenu(viewHolder.itemView.getContext(),v2);
            popupMenu.getMenuInflater().inflate(R.menu.menu11,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                int ids=item.getItemId();
                switch (ids) {
                    case R.id.menu15:

                        Gson gson=new Gson();
                        SharedPreferences sharedPreferences2=viewHolder.itemView.getContext().getSharedPreferences("favList",MODE_PRIVATE);
                        SharedPreferences.Editor editor2=sharedPreferences2.edit();
                        String jsonText=sharedPreferences2.getString("favList",null);
                        ArrayList <String>favList1=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());
                        if(jsonText!=null) {
                            jsonText=sharedPreferences2.getString("favList",null);
                            ArrayList <String>favList3=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());
                            if (favList3.contains(localDataSet.get(position).getTitle())) {
                                Toast.makeText(viewHolder.itemView.getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
                                favList1.remove(localDataSet.get(position).getTitle());
                                String favList21 = gson.toJson(favList1);
                                editor2.putString("favList", favList21);
                            }
                            else
                            {
                                Toast.makeText(viewHolder.itemView.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                                favList1.add(localDataSet.get(position).getTitle());
                                String favList2 = gson.toJson(favList1);
                                editor2.putString("favList", favList2);
                            }
                            editor2.apply();
                            return true;
                        }
                        Toast.makeText(viewHolder.itemView.getContext(), "Added to favorites", Toast.LENGTH_SHORT).show();
                        ArrayList<String> temp=new ArrayList<>();
                        temp.add(localDataSet.get(position).getTitle());
                        String favList2 = gson.toJson(temp);
                        editor2.putString("favList", favList2);
                        editor2.apply();

                        return  true;
                    case R.id.menu1:

                        if(musicService!=null)
                        {
                            int sessionId=musicService.getAudioSessionId();
                            Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                            intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                            ((Activity)viewHolder.itemView.getContext()).startActivityForResult(intent12,0);
                            intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                        }
                        else if(mediaPlayer!=null)
                        {
                            int sessionId=mediaPlayer.getAudioSessionId();
                            Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                            intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                            ((Activity)viewHolder.itemView.getContext()).startActivityForResult(intent12,0);
                            intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                        }
                        else
                        {
                            Toast.makeText(viewHolder.itemView.getContext(), "Start a song first!", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.menu8:

                        viewHolder.getQueueList().add(songList.indexOf(localDataSet.get(position).getTitle()));
                        Toast.makeText(viewHolder.itemView.getContext(), "Song added to queue", Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.menu4:

                        LayoutInflater inflater1 = (LayoutInflater) viewHolder.itemView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        @SuppressLint("InflateParams") View popupView1 = inflater1.inflate(R.layout.popup1, null);

                        int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                        final PopupWindow popupWindow1 = new PopupWindow(popupView1, width1, height1, true);
                        View pview2=popupWindow1.getContentView();
                        album=pview2.findViewById(R.id.text1);
                        artist=pview2.findViewById(R.id.artist);
                        genre=pview2.findViewById(R.id.genre);
                        duration=pview2.findViewById(R.id.duration);
                        title=pview2.findViewById(R.id.title);
                        author=pview2.findViewById(R.id.author);
                        bitrate=pview2.findViewById(R.id.bitrate);
                        year=pview2.findViewById(R.id.year);
                        popupWindow1.setAnimationStyle(R.style.Theme_XMusic);
                        popupWindow1.showAtLocation(v2, Gravity.CENTER, 0, 0);

                        String min=String.valueOf(Integer.parseInt(String.valueOf(localDataSet.get(position).getDuration()).substring(0,3))/60);
                        String sec=String.valueOf(Integer.parseInt(String.valueOf(localDataSet.get(position).getDuration()).substring(0,3))%60);
                        duration.setText(min+":"+sec);
                        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                        metaRetriver.setDataSource(localDataSet.get(position).getPath());

                        try {
                            album.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                            artist.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            genre.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                            title.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                            author.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                            author.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                            bitrate.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).substring(0,3)+" Kbps");
                            year.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
                        } catch (Exception e) {
                            album.setText("Unknown Album");
                            artist.setText("Unknown Artist");
                            genre.setText("Unknown Genre");
                            duration.setText("Unknown Duration");
                            title.setText("Unknown Title");
                            author.setText("Unknown Author");
                            author.setText("Unknown Bit Rate");
                            author.setText("Unknown Year");
                        }

                        return true;
                    case R.id.menu3:

                        String path=localDataSet.get(position).getPath();
                        Uri uri=Uri.parse(path);
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("audio/*");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        viewHolder.itemView.getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

                        return true;
                    case R.id.menu9:

                        viewHolder.setPlayNext(String.valueOf(songList.indexOf(localDataSet.get(position).getTitle())));
                        Toast.makeText(viewHolder.itemView.getContext(), "Song Added!", Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.menu10:

                        SharedPreferences sharedPreferences = viewHolder.itemView.getContext().getSharedPreferences("playLists", Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                        String playlists = sharedPreferences.getString("playLists", null);

                        LayoutInflater inflater = (LayoutInflater) viewHolder.itemView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.playlists_pop, null);

                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                        View pview = popupWindow.getContentView();

                        ListView listView = pview.findViewById(R.id.playLists1);
                        ImageView imageView = pview.findViewById(R.id.addPlayListbtn);

                        popupWindow.setAnimationStyle(R.style.Theme_XMusic);
                        popupWindow.showAtLocation(v2, Gravity.CENTER, 0, 0);

                        if (playlists == null || playlists.equals("[]")) {
                            ArrayList<String> arrayList = new ArrayList<>();
                            arrayList.add("No Playlist added,Create One!");
                            ArrayAdapter arrayAdapter = new ArrayAdapter(viewHolder.itemView.getContext(), android.R.layout.simple_list_item_1, arrayList);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            ArrayList<String> pl1 = viewHolder.getGson().fromJson(playlists, new TypeToken<ArrayList<String>>() {
                            }.getType());
                            ArrayAdapter arrayAdapter = new ArrayAdapter(viewHolder.itemView.getContext(), android.R.layout.simple_list_item_1, pl1);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener((parent, view, position2, id) -> {

                                SharedPreferences sharedPreferences1=viewHolder.itemView.getContext().getSharedPreferences(pl1.get(position2),MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1=sharedPreferences1.edit();
                                String ifsong=sharedPreferences1.getString(pl1.get(position2),null);
                                if(ifsong!=null)
                                {
                                    ArrayList<String> pl_1 = viewHolder.getGson().fromJson(ifsong, new TypeToken<ArrayList<String>>() {
                                    }.getType());
                                    if(!pl_1.contains(localDataSet.get(position).getTitle())) {
                                        pl_1.add(localDataSet.get(position).getTitle());
                                        String temp_pl = viewHolder.getGson().toJson(pl_1);
                                        editor1.putString(pl1.get(position2), temp_pl);
                                        editor1.apply();
                                        Toast.makeText(viewHolder.itemView.getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(viewHolder.itemView.getContext(), "Song already exists in playlist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    ArrayList<String> pl_2=new ArrayList<>();
                                    pl_2.add(localDataSet.get(position).getTitle());
                                    String temp_pl1=viewHolder.getGson().toJson(pl_2);
                                    editor1.putString(pl1.get(position2),temp_pl1);
                                    editor1.apply();
                                    Toast.makeText(viewHolder.itemView.getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        imageView.setOnClickListener(v -> {
                            popupWindow.dismiss();

                            LayoutInflater inflater2 = (LayoutInflater) viewHolder.itemView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") View popupView2= inflater2.inflate(R.layout.playlist_pop1, null);

                            int width2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow2 = new PopupWindow(popupView2, width2, height2, true);
                            View pview3 = popupWindow2.getContentView();

                            EditText editText = pview3.findViewById(R.id.playLists_name);
                            Button create = pview3.findViewById(R.id.create_btn);

                            popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                            popupWindow2.showAtLocation(v2, Gravity.CENTER, 0, 0);

                            create.setOnClickListener(v1 -> {

                                if(playlists!=null)
                                {
                                    String editVal = editText.getText().toString();
                                    ArrayList<String> pl2 = viewHolder.getGson().fromJson(playlists, new TypeToken<ArrayList<String>>() {
                                    }.getType());
                                    pl2.add(editVal);
                                    String pl3 = viewHolder.getGson().toJson(pl2);
                                    editor.putString("playLists", pl3);
                                    editor.apply();
                                    Toast.makeText(viewHolder.itemView.getContext(), "Playlist has been successfully created!", Toast.LENGTH_SHORT).show();
                                    popupWindow2.dismiss();
                                }
                                else
                                {
                                    String editVal = editText.getText().toString();
                                    ArrayList<String> pl3 =new ArrayList<>();
                                    pl3.add(editVal);
                                    String pl4 = viewHolder.getGson().toJson(pl3);
                                    editor.putString("playLists", pl4);
                                    editor.apply();
                                    Toast.makeText(viewHolder.itemView.getContext(), "Playlist has been successfully created!", Toast.LENGTH_SHORT).show();
                                    popupWindow2.dismiss();
                                }

                            });
                        });
                    return true;
                    case R.id.menu13:

                        Intent intent=new Intent(viewHolder.itemView.getContext(),PlayList_.class);
                        viewHolder.itemView.getContext().startActivity(intent);

                        return true;
                    case R.id.menu5:

                        if(musicService!=null) {
                            LayoutInflater inflater2 = (LayoutInflater) viewHolder.itemView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") View popupView2 = inflater2.inflate(R.layout.timer_popup, null);

                            int width2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow2 = new PopupWindow(popupView2, width2, height2, true);
                            View pview3 = popupWindow2.getContentView();

                            timer_ok = pview3.findViewById(R.id.timer_ok);
                            timer_cancel = pview3.findViewById(R.id.timer_cancel);
                            timernum = pview3.findViewById(R.id.timer_num);

                            timer_ok.setOnClickListener(v1 -> {
                                String timer_val1 = timernum.getText().toString();
                                if(!timer_val1.equals("")) {
                                    int timer_val = Integer.parseInt(timernum.getText().toString());
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            musicService.stop();
                                            musicService.release();
                                        }
                                    }, timer_val * 1000);
                                    popupWindow2.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(viewHolder.itemView.getContext(), "Enter time first!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                            popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                            popupWindow2.showAtLocation(v2, Gravity.CENTER, 0, 0);
                        }
                        else if(mediaPlayer!=null)
                        {
                            LayoutInflater inflater2 = (LayoutInflater) viewHolder.itemView.getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") View popupView2 = inflater2.inflate(R.layout.timer_popup, null);

                            int width2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height2 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow2 = new PopupWindow(popupView2, width2, height2, true);
                            View pview3 = popupWindow2.getContentView();

                            timer_ok = pview3.findViewById(R.id.timer_ok);
                            timer_cancel = pview3.findViewById(R.id.timer_cancel);
                            timernum = pview3.findViewById(R.id.timer_num);

                            timer_ok.setOnClickListener(v1 -> {
                                String timer_val1 = timernum.getText().toString();
                                if(!timer_val1.equals("")) {
                                    int timer_val = Integer.parseInt(timernum.getText().toString());
                                    new Timer().schedule(new TimerTask() {
                                        @Override
                                        public void run() {
                                            mediaPlayer.stop();
                                            mediaPlayer.release();
                                        }
                                    }, timer_val * 1000);
                                    popupWindow2.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(viewHolder.itemView.getContext(), "Enter time first!", Toast.LENGTH_SHORT).show();
                                }
                            });
                            timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                            popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                            popupWindow2.showAtLocation(v2, Gravity.CENTER, 0, 0);
                        }
                        else
                        {
                            Toast.makeText(viewHolder.itemView.getContext(), "Play a song first", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.menu11:

                        Intent intent1=new Intent(viewHolder.itemView.getContext(),FavActivity.class);
                        viewHolder.itemView.getContext().startActivity(intent1);

                        return true;
                    case R.id.menu7:

                        ((Activity) (viewHolder.itemView.getContext())).finish();
                        System.exit(0);

                        return true;
                }
                return true;
            });
        });
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

    public void updateList(ArrayList<MusicFiles> mf)
    {
        localDataSet=new ArrayList<>();
        localDataSet.addAll(mf);
        notifyDataSetChanged();
    }

}
