package com.hiten_khatri.xmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import androidx.appcompat.widget.SearchView;
import android.widget.Toast;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
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
import static com.hiten_khatri.xmusic.MusicService.ARTIST_NAME;
import static com.hiten_khatri.xmusic.MusicService.CURRENT_SONG_LIST;
import static com.hiten_khatri.xmusic.MusicService.MUSIC_FILE;
import static com.hiten_khatri.xmusic.MusicService.MUSIC_FILE_LAST_PLAYED;
import static com.hiten_khatri.xmusic.MusicService.SONG_NAME;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.timernum;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public boolean flag=false;
    Animation from_bottom;
    Animation to_bottom;
    Animation rotate_close;
    Animation rotate_open;
    public FloatingActionButton floatingActionButton;
    public FloatingActionButton floatingActionButton2;
    public FloatingActionButton floatingActionButton3;
    public FloatingActionButton floatingActionButton4;
    public FloatingActionButton floatingActionButton5;
    public Gson gson;
    public static String currentSong1;
    public static int posi=0;
    public static ArrayList<MusicFiles> songs1;
    public static String CMS1;
    public static String SONG_NAME1="";
    public static String SONG_ARTIST1="";
    public static String PATH_TO_FRAG=null;
    public static boolean SHOW_MINI_PLAYER=false;
    public static String MY_SORT_PREF="sortOrder";
    public static SharedPreferences sharedPreferences;
    public static String sortOrder;
    public static String order;
    public static SharedPreferences.Editor editor;
    public static ArrayList<MusicFiles> musicFilesArrayList;
    public TabLayout tabLayout;
    public TabItem hometab;
    public TabItem songstab;
    public TabItem albumtab;
    public TabItem artisttab;
    private ViewPager viewPager2;
    public static ArrayList<String> songList;
    public static ArrayList<String> songList1;
    public static ArrayList<MusicFiles> songAlbum;
    public static ArrayList<String> songAlbum1;
    public static ArrayList<MusicFiles> songArtist;
    public static ArrayList<String> songArtist1;
    @SuppressLint("StaticFieldLeak")
    FrameLayout bottomPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_XMusic);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPreferences2=getSharedPreferences("startUp",MODE_PRIVATE);
        SharedPreferences.Editor editor2=sharedPreferences2.edit();
        String startUp=sharedPreferences2.getString("startUps",null);
        if(startUp==null) {
            editor2.putString("startUps","yes");
            editor2.apply();
            Intent intent10=new Intent(this,StartupActivity.class);
            startActivity(intent10);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("X Music");
        toolbar.setTitleTextColor(Color.parseColor("#FF8C00"));
        toolbar.setTitleTextAppearance(this,R.style.RobotoBoldTextAppearance);

        tabLayout=findViewById(R.id.tabLayout);
        hometab=findViewById(R.id.hometab);
        songstab=findViewById(R.id.songstab);
        albumtab=findViewById(R.id.albumtab);
        artisttab=findViewById(R.id.artisttab);
        viewPager2=findViewById(R.id.viewPager);
        bottomPlayer=findViewById(R.id.frameLayout);
        songs1=new ArrayList<>();
        gson=new Gson();
        songList1=new ArrayList<>();
        floatingActionButton=findViewById(R.id.floatBtn);
        floatingActionButton2=findViewById(R.id.floatBtn2);
        floatingActionButton3=findViewById(R.id.floatBtn3);
        floatingActionButton4=findViewById(R.id.floatBtn4);
        floatingActionButton5=findViewById(R.id.floatBtn5);
        from_bottom= AnimationUtils.loadAnimation(this,R.anim.from_bottom);
        to_bottom= AnimationUtils.loadAnimation(this,R.anim.to_bottom);
        rotate_close= AnimationUtils.loadAnimation(this,R.anim.rotate_close);
        rotate_open= AnimationUtils.loadAnimation(this,R.anim.rotate_open);

        floatingActionButton2.setVisibility(View.GONE);
        floatingActionButton3.setVisibility(View.GONE);
        floatingActionButton4.setVisibility(View.GONE);
        floatingActionButton5.setVisibility(View.GONE);
        floatingActionButton2.setClickable(false);
        floatingActionButton3.setClickable(false);
        floatingActionButton4.setClickable(false);
        floatingActionButton5.setClickable(false);

        floatingActionButton.setOnClickListener(v -> {
            if(!flag) {
                animFunc2();
            }
            else
            {
                animFunc1();
            }
        });
        floatingActionButton2.setOnClickListener(v -> {
            animFunc1();
            Intent intent=new Intent(MainActivity.this,FavActivity.class);
            startActivity(intent);
        });
        floatingActionButton3.setOnClickListener(v -> {
            animFunc1();
            Intent intent=new Intent(MainActivity.this,PlayList_.class);
            startActivity(intent);
        });
        floatingActionButton4.setOnClickListener(v -> {
            animFunc1();
            Random random = new Random();
            int rpos = random.nextInt(musicFilesArrayList.size());
            Intent intent1 = new Intent(MainActivity.this, PlaySong.class);
            intent1.putExtra("position", rpos);
            intent1.putExtra("currentSong", songList.get(rpos));
            intent1.putExtra("songList", "songAdap");
            intent1.putExtra("shuffle_bool",true);
            startActivity(intent1);
        });
        floatingActionButton5.setOnClickListener(v -> {
            animFunc1();
            Intent intent1 = new Intent(MainActivity.this, PlaySong.class);
            intent1.putExtra("position", 0);
            intent1.putExtra("currentSong", songList.get(0));
            intent1.putExtra("songList", "songAdap");
            startActivity(intent1);
        });

        deleteCache(this);

        if(startUp!=null) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {

                    requestPermissions(new String[]{Manifest.permission.RECORD_AUDIO}, 1);
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

                Intent intent = new Intent(this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(this, 1000, intent, PendingIntent.FLAG_CANCEL_CURRENT);

                AlarmManager alarmManager = (AlarmManager) this.getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, pendingIntent);
            } else {
                PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
                pagerAdapter.addFragments(new Fragment(), "Home");
                pagerAdapter.addFragments(new Fragment(), "Songs");
                pagerAdapter.addFragments(new Fragment(), "Album");
                pagerAdapter.addFragments(new Fragment(), "Artist");
                viewPager2.setAdapter(pagerAdapter);
                tabLayout.setupWithViewPager(viewPager2);
            }
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)== PackageManager.PERMISSION_GRANTED){
            sharedPreferences=getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE);
            sortOrder=sharedPreferences.getString("sorting","sort_by_name");
            order=null;
            switch (sortOrder){
                case "sort_by_date1":
                    order=MediaStore.MediaColumns.DATE_ADDED + " ASC";
                    break;
                case "sort_by_date2":
                    order=MediaStore.MediaColumns.DATE_ADDED + " DESC";
                    break;
                case "sort_by_size2":
                    order=MediaStore.MediaColumns.SIZE + " DESC";
                    break;
                case "sort_by_size1":
                    order=MediaStore.MediaColumns.SIZE + " ASC";
                    break;
                case "sort_by_name1":
                    order=MediaStore.MediaColumns.DISPLAY_NAME + " ASC";
                    break;
                case "sort_by_name2":
                    order=MediaStore.MediaColumns.DISPLAY_NAME + " DESC";
                    break;
            }
            songAlbum=new ArrayList<>();
            songAlbum1=new ArrayList<>();
            songArtist=new ArrayList<>();
            songArtist1=new ArrayList<>();
            musicFilesArrayList=new ArrayList<>();
            Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            @SuppressLint("InlinedApi") String[] projection={
                    MediaStore.Audio.Media.DATA,
                    MediaStore.Audio.Media.ARTIST,
                    MediaStore.Audio.Media._ID,
                    MediaStore.Audio.Media.ALBUM,
                    MediaStore.Audio.Media.TITLE,
                    MediaStore.Audio.Media.DURATION,

            };
            Cursor cursor=getContentResolver().query(uri,projection,null,null,order);
            if(cursor!=null) {
                while (cursor.moveToNext()) {
                    String path = cursor.getString(0);
                    String artist = cursor.getString(1);
                    String songAlb = cursor.getString(3);
                    String title = cursor.getString(4);
                    String duration = cursor.getString(5);
                    long id=Long.parseLong(cursor.getString(2));
                    MusicFiles musicFiles=new MusicFiles(path,title,artist,songAlb,duration,id);
                    if(!songAlbum1.contains(songAlb))
                    {
                        songAlbum1.add(songAlb);
                        songAlbum.add(musicFiles);
                    }
                    if(!songArtist1.contains(artist))
                    {
                        songArtist1.add(artist);
                        songArtist.add(musicFiles);
                    }
                    musicFilesArrayList.add(musicFiles);
                }
                cursor.close();
            }

            songList=new ArrayList<>();
            for(int i=0;i<musicFilesArrayList.size();i++)
            {
                songList.add(musicFilesArrayList.get(i).getTitle());
            }
        }
        else
        {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},1);
        }
    }

    private void animFunc2() {
        flag=true;
        floatingActionButton.startAnimation(rotate_open);
        floatingActionButton2.startAnimation(from_bottom);
        floatingActionButton3.startAnimation(from_bottom);
        floatingActionButton2.setVisibility(View.VISIBLE);
        floatingActionButton3.setVisibility(View.VISIBLE);
        floatingActionButton2.setClickable(true);
        floatingActionButton3.setClickable(true);
        floatingActionButton4.startAnimation(from_bottom);
        floatingActionButton5.startAnimation(from_bottom);
        floatingActionButton4.setVisibility(View.VISIBLE);
        floatingActionButton5.setVisibility(View.VISIBLE);
        floatingActionButton4.setClickable(true);
        floatingActionButton5.setClickable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_item,menu);
        MenuItem menuItem = menu.findItem(R.id.action_search);
        SearchView searchView=(SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(this);
        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        editor=getSharedPreferences(MY_SORT_PREF,MODE_PRIVATE).edit();
        int id = item.getItemId();
        switch (id){
            case R.id.menu19:

                if(musicService!=null)
                {
                    int sessionId=musicService.getAudioSessionId();
                    Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                    startActivityForResult(intent12,0);
                    intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                }
                else if(mediaPlayer!=null)
                {
                    int sessionId=mediaPlayer.getAudioSessionId();
                    Intent intent12 =new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    intent12.putExtra(AudioEffect.EXTRA_AUDIO_SESSION,sessionId);
                    startActivityForResult(intent12,0);
                    intent12.putExtra(AudioEffect.EXTRA_CONTENT_TYPE, AudioEffect.CONTENT_TYPE_MUSIC);
                }
                else
                {
                    Toast.makeText(this, "Start a song first!", Toast.LENGTH_SHORT).show();
                }

                return true;
            case R.id.menu1:

                Toast.makeText(MainActivity.this,"Item 1 Selected",Toast.LENGTH_LONG).show();

                return true;
            case R.id.menu2:

                shareApplication();

                return true;
            case R.id.menu3:
            case R.id.menu20:

                Intent intent1=new Intent(MainActivity.this,FavActivity.class);
                startActivity(intent1);

                return true;
            case R.id.menu4:
            case R.id.menu23:

                Intent intent=new Intent(MainActivity.this,PlayList_.class);
                startActivity(intent);

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
                                Toast.makeText(MainActivity.this, "Enter time first!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        timer_cancel.setOnClickListener(v1 -> popupWindow2.dismiss());

                        popupWindow2.setAnimationStyle(R.style.Theme_XMusic);
                        popupWindow2.showAtLocation(menuItemView, Gravity.CENTER, 0, 0);
                    });
                }
                else if(mediaPlayer!=null) {

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
                                        mediaPlayer.stop();
                                        mediaPlayer.release();
                                        finish();
                                        System.exit(0);
                                    }
                                }, timer_val * 1000);
                                popupWindow2.dismiss();
                            }
                            else
                            {
                                Toast.makeText(MainActivity.this, "Enter time first!", Toast.LENGTH_SHORT).show();
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

                Intent intent2=new Intent(MainActivity.this,AboutAcitvity.class);
                startActivity(intent2);

                return true;
            case R.id.menu7:

                Intent intent3=new Intent(MainActivity.this,SupportActivity.class);
                startActivity(intent3);

                return true;
            case R.id.menu8:

                finish();
                System.exit(0);

                return true;

            case R.id.menu14:

                editor.putString("sorting","sort_by_date2");
                editor.apply();
                MainActivity.this.recreate();

                return true;
            case R.id.menu15:

                editor.putString("sorting","sort_by_size1");
                editor.apply();
                MainActivity.this.recreate();

                return true;
            case R.id.menu16:

                editor.putString("sorting","sort_by_size2");
                editor.apply();
                MainActivity.this.recreate();

                return true;
            case R.id.menu17:

                editor.putString("sorting","sort_by_name1");
                editor.apply();
                MainActivity.this.recreate();

                return true;
            case R.id.menu18:

                editor.putString("sorting","sort_by_name2");
                editor.apply();
                MainActivity.this.recreate();

                return true;
            case R.id.menu13:

                editor.putString("sorting","sort_by_date1");
                editor.apply();
                MainActivity.this.recreate();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
    }

    public static void deleteCache(Context context) {
        try {
            File dir = context.getCacheDir();
            deleteDir(dir);
        } catch (Exception e) { e.printStackTrace();}
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (String child : Objects.requireNonNull(children)) {
                boolean success = deleteDir(new File(dir, child));
                if (!success) {
                    return false;
                }
            }
            return dir.delete();
        } else if(dir!= null && dir.isFile()) {
            return dir.delete();
        } else {
            return false;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences=getSharedPreferences(MUSIC_FILE_LAST_PLAYED,MODE_PRIVATE);
        String value=sharedPreferences.getString(MUSIC_FILE,null);
        String song=sharedPreferences.getString(SONG_NAME,null);
        String artist=sharedPreferences.getString(ARTIST_NAME,null);
        String cPlayList=sharedPreferences.getString("PLAYLIST_NAME",null);
        currentSong1=sharedPreferences.getString(CURRENT_SONG_LIST,"songAdap");
        if(value!=null)
        {
            bottomPlayer.setVisibility(View.VISIBLE);
            PATH_TO_FRAG=value;
            SHOW_MINI_PLAYER=true;
            SONG_NAME1=song;
            SONG_ARTIST1=artist;

            switch (currentSong1) {
                case "albumDetails":
                    songList1=new ArrayList<>();
                    songs1=new ArrayList<>();
                    if(albumSongList1!=null) {
                        for (int i = 0; i < albumSongList1.size(); i++) {
                            songList1.add(albumSongList1.get(i).getTitle());
                        }
                        songs1 = albumSongList1;
                    }
                    else
                    {
                        songList1=songList;
                        songs1=musicFilesArrayList;
                    }
                    posi=songList1.indexOf(song);
                    break;
                case "favFileList":
                    songs1=new ArrayList<>();
                    songList1=new ArrayList<>();
                    SharedPreferences sharedPreferences1=getSharedPreferences("favList",MODE_PRIVATE);
                    String jsonText=sharedPreferences1.getString("favList",null);
                    ArrayList<String> favList3=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());
                    if(jsonText!=null && !jsonText.equals("[]")) {
                        for (int i = 0; i < favList3.size(); i++) {
                            songs1.add(musicFilesArrayList.get(songList.indexOf(favList3.get(i))));
                            songList1.add(musicFilesArrayList.get(songList.indexOf(favList3.get(i))).getTitle());
                        }
                    }
                    else
                    {
                        songs1=musicFilesArrayList;
                        songList1=songList;
                    }
                    posi=songList1.indexOf(song);
                    break;
                case "playListFileList":
                case "playAll":
                case "playAll1":
                    songList1=new ArrayList<>();
                    songs1=new ArrayList<>();
                    if(cPlayList!=null && !cPlayList.equals("[]"))
                    {
                        SharedPreferences sharedPreferences2=getSharedPreferences(cPlayList,MODE_PRIVATE);
                        String string=sharedPreferences2.getString(cPlayList,null);
                        if(string!=null && !string.equals("[]")) {
                            ArrayList<String> playListSongs = gson.fromJson(string, new TypeToken<ArrayList<String>>() {
                            }.getType());
                            for(int i=0;i<playListSongs.size();i++) {
                                songList1.add(musicFilesArrayList.get(songList.indexOf(playListSongs.get(i))).getTitle());
                                songs1.add(musicFilesArrayList.get(songList.indexOf(playListSongs.get(i))));
                            }
                        }
                        else
                        {
                            songs1=musicFilesArrayList;
                            songList1=songList;
                        }
                    }
                    else
                    {
                        songs1=musicFilesArrayList;
                        songList1=songList;
                    }
                    posi=songList1.indexOf(song);
                    break;
                case "songAdap":
                default:
                    songs1=new ArrayList<>();
                    songList1=new ArrayList<>();
                    songList1=songList;
                    songs1 = musicFilesArrayList;
                    posi=songList1.indexOf(song);
                    break;
            }
        }
        else
        {
            PATH_TO_FRAG=null;
            SHOW_MINI_PLAYER=false;
            SONG_NAME1="";
            SONG_ARTIST1="";
            CMS1="songAdap";
            posi=0;
            bottomPlayer.setVisibility(View.GONE);
        }
    }

    public void animFunc1()
    {
        flag=false;
        floatingActionButton.startAnimation(rotate_close);
        floatingActionButton2.startAnimation(to_bottom);
        floatingActionButton3.startAnimation(to_bottom);
        floatingActionButton2.setVisibility(View.GONE);
        floatingActionButton3.setVisibility(View.GONE);
        floatingActionButton2.setClickable(false);
        floatingActionButton3.setClickable(false);
        floatingActionButton4.startAnimation(to_bottom);
        floatingActionButton5.startAnimation(to_bottom);
        floatingActionButton4.setVisibility(View.GONE);
        floatingActionButton5.setVisibility(View.GONE);
        floatingActionButton4.setClickable(false);
        floatingActionButton5.setClickable(false);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
         String userInput=newText.toLowerCase();
         ArrayList<MusicFiles> musicFiles=new ArrayList<>();
         for(MusicFiles song:musicFilesArrayList)
         {
             if(song.getTitle().toLowerCase().contains(userInput))
             {
                 musicFiles.add(song);
             }
         }
         songFrag.songAdapter.updateList(musicFiles);
        return true;
    }
}