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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static com.hiten_khatri.xmusic.FavActivity.favFileList;
import static com.hiten_khatri.xmusic.FavActivity.favList3;
import static com.hiten_khatri.xmusic.FavActivity.gson;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.album;
import static com.hiten_khatri.xmusic.PlaySong.artist;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.timernum;
import static com.hiten_khatri.xmusic.PlaySong.year;
import static com.hiten_khatri.xmusic.PlaySong.bitrate;
import static com.hiten_khatri.xmusic.PlaySong.duration;
import static com.hiten_khatri.xmusic.PlaySong.genre;
import static com.hiten_khatri.xmusic.PlaySong.title;
import static com.hiten_khatri.xmusic.PlaySong.author;

public class recCusAda extends ArrayAdapter<ArrayList<String>> {

    public TextView textView;
    public ArrayList<String> arr;
    public Context context;
    public ImageView imageView;
    public ArrayList<String> favList1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public static ArrayList<Integer> queueList3=new ArrayList<>();
    public static  String playNext3="";

    public recCusAda(@NonNull Context context, int resource, ArrayList arr) {
        super(context, resource,arr);
        this.arr=arr;
        this.context=context;
    }

    @SuppressLint({"ViewHolder", "CommitPrefEdits", "SetTextI18n", "NonConstantResourceId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView= LayoutInflater.from(getContext()).inflate(R.layout.rec_cus,parent,false);
        textView=convertView.findViewById(R.id.textView1);
        imageView=convertView.findViewById(R.id.imageView8);
        textView.setText(arr.get(position));

        sharedPreferences=getContext().getSharedPreferences("favList",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        String jsonText=sharedPreferences.getString("favList",null);
        if(jsonText!=null && !jsonText.equals("[]")) {
            convertView.setOnClickListener(v -> {
                Intent intent = new Intent(getContext(), PlaySong.class);
                intent.putExtra("position", position);
                intent.putExtra("songList", "favFileList");
                intent.putExtra("currentSong", favList3.get(position));
                intent.putExtra("recFlag", "true");
                getContext().startActivity(intent);

            });

            favList1 = gson.fromJson(jsonText, new TypeToken<ArrayList<String>>() {
            }.getType());
            imageView.setOnClickListener(v -> {
                PopupMenu popupMenu = new PopupMenu(getContext(), v);
                popupMenu.getMenuInflater().inflate(R.menu.menu2, popupMenu.getMenu());
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(item -> {

                    int id = item.getItemId();
                    switch (id) {
                        case R.id.menu1:
                            removeFav(position);
                            notifyDataSetChanged();

                            return true;
                        case R.id.menu2:
                            String path = favFileList.get(position).getPath();
                            Uri uri4 = Uri.parse(path);
                            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                            sharingIntent.setType("audio/*");
                            sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                            sharingIntent.putExtra(Intent.EXTRA_STREAM, uri4);
                            getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

                            return true;
                        case R.id.menu3:
                            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup1, null);

                            int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                            View pview = popupWindow.getContentView();
                            album = pview.findViewById(R.id.text1);
                            artist = pview.findViewById(R.id.artist);
                            genre = pview.findViewById(R.id.genre);
                            duration = pview.findViewById(R.id.duration);
                            title = pview.findViewById(R.id.title);
                            author = pview.findViewById(R.id.author);
                            bitrate = pview.findViewById(R.id.bitrate);
                            year = pview.findViewById(R.id.year);
                            popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                            popupWindow.setAnimationStyle(R.style.Theme_XMusic);

                            String min = String.valueOf(Integer.parseInt(String.valueOf(favFileList.get(position).getDuration()).substring(0, 3)) / 60);
                            String sec = String.valueOf(Integer.parseInt(String.valueOf(favFileList.get(position).getDuration()).substring(0, 3)) % 60);
                            duration.setText(min + ":" + sec);

                            MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                            metaRetriver.setDataSource(favFileList.get(position).getPath());

                            try {
                                album.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                                artist.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                                genre.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                                title.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
                                author.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
                                bitrate.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE).substring(0, 3) + " Kbps");
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
                        case R.id.menu4:

                            queueList3.add(favList3.indexOf(favFileList.get(position).getTitle()));
                            Toast.makeText(getContext(), "Song added to queue", Toast.LENGTH_SHORT).show();

                            return true;
                        case R.id.menu5:

                            setPlayNext(String.valueOf(favList3.indexOf(favFileList.get(position).getTitle())));
                            Toast.makeText(getContext(), "Song Added!", Toast.LENGTH_SHORT).show();

                            return true;
                        case R.id.menu6:

                            if(musicService!=null)
                            {
                                int sessionId=musicService.getAudioSessionId();
                                Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                                intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                                ((Activity) getContext()).startActivityForResult(intent12,0);
                                intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                            }
                            else if(mediaPlayer!=null)
                            {
                                int sessionId=mediaPlayer.getAudioSessionId();
                                Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                                intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                                ((Activity) getContext()).startActivityForResult(intent12,0);
                                intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Start a song first!", Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        case R.id.menu7:

                            if(musicService!=null) {
                                LayoutInflater inflater2 = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                                        Toast.makeText(getContext(), "Enter time first!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                                popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                                popupWindow2.showAtLocation(v, Gravity.CENTER, 0, 0);
                            }
                            else if(mediaPlayer!=null)
                            {
                                LayoutInflater inflater2 = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                                        Toast.makeText(getContext(), "Enter time first!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                                timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                                popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                                popupWindow2.showAtLocation(v, Gravity.CENTER, 0, 0);
                            }
                            else
                            {
                                Toast.makeText(getContext(), "Play a song first", Toast.LENGTH_SHORT).show();
                            }

                            return true;
                        case R.id.menu8:

                            System.exit(0);

                            return true;
                    }

                    return true;
                });
            });
        }
        else
        {
            convertView.setOnClickListener(v -> Toast.makeText(getContext(), "Add a song first!", Toast.LENGTH_SHORT).show());
            imageView.setOnClickListener(v -> {});
        }
        return convertView;
    }

    public void removeFav(int position)
    {
        Toast.makeText(getContext(), "Removed from favorites", Toast.LENGTH_SHORT).show();
        favList1.remove(position);
        String favList21 = gson.toJson(favList1);
        editor.putString("favList", favList21);
        editor.apply();
        arr.clear();
        arr.addAll(favList1);
    }

    public void setPlayNext(String s2)
    {
        playNext3=s2;
    }
}