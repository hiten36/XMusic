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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static com.hiten_khatri.xmusic.AlbumDetails.albumSongList;
import static com.hiten_khatri.xmusic.AlbumDetails.albumSongList1;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.album;
import static com.hiten_khatri.xmusic.PlaySong.artist;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.year;
import static com.hiten_khatri.xmusic.PlaySong.bitrate;
import static com.hiten_khatri.xmusic.PlaySong.duration;
import static com.hiten_khatri.xmusic.PlaySong.genre;
import static com.hiten_khatri.xmusic.PlaySong.title;
import static com.hiten_khatri.xmusic.PlaySong.author;
import static com.hiten_khatri.xmusic.PlaySong.timernum;

public class AlbumDetailsCus extends ArrayAdapter<ArrayList<String>> {

    public TextView textView;
    public ArrayList<String> arr;
    public Context context;
    public ImageView imageView;
    public static ArrayList<Integer> queueList1=new ArrayList<>();
    public static String playNext1="";
    public Gson gson;

    public AlbumDetailsCus(@NonNull Context context, int resource, ArrayList arr) {
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

        gson=new Gson();
        
        convertView.setOnClickListener(v -> {
            Intent intent1 =new Intent(getContext(),PlaySong.class);
            intent1.putExtra("position",position);
            intent1.putExtra("songList","albumDetails");
            intent1.putExtra("currentSong",albumSongList1.get(position).getTitle());
            getContext().startActivity(intent1);

        });

        imageView.setOnClickListener(v2 -> {
            PopupMenu popupMenu=new PopupMenu(getContext(),v2);
            popupMenu.getMenuInflater().inflate(R.menu.menu11,popupMenu.getMenu());
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                int ids=item.getItemId();
                switch (ids) {
                    case R.id.menu1:

                        if(musicService!=null)
                        {
                            int sessionId=musicService.getAudioSessionId();
                            Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                            intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                            ((Activity)getContext()).startActivityForResult(intent12,0);
                            intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                        }
                        else if(mediaPlayer!=null)
                        {
                            int sessionId=mediaPlayer.getAudioSessionId();
                            Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                            intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                            ((Activity)getContext()).startActivityForResult(intent12,0);
                            intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Start a song first!", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.menu8:

                        queueList1.add(albumSongList.indexOf(albumSongList1.get(position).getTitle()));
                        Toast.makeText(getContext(), "Song added to queue", Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.menu4:

                        LayoutInflater inflater1 = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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

                        String min=String.valueOf(Integer.parseInt(String.valueOf(albumSongList1.get(position).getDuration()).substring(0,3))/60);
                        String sec=String.valueOf(Integer.parseInt(String.valueOf(albumSongList1.get(position).getDuration()).substring(0,3))%60);
                        duration.setText(min+":"+sec);
                        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                        metaRetriver.setDataSource(albumSongList1.get(position).getPath());

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

                        String path=albumSongList1.get(position).getPath();
                        Uri uri=Uri.parse(path);
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("audio/*");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                        getContext().startActivity(Intent.createChooser(sharingIntent, "Share via"));

                        return true;
                    case R.id.menu9:

                        setPlayNext(String.valueOf(position));
                        Toast.makeText(getContext(), "Song Added!", Toast.LENGTH_SHORT).show();

                        return true;
                    case R.id.menu10:

                        SharedPreferences sharedPreferences = getContext().getSharedPreferences("playLists", Context.MODE_PRIVATE);
                        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = sharedPreferences.edit();
                        String playlists = sharedPreferences.getString("playLists", null);

                        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
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
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList);
                            listView.setAdapter(arrayAdapter);
                        } else {
                            ArrayList<String> pl1 = gson.fromJson(playlists, new TypeToken<ArrayList<String>>() {
                            }.getType());
                            ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, pl1);
                            listView.setAdapter(arrayAdapter);

                            listView.setOnItemClickListener((parent1, view, position2, id) -> {

                                SharedPreferences sharedPreferences1=getContext().getSharedPreferences(pl1.get(position2),MODE_PRIVATE);
                                @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor1=sharedPreferences1.edit();
                                String ifsong=sharedPreferences1.getString(pl1.get(position2),null);
                                if(ifsong!=null)
                                {
                                    ArrayList<String> pl_1 = gson.fromJson(ifsong, new TypeToken<ArrayList<String>>() {
                                    }.getType());
                                    if(!pl_1.contains(albumSongList1.get(position).getTitle())) {
                                        pl_1.add(albumSongList1.get(position).getTitle());
                                        String temp_pl = gson.toJson(pl_1);
                                        editor1.putString(pl1.get(position2), temp_pl);
                                        editor1.apply();
                                        Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        Toast.makeText(getContext(), "Song already exists in playlist", Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    ArrayList<String> pl_2=new ArrayList<>();
                                    pl_2.add(albumSongList1.get(position).getTitle());
                                    String temp_pl1=gson.toJson(pl_2);
                                    editor1.putString(pl1.get(position2),temp_pl1);
                                    editor1.apply();
                                    Toast.makeText(getContext(), "Added to playlist", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        imageView.setOnClickListener(v -> {
                            RelativeLayout relativeLayout=pview.findViewById(R.id.rel_add);
                            relativeLayout.setVisibility(View.VISIBLE);
                            EditText editText = pview.findViewById(R.id.playLists_name);
                            Button create = pview.findViewById(R.id.create_btn);

                            create.setOnClickListener(v1 -> {

                                if(playlists!=null)
                                {
                                    String editVal = editText.getText().toString();
                                    ArrayList<String> pl2 = gson.fromJson(playlists, new TypeToken<ArrayList<String>>() {
                                    }.getType());
                                    pl2.add(editVal);
                                    String pl3 = gson.toJson(pl2);
                                    editor.putString("playLists", pl3);
                                    editor.apply();
                                    Toast.makeText(getContext(), "Playlist has been successfully created!", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }
                                else
                                {
                                    String editVal = editText.getText().toString();
                                    ArrayList<String> pl3 =new ArrayList<>();
                                    pl3.add(editVal);
                                    String pl4 = gson.toJson(pl3);
                                    editor.putString("playLists", pl4);
                                    editor.apply();
                                    Toast.makeText(getContext(), "Playlist has been successfully created!", Toast.LENGTH_SHORT).show();
                                    popupWindow.dismiss();
                                }

                                relativeLayout.setVisibility(View.GONE);
                            });
                        });
                        return true;
                    case R.id.menu13:

                        Intent intent=new Intent(getContext(),PlayList_.class);
                        getContext().startActivity(intent);

                        return true;
                    case R.id.menu5:

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
                            popupWindow2.showAtLocation(v2, Gravity.CENTER, 0, 0);
                        }
                        else if(mediaPlayer!=null) {
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
                            popupWindow2.showAtLocation(v2, Gravity.CENTER, 0, 0);
                        }
                        else
                        {
                            Toast.makeText(getContext(), "Play a song first", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.menu11:

                        Intent intent1=new Intent(getContext(),FavActivity.class);
                        getContext().startActivity(intent1);

                        return true;
                }
                return true;
            });
        });
        
        return convertView;
    }

    public void setPlayNext(String s2)
    {
        playNext1=s2;
    }
}