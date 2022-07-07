package com.hiten_khatri.xmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.gauravk.audiovisualizer.visualizer.CircleLineVisualizer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static com.hiten_khatri.xmusic.AlbumDetails.albumSongList1;
import static com.hiten_khatri.xmusic.AlbumDetailsCus.playNext1;
import static com.hiten_khatri.xmusic.AlbumDetailsCus.queueList1;
import static com.hiten_khatri.xmusic.ArtistDetails.artistSongList1;
import static com.hiten_khatri.xmusic.ArtistDetailsCus.playNext4;
import static com.hiten_khatri.xmusic.ArtistDetailsCus.queueList4;
import static com.hiten_khatri.xmusic.FavActivity.favFileList;
import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;
import static com.hiten_khatri.xmusic.MainActivity.posi;
import static com.hiten_khatri.xmusic.PlayListCus.playAll;
import static com.hiten_khatri.xmusic.PlayListCus.playAll1;
import static com.hiten_khatri.xmusic.PlayListSongListCus.playListFileList;
import static com.hiten_khatri.xmusic.PlayListSongListCus.playNext2;
import static com.hiten_khatri.xmusic.PlayListSongListCus.queueList2;
import static com.hiten_khatri.xmusic.recCusAda.playNext3;
import static com.hiten_khatri.xmusic.recCusAda.queueList3;
import static com.hiten_khatri.xmusic.songAdapter.ViewHolder.playNext;
import static com.hiten_khatri.xmusic.songAdapter.ViewHolder.queueList;

public class PlaySong extends AppCompatActivity implements ActionPlaying, ServiceConnection {


    SharedPreferences sharedPreferences3;
    SharedPreferences.Editor editor3;
    public boolean check=true;
    public static boolean flag1=false;
    public static boolean swipeDismiss;
    public int currentTime;
    public static String currentSongList;
    public static String jsonText;
    public static ArrayList<String> favList3;
    public static ArrayList<String> favList1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    public static ArrayList<MusicFiles> songs;
    public int rep_pos;
    public static boolean repeat_bool=false;
    public static boolean shuffle_bool=false;
    public static int getdur;
    public static int curpos;
    public static int position;
    public static int queuePos=0;
    private TextView textView;
    private ImageView pause;
    private SeekBar seekBar;
    private TextView ltimes;
    private TextView rtimes;
    private ImageView shuffle;
    private ImageView repeat;
    private CircleLineVisualizer circleLineVisualizer;
    private ImageView favorites;
    private ImageView albumImg;
    @SuppressLint("StaticFieldLeak")
    public static TextView album;
    @SuppressLint("StaticFieldLeak")
    public static TextView artist;
    @SuppressLint("StaticFieldLeak")
    public static TextView genre;
    @SuppressLint("StaticFieldLeak")
    public static TextView duration;
    @SuppressLint("StaticFieldLeak")
    public static TextView title;
    @SuppressLint("StaticFieldLeak")
    public static TextView author;
    @SuppressLint("StaticFieldLeak")
    public static TextView bitrate;
    @SuppressLint("StaticFieldLeak")
    public static TextView year;
    public LinearLayout timer_ask;
    @SuppressLint("StaticFieldLeak")
    public static Button timer_ok;
    @SuppressLint("StaticFieldLeak")
    public static Button timer_cancel;
    @SuppressLint("StaticFieldLeak")
    public static EditText timernum;
    public static MusicService musicService;

    @SuppressLint({"NonConstantResourceId", "SetTextI18n", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_play_song);

        overridePendingTransition(R.anim.bottom_up,R.anim.bottom_down);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("X Music");
        toolbar.setTitleTextColor(Color.parseColor("#FF8C00"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.RobotoBoldTextAppearance);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
        }

        textView=findViewById(R.id.textView2);
        ImageView previous = findViewById(R.id.previous);
        pause=findViewById(R.id.pause);
        ImageView next = findViewById(R.id.next);
        seekBar=findViewById(R.id.seekBar);
        rtimes=findViewById(R.id.rtimes);
        ltimes=findViewById(R.id.ltimes);
        shuffle=findViewById(R.id.shuffle);
        repeat=findViewById(R.id.repeat);
        circleLineVisualizer=findViewById(R.id.blast);
        favorites=findViewById(R.id.favorites);
        albumImg=findViewById(R.id.albumImg);
        ImageView more = findViewById(R.id.more);
        timer_ask=findViewById(R.id.timer_ask);
        sharedPreferences=getSharedPreferences("favList",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        favList1=new ArrayList<>();
        gson=new Gson();
        favList3=new ArrayList<>();

        Intent intent=getIntent();

        switch (intent.getStringExtra("songList")) {
            case "albumDetails":
                currentSongList="albumDetails";
                songs = albumSongList1;
                break;
            case "artistDetails":
                currentSongList="artistDetails";
                songs = artistSongList1;
                break;
            case "songAdap":
                currentSongList="songAdap";
                songs = musicFilesArrayList;
                break;
            case "favFileList":
                currentSongList="favFileList";
                songs = favFileList;
                break;
            case "playListFileList":
                currentSongList="playListFileList";
                songs=playListFileList;
                break;
            case "playAll":
                currentSongList="playAll";
                songs=playAll;
                break;
            case "playAll1":
                currentSongList="playAll1";
                songs=playAll1;
                break;
        }

        String textContent=intent.getStringExtra("currentSong");
        textView.setText(textContent);
        textView.setSelected(true);

        if(shuffle_bool)
        {
            shuffle.setImageResource(R.drawable.ic_shuffle_on);
        }
        else
        {
            shuffle.setImageResource(R.drawable.shuffle);
        }

        if(repeat_bool)
        {
            repeat.setImageResource(R.drawable.repeat_on);
        }
        else
        {
            repeat.setImageResource(R.drawable.repeat);
        }

        shuffle_bool=intent.getBooleanExtra("shuffle_bool",false);
        boolean sb=intent.getBooleanExtra("shuffle_bool",false);
        if(sb)
        {
            rep_pos=position;
        }
        position=intent.getIntExtra("position",0);
        currentTime=intent.getIntExtra("currentTime",0);

        Intent intent1=new Intent(this,MusicService.class);
        intent1.putExtra("servicePosition",position);
        startService(intent1);

        jsonText=sharedPreferences.getString("favList",null);
        favList3=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());
        favList1=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());

        if(jsonText!=null) {
            if (favList3.contains(songs.get(position).getTitle())) {
                favorites.setImageResource(R.drawable.favorite_fill);
            }
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if(fromUser)
                {
                    musicService.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                musicService.seekTo(seekBar.getProgress());
            }
        });

        Handler handler=new Handler();
        Timer timer = new Timer();
        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(() -> {
                    try {

                        musicService.OnCompleted();

                        if(musicService != null){
                            getdur=musicService.getDuration();
                            curpos=musicService.getCurrentPosition();
                            int mCurrentPosition=musicService.getCurrentPosition();
                            seekBar.setProgress(mCurrentPosition);
                            if (String.valueOf(mCurrentPosition).length() == 4) {
                                String ltime = String.valueOf(mCurrentPosition).substring(0, 1);
                                ltimes.setText("0:0" + ltime);
                            }
                            if (String.valueOf(mCurrentPosition).length() > 4) {
                                if (mCurrentPosition >= 60000) {
                                    int min = mCurrentPosition / 60000;
                                    int sec = mCurrentPosition % 60000;
                                    if (String.valueOf(sec).length() == 4) {
                                        String ltime = String.valueOf(sec).substring(0, 1);
                                        String ltime1 = String.valueOf(min);
                                        ltimes.setText(ltime1 + ":0" + ltime);
                                    }
                                    if (String.valueOf(sec).length() == 5) {
                                        String ltime1 = String.valueOf(min);
                                        String ltime = String.valueOf(sec).substring(0, 2);
                                        ltimes.setText(ltime1 + ":" + ltime);
                                    }
                                    if (String.valueOf(sec).length() < 4) {
                                        String ltime1 = String.valueOf(min);
                                        ltimes.setText(ltime1 + ":00");
                                    }
                                } else {
                                    String ltime = String.valueOf(mCurrentPosition).substring(0, 2);
                                    ltimes.setText("0:" + ltime);
                                }
                            }
                            int remainpos = musicService.getDuration() - musicService.getCurrentPosition();
                            if (String.valueOf(remainpos).length() == 4) {
                                String rtime = String.valueOf(remainpos).substring(0, 1);
                                rtimes.setText("0:0" + rtime);
                            }
                            if (String.valueOf(remainpos).length() > 4) {
                                if (remainpos >= 60000) {
                                    int min1 = remainpos / 60000;
                                    int sec1 = remainpos % 60000;
                                    if (String.valueOf(sec1).length() == 4) {
                                        String rtime = String.valueOf(sec1).substring(0, 1);
                                        String rtime1 = String.valueOf(min1);
                                        rtimes.setText(rtime1 + ":0" + rtime);
                                    }
                                    if (String.valueOf(sec1).length() == 5) {
                                        String rtime = String.valueOf(sec1).substring(0, 2);
                                        String rtime1 = String.valueOf(min1);
                                        rtimes.setText(rtime1 + ":" + rtime);
                                    }
                                    if (String.valueOf(sec1).length() < 4) {
                                        String ritme = String.valueOf(min1);
                                        rtimes.setText(ritme + ":00");
                                    }
                                } else {
                                    String rtime = String.valueOf(remainpos).substring(0, 2);
                                    rtimes.setText("0:" + rtime);
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 400);

        pause.setOnClickListener(v -> pauseFunc());

        previous.setOnClickListener(v -> prevFunc());

        next.setOnClickListener(v -> nextFunc());

        repeat.setOnClickListener(v -> {
            if(!repeat_bool) {
                if(shuffle_bool)
                {
                    shuffle_bool=false;
                    shuffle.setImageResource(R.drawable.shuffle);
                }
                else
                {
                    updatePosition(position);
                }

                repeat_bool = true;
                repeat.setImageResource(R.drawable.repeat_on);
            }
            else
            {
                repeat_bool = false;
                repeat.setImageResource(R.drawable.repeat);
            }
        });

        shuffle.setOnClickListener(v -> {

            if(!shuffle_bool) {
                if(repeat_bool)
                {
                    repeat_bool=false;
                    repeat.setImageResource(R.drawable.repeat);
                }
                shuffle_bool = true;
                shuffle.setImageResource(R.drawable.ic_shuffle_on);
                musicService.stop();
                musicService.release();
                Random random=new Random();
                int rpos=random.nextInt(songs.size());
                updatePosition(rpos);
                musicService.createMediaPlayer(rpos);
                musicService.start();
                int ids4=musicService.getAudioSessionId();
                circleLineVisualizer.setAudioSessionId(ids4);
                pause.setImageResource(R.drawable.pause);
                musicService.showNotification(R.drawable.pause,position);
                textView.setText(songs.get(rpos).getTitle());
                seekBar.setMax(musicService.getDuration());
                position=rpos;
                if(jsonText!=null) {
                    if (favList3.contains(songs.get(position).getTitle())) {
                        favorites.setImageResource(R.drawable.favorite_fill);
                    } else {
                        favorites.setImageResource(R.drawable.favorite);
                    }
                }

                byte[] image2 = musicService.getAlbumArt(songs.get(position).getPath());
                if (image2 != null) {
                    Glide.with(PlaySong.this).asBitmap().load(image2).apply(RequestOptions.circleCropTransform()).into(albumImg);
                } else {
                    Glide.with(PlaySong.this).asBitmap().load(R.drawable.music1).into(albumImg);
                }

            }
            else
            {
                shuffle_bool = false;
                shuffle.setImageResource(R.drawable.shuffle);
            }
        });

        favorites.setOnClickListener(v -> {

            if(jsonText!=null) {
                jsonText=sharedPreferences.getString("favList",null);
                favList3=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());
                if (favList3.contains(songs.get(position).getTitle())) {
                    Toast.makeText(PlaySong.this, "Removed from favorites", Toast.LENGTH_SHORT).show();
                    favorites.setImageResource(R.drawable.favorite);
                    favList1.remove(songs.get(position).getTitle());
                    String favList21 = gson.toJson(favList1);
                    editor.putString("favList", favList21);
                }
                else
                {
                    Toast.makeText(PlaySong.this, "Added to favorites", Toast.LENGTH_SHORT).show();
                    favorites.setImageResource(R.drawable.favorite_fill);
                    favList1.add(songs.get(position).getTitle());
                    String favList2 = gson.toJson(favList1);
                    editor.putString("favList", favList2);
                }
                editor.apply();
                return;
            }
            Toast.makeText(PlaySong.this, "Added to favorites", Toast.LENGTH_SHORT).show();
            favorites.setImageResource(R.drawable.favorite_fill);
            ArrayList<String> temp=new ArrayList<>();
            temp.add(songs.get(position).getTitle());
            String favList2 = gson.toJson(temp);
            editor.putString("favList", favList2);
            editor.apply();
            return;
        });

        more.setOnClickListener(v -> {
            PopupMenu popupMenu=new PopupMenu(PlaySong.this,v);
            popupMenu.getMenuInflater().inflate(R.menu.menu,popupMenu.getMenu());
            MenuItem popupMenu1=popupMenu.getMenu().findItem(R.id.menu8);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            editor3=sharedPreferences3.edit();
            check=sharedPreferences3.getBoolean("visChecks",true);
            popupMenu1.setChecked(check);
            popupMenu.show();
            popupMenu.setOnMenuItemClickListener(item -> {
                int ids14 =item.getItemId();
                switch (ids14)
                {
                    case R.id.menu1:

                        int sessionId=musicService.getAudioSessionId();
                        Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                        intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                        startActivityForResult(intent12,0);
                        intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);

                        return true;
                    case R.id.menu8:

                        if(circleLineVisualizer!=null) {
                            if (check) {
                                editor3.putBoolean("visChecks", false);
                                editor3.apply();
                                circleLineVisualizer.setVisibility(View.GONE);
                            }
                            else
                            {
                                editor3.putBoolean("visChecks",true);
                                editor3.apply();
                                circleLineVisualizer.setVisibility(View.VISIBLE);
                            }
                        }

                        return true;
                    case R.id.menu4:

                        LayoutInflater inflater = (LayoutInflater)
                                getSystemService(LAYOUT_INFLATER_SERVICE);
                        @SuppressLint("InflateParams") View popupView = inflater.inflate(R.layout.popup1, null);

                        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
                        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, true);
                        View pview=popupWindow.getContentView();
                        album=pview.findViewById(R.id.text1);
                        artist=pview.findViewById(R.id.artist);
                        genre=pview.findViewById(R.id.genre);
                        duration=pview.findViewById(R.id.duration);
                        title=pview.findViewById(R.id.title);
                        author=pview.findViewById(R.id.author);
                        bitrate=pview.findViewById(R.id.bitrate);
                        year=pview.findViewById(R.id.year);
                        popupWindow.setAnimationStyle(R.style.Theme_XMusic);
                        popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);

                        String min=String.valueOf(Integer.parseInt(String.valueOf(songs.get(position).getDuration()).substring(0,3))/60);
                        String sec=String.valueOf(Integer.parseInt(String.valueOf(songs.get(position).getDuration()).substring(0,3))%60);
                        duration.setText(min+":"+sec);

                        MediaMetadataRetriever metaRetriver = new MediaMetadataRetriever();
                        metaRetriver.setDataSource(songs.get(position).getPath());

                        try {
                            album.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
                            artist.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
                            genre.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
                            title.setText(metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
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

                        String path=songs.get(position).getPath();
                        Uri uri4=Uri.parse(path);
                        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                        sharingIntent.setType("audio/*");
                        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Share");
                        sharingIntent.putExtra(Intent.EXTRA_STREAM, uri4);
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));

                        return true;
                    case R.id.menu5:

                        if(musicService!=null) {
                            LayoutInflater inflater1 = (LayoutInflater)
                                    getSystemService(LAYOUT_INFLATER_SERVICE);
                            @SuppressLint("InflateParams") View popupView1 = inflater1.inflate(R.layout.timer_popup, null);

                            int width1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            int height1 = LinearLayout.LayoutParams.WRAP_CONTENT;
                            final PopupWindow popupWindow1 = new PopupWindow(popupView1, width1, height1, true);
                            View pview1 = popupWindow1.getContentView();

                            timer_ok = pview1.findViewById(R.id.timer_ok);
                            timer_cancel = pview1.findViewById(R.id.timer_cancel);
                            timernum = pview1.findViewById(R.id.timer_num);

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
                                    popupWindow1.dismiss();
                                }
                                else
                                {
                                    Toast.makeText(PlaySong.this, "Enter time first!", Toast.LENGTH_SHORT).show();
                                }
                            });

                            timer_cancel.setOnClickListener(v1 -> popupWindow1.dismiss());

                            popupWindow1.setAnimationStyle(R.style.Theme_XMusic);
                            popupWindow1.showAtLocation(v, Gravity.CENTER, 0, 0);
                        }
                        else
                        {
                            Toast.makeText(PlaySong.this, "Play a song first", Toast.LENGTH_SHORT).show();
                        }

                        return true;
                    case R.id.menu41:

                        Intent intent11=new Intent(PlaySong.this,FavActivity.class);
                        PlaySong.this.startActivity(intent11);
                }
                return true;
            });
        });
    }

    public void updatePosition(int position)
    {
        this.rep_pos=position;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Intent intent=getIntent();
        if(intent.getStringExtra("recFlag")!=null) {
            if (intent.getStringExtra("recFlag").equals("true")) {
                if ((keyCode == KeyEvent.KEYCODE_BACK)) {
                    Intent intent1 = new Intent(PlaySong.this, FavActivity.class);
                    intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent1);
                    super.onBackPressed();
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.bottom_up,R.anim.bottom_down);
    }

    @Override
    protected void onResume() {
        Intent intent=new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unbindService(this);
        super.onPause();
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder=(MusicService.MyBinder) service;
        musicService=myBinder.getService();
        musicService.setCallBack(this);
        int ids=musicService.getAudioSessionId();
        circleLineVisualizer.setAudioSessionId(ids);
        sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
        check=sharedPreferences3.getBoolean("visChecks",true);
        if (check) {
            circleLineVisualizer.setVisibility(View.VISIBLE);
        }
        else
        {
            circleLineVisualizer.setVisibility(View.GONE);
        }
        seekBar.setMax(musicService.getDuration());
        musicService.seekTo(currentTime+3);
        musicService.showNotification(R.drawable.pause,position);
        byte[] image = musicService.getAlbumArt(songs.get(position).getPath());
        if (image != null) {
            Glide.with(PlaySong.this).asBitmap().load(image).apply(RequestOptions.circleCropTransform()).into(albumImg);
        } else {
            Glide.with(PlaySong.this).asBitmap().load(R.drawable.music1).apply(RequestOptions.circleCropTransform()).into(albumImg);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService=null;
    }

    public void nextFunc()
    {
        flag1=false;
        swipeDismiss=false;
        if(queueList.size()>0 && queueList!=null)
        {
            if(queuePos<queueList.size()) {
                musicService.stop();
                musicService.release();
                musicService.createMediaPlayer(queueList.get(queuePos));
                musicService.start();
                sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                check=sharedPreferences3.getBoolean("visChecks",true);
                if (check) {
                    circleLineVisualizer.setVisibility(View.VISIBLE);
                }
                else
                {
                    circleLineVisualizer.setVisibility(View.GONE);
                }
                textView.setText(songs.get(queueList.get(queuePos)).getTitle());
                seekBar.setMax(musicService.getDuration());
                pause.setImageResource(R.drawable.pause);
                musicService.showNotification(R.drawable.pause,position);
                position = queueList.get(queuePos);
                posi=position;
                queuePos++;
            }
            if(queuePos==queueList.size())
            {
                queuePos=0;
                queueList.clear();
            }
        }

        else if(queueList1!=null && queueList1.size()>0)
        {
            if(queueList1.size()>0) {
                if (queuePos < queueList1.size()) {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(queueList1.get(queuePos));
                    musicService.start();
                    int ids1 = musicService.getAudioSessionId();
                    circleLineVisualizer.setAudioSessionId(ids1);
                    sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                    check=sharedPreferences3.getBoolean("visChecks",true);
                    if (check) {
                        circleLineVisualizer.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        circleLineVisualizer.setVisibility(View.GONE);
                    }
                    textView.setText(songs.get(queueList1.get(queuePos)).getTitle());
                    seekBar.setMax(musicService.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,position);
                    position = queueList1.get(queuePos);
                    posi=position;
                    queuePos++;
                }
            }
            if(queuePos==queueList1.size())
            {
                queuePos=0;
                queueList1.clear();
            }
        }

        else if(queueList2!=null && queueList2.size()>0)
        {
            if(queueList2.size()>0) {
                if (queuePos < queueList2.size()) {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(queueList2.get(queuePos));
                    musicService.start();
                    int ids1 = musicService.getAudioSessionId();
                    circleLineVisualizer.setAudioSessionId(ids1);
                    sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                    check=sharedPreferences3.getBoolean("visChecks",true);
                    if (check) {
                        circleLineVisualizer.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        circleLineVisualizer.setVisibility(View.GONE);
                    }
                    textView.setText(songs.get(queueList2.get(queuePos)).getTitle());
                    seekBar.setMax(musicService.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,position);
                    position = queueList2.get(queuePos);
                    posi=position;
                    queuePos++;
                }
            }
            if(queuePos==queueList2.size())
            {
                queuePos=0;
                queueList2.clear();
            }
        }

        else if(queueList3!=null && queueList3.size()>0)
        {
            if(queueList3.size()>0) {
                if (queuePos < queueList3.size()) {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(queueList3.get(queuePos));
                    musicService.start();
                    int ids1 = musicService.getAudioSessionId();
                    circleLineVisualizer.setAudioSessionId(ids1);
                    sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                    check=sharedPreferences3.getBoolean("visChecks",true);
                    if (check) {
                        circleLineVisualizer.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        circleLineVisualizer.setVisibility(View.GONE);
                    }
                    textView.setText(songs.get(queueList3.get(queuePos)).getTitle());
                    seekBar.setMax(musicService.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,position);
                    position = queueList3.get(queuePos);
                    posi=position;
                    queuePos++;
                }
            }
            if(queuePos==queueList3.size())
            {
                queuePos=0;
                queueList3.clear();
            }
        }

        else if(queueList4!=null && queueList4.size()>0)
        {
            if(queueList4.size()>0) {
                if (queuePos < queueList4.size()) {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(queueList4.get(queuePos));
                    musicService.start();
                    int ids1 = musicService.getAudioSessionId();
                    circleLineVisualizer.setAudioSessionId(ids1);
                    sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                    check=sharedPreferences3.getBoolean("visChecks",true);
                    if (check) {
                        circleLineVisualizer.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        circleLineVisualizer.setVisibility(View.GONE);
                    }
                    textView.setText(songs.get(queueList4.get(queuePos)).getTitle());
                    seekBar.setMax(musicService.getDuration());
                    pause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,position);
                    position = queueList4.get(queuePos);
                    posi=position;
                    queuePos++;
                }
            }
            if(queuePos==queueList4.size())
            {
                queuePos=0;
                queueList4.clear();
            }
        }

        else if(!playNext.equals(""))
        {
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(Integer.parseInt(playNext));
            musicService.start();
            int ids1=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids1);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(Integer.parseInt(playNext)).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            position=Integer.parseInt(playNext);
            posi=position;
            playNext="";
        }

        else if(!playNext1.equals(""))
        {
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(Integer.parseInt(playNext1));
            musicService.start();
            int ids1=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids1);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(Integer.parseInt(playNext1)).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            position=Integer.parseInt(playNext1);
            posi=position;
            playNext1="";
        }

        else if(!playNext2.equals(""))
        {
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(Integer.parseInt(playNext2));
            musicService.start();
            int ids1=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids1);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(Integer.parseInt(playNext2)).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            position=Integer.parseInt(playNext2);
            posi=position;
            playNext2="";
        }

        else if(!playNext3.equals(""))
        {
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(Integer.parseInt(playNext3));
            musicService.start();
            int ids1=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids1);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(Integer.parseInt(playNext3)).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            position=Integer.parseInt(playNext3);
            posi=position;
            playNext3="";
        }

        else if(!playNext4.equals(""))
        {
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(Integer.parseInt(playNext4));
            musicService.start();
            int ids1=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids1);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(Integer.parseInt(playNext4)).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            position=Integer.parseInt(playNext4);
            posi=position;
            playNext4="";
        }

        else {

            if (repeat_bool) {
                if (shuffle_bool) {
                    shuffle_bool = false;
                }
                musicService.stop();
                musicService.release();
                musicService.createMediaPlayer(rep_pos);
                musicService.start();
                int ids21 = musicService.getAudioSessionId();
                circleLineVisualizer.setAudioSessionId(ids21);
                sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                check=sharedPreferences3.getBoolean("visChecks",true);
                if (check) {
                    circleLineVisualizer.setVisibility(View.VISIBLE);
                }
                else
                {
                    circleLineVisualizer.setVisibility(View.GONE);
                }
                textView.setText(songs.get(rep_pos).getTitle());
                seekBar.setMax(musicService.getDuration());
                pause.setImageResource(R.drawable.pause);
                musicService.showNotification(R.drawable.pause,position);
                posi=position;
            } else if (shuffle_bool) {

                musicService.stop();
                musicService.release();
                Random random = new Random();
                int rpos = random.nextInt(songs.size());
                updatePosition(rpos);
                musicService.createMediaPlayer(rpos);
                musicService.start();
                int ids22 = musicService.getAudioSessionId();
                circleLineVisualizer.setAudioSessionId(ids22);
                sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                check=sharedPreferences3.getBoolean("visChecks",true);
                if (check) {
                    circleLineVisualizer.setVisibility(View.VISIBLE);
                }
                else
                {
                    circleLineVisualizer.setVisibility(View.GONE);
                }
                pause.setImageResource(R.drawable.pause);
                musicService.showNotification(R.drawable.pause,position);
                textView.setText(songs.get(rpos).getTitle());
                seekBar.setMax(musicService.getDuration());
                position = rpos;
                posi=position;
                if (jsonText != null) {
                    if (favList3.contains(songs.get(position).getTitle())) {
                        favorites.setImageResource(R.drawable.favorite_fill);
                    } else {
                        favorites.setImageResource(R.drawable.favorite);
                    }
                }
            } else {
                musicService.stop();
                musicService.release();
                if (position != songs.size() - 1) {
                    position = position + 1;
                } else {
                    position = 0;
                }
                musicService.createMediaPlayer(position);
                musicService.start();
                int ids3 = musicService.getAudioSessionId();
                circleLineVisualizer.setAudioSessionId(ids3);
                sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
                check=sharedPreferences3.getBoolean("visChecks",true);
                if (check) {
                    circleLineVisualizer.setVisibility(View.VISIBLE);
                }
                else
                {
                    circleLineVisualizer.setVisibility(View.GONE);
                }
                seekBar.setMax(musicService.getDuration());
                String textContent1 = songs.get(position).getTitle();
                textView.setText(textContent1);
                pause.setImageResource(R.drawable.pause);
                musicService.showNotification(R.drawable.pause,position);
                posi=position;
            }
        }

        if(jsonText!=null) {
            if (favList3.contains(songs.get(position).getTitle())) {
                favorites.setImageResource(R.drawable.favorite_fill);
            } else {
                favorites.setImageResource(R.drawable.favorite);
            }
        }

        byte[] image3 = musicService.getAlbumArt(songs.get(position).getPath());
        if (image3 != null) {
            try {
                Glide.with(this).asBitmap().load(image3).apply(RequestOptions.circleCropTransform()).into(albumImg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } else {
            Glide.with(this).asBitmap().load(R.drawable.music1).into(albumImg);
        }
        musicService.showNotification(R.drawable.pause,position);
        posi=position;
    }

    public void prevFunc()
    {
        flag1=false;
        swipeDismiss=false;
        if(queueList.size()>0)
        {
            if(queuePos<queueList.size())
            {
                queueList.clear();
            }
        }
        if(queueList1.size()>0)
        {
            if(queuePos<queueList1.size())
            {
                queueList1.clear();
            }
        }
        if(queueList2.size()>0)
        {
            if(queuePos<queueList2.size())
            {
                queueList2.clear();
            }
        }
        if(queueList3.size()>0)
        {
            if(queuePos<queueList3.size())
            {
                queueList3.clear();
            }
        }
        if(queueList4.size()>0)
        {
            if(queuePos<queueList4.size())
            {
                queueList4.clear();
            }
        }

        if(repeat_bool) {
            if(shuffle_bool)
            {
                shuffle_bool=false;
            }
            musicService.stop();
            musicService.release();
            musicService.createMediaPlayer(rep_pos);
            musicService.start();
            int ids24=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids24);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            textView.setText(songs.get(rep_pos).getTitle());
            seekBar.setMax(musicService.getDuration());
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            posi=position;
        }

        else if(shuffle_bool)
        {
            musicService.stop();
            musicService.release();
            Random random=new Random();
            int rpos=random.nextInt(songs.size());
            updatePosition(rpos);
            musicService.createMediaPlayer(rpos);
            musicService.start();
            int ids23=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids23);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            textView.setText(songs.get(rpos).getTitle());
            seekBar.setMax(musicService.getDuration());
            position=rpos;
            posi=position;
            if(jsonText!=null) {
                if (favList3.contains(songs.get(position).getTitle())) {
                    favorites.setImageResource(R.drawable.favorite_fill);
                } else {
                    favorites.setImageResource(R.drawable.favorite);
                }
            }
        }

        else {
            musicService.stop();
            musicService.release();
            if (position != 0) {
                position = position - 1;
            } else {
                position = songs.size() - 1;
            }
            musicService.createMediaPlayer(position);
            musicService.start();
            int ids2=musicService.getAudioSessionId();
            circleLineVisualizer.setAudioSessionId(ids2);
            sharedPreferences3=getSharedPreferences("visCheck",MODE_PRIVATE);
            check=sharedPreferences3.getBoolean("visChecks",true);
            if (check) {
                circleLineVisualizer.setVisibility(View.VISIBLE);
            }
            else
            {
                circleLineVisualizer.setVisibility(View.GONE);
            }
            seekBar.setMax(musicService.getDuration());
            String textContent1 = songs.get(position).getTitle();
            textView.setText(textContent1);
            pause.setImageResource(R.drawable.pause);
            musicService.showNotification(R.drawable.pause,position);
            posi=position;
        }

        if(jsonText!=null) {
            if (favList3.contains(songs.get(position).getTitle())) {
                favorites.setImageResource(R.drawable.favorite_fill);
            } else {
                favorites.setImageResource(R.drawable.favorite);
            }
        }

        byte[] image1 = musicService.getAlbumArt(songs.get(position).getPath());
        if (image1 != null) {
            try {
                Glide.with(this).asBitmap().load(image1).apply(RequestOptions.circleCropTransform()).into(albumImg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        } else{
            Glide.with(PlaySong.this).asBitmap().load(R.drawable.music1).into(albumImg);
        }
    }

    public void pauseFunc()
    {
        if(!flag1)
        {
            flag1=true;
            swipeDismiss=true;
        }
        else
        {
            flag1=false;
            swipeDismiss=false;
        }
        if(musicService.isPlaying())
        {
            musicService.showNotification(R.drawable.play,position);
            pause.setImageResource(R.drawable.play);
            musicService.pause();
            posi=position;
        }
        else
        {
            musicService.showNotification(R.drawable.pause,position);
            pause.setImageResource(R.drawable.pause);
            musicService.start();
            posi=position;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu5,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        int id = item.getItemId();
        switch (id){
            case R.id.menu23:
            case R.id.menu4:

                Intent intent4=new Intent(this,PlayList_.class);
                startActivity(intent4);
                return true;
            case R.id.menu1:

                Toast.makeText(PlaySong.this,"Item 1 Selected",Toast.LENGTH_LONG).show();

                return true;
            case R.id.menu2:

                shareApplication();

                return true;
            case R.id.menu3:
            case R.id.menu20:

                Intent intent1=new Intent(PlaySong.this,FavActivity.class);
                startActivity(intent1);

                return true;

            case R.id.menu5:

                if(musicService!=null) {

                    new Handler().post(() -> {
                        final View menuItemView = findViewById(R.id.menu_main);

                        LayoutInflater inflater2 = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
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
                                        finish();
                                        System.exit(0);
                                    }
                                }, timer_val * 1000);
                                popupWindow2.dismiss();
                            }
                            else
                            {
                                Toast.makeText(PlaySong.this, "Enter time first!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                        popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                        popupWindow2.showAtLocation(menuItemView, Gravity.CENTER, 0, 0);
                    });
                }
                else
                {
                    Toast.makeText(this, "Play a song first", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.menu6:

                Intent intent2=new Intent(PlaySong.this,AboutAcitvity.class);
                startActivity(intent2);

                return true;
            case R.id.menu7:

                Intent intent3=new Intent(PlaySong.this,SupportActivity.class);
                startActivity(intent3);

                return true;
            case R.id.menu8:

                finish();
                System.exit(0);

                return true;

            default:
                return true;
        }
    }

    private void shareApplication() {
        ApplicationInfo app = getApplicationContext().getApplicationInfo();
        String filePath = app.sourceDir;

        Intent intent = new Intent(Intent.ACTION_SEND);

        intent.setType("*/*");

        File originalApk = new File(filePath);

        try {
            File tempFile = new File(getExternalCacheDir() + "/ExtractedApk");
            if (!tempFile.isDirectory())
                if (!tempFile.mkdirs())
                    return;
            tempFile = new File(tempFile.getPath() + "/" + getString(app.labelRes).replace(" ","").toLowerCase() + ".apk");
            if (!tempFile.exists()) {
                if (!tempFile.createNewFile()) {
                    return;
                }
            }
            InputStream in = new FileInputStream(originalApk);
            OutputStream out = new FileOutputStream(tempFile);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            in.close();
            out.close();
            System.out.println("File copied.");
            Uri photoURI = FileProvider.getUriForFile(this, BuildConfig.APPLICATION_ID + ".provider", tempFile);
            intent.putExtra(Intent.EXTRA_STREAM, photoURI);
            startActivity(Intent.createChooser(intent, "Share with your friends"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}