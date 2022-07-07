package com.hiten_khatri.xmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

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
import java.util.Timer;
import java.util.TimerTask;

import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;
import static com.hiten_khatri.xmusic.MainActivity.songList;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.timernum;

public class FavActivity extends AppCompatActivity {

    public static ArrayList<MusicFiles> favFileList;
    public static String jsonText;
    public static ArrayList<String> favList3;
    SharedPreferences sharedPreferences;
    public static SharedPreferences.Editor editor;
    public static Gson gson;
    recCusAda arrayAdapter;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav);

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("Favorites");
        toolbar.setTitleTextColor(Color.parseColor("#FF8C00"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ListView listView = findViewById(R.id.favSongList);
        favFileList=new ArrayList<>();

        sharedPreferences=getSharedPreferences("favList",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        gson=new Gson();

        jsonText=sharedPreferences.getString("favList",null);
        favList3=gson.fromJson(jsonText,new TypeToken<ArrayList<String>>(){}.getType());

        if(jsonText!=null && !jsonText.equals("[]")) {

            for (int i = 0; i < favList3.size(); i++) {
                favFileList.add(musicFilesArrayList.get(songList.indexOf(favList3.get(i))));
            }
            arrayAdapter = new recCusAda(FavActivity.this, R.layout.rec_cus, favList3);
            listView.setAdapter(arrayAdapter);
        }
        else
        {
            ArrayList<String> temp=new ArrayList<>();
            temp.add("No songs added!");
            recCusAda arrayAdapter=new recCusAda(FavActivity.this,R.layout.rec_cus,temp);
            listView.setAdapter(arrayAdapter);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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

                Toast.makeText(FavActivity.this,"Item 1 Selected",Toast.LENGTH_LONG).show();

                return true;
            case R.id.menu2:

                shareApplication();

                return true;
            case R.id.menu3:
            case R.id.menu20:

                Intent intent1=new Intent(FavActivity.this,FavActivity.class);
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
                                Toast.makeText(FavActivity.this, "Enter time first!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(FavActivity.this, "Enter time first!", Toast.LENGTH_SHORT).show();
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

                Intent intent2=new Intent(FavActivity.this,AboutAcitvity.class);
                startActivity(intent2);

                return true;
            case R.id.menu7:

                Intent intent3=new Intent(FavActivity.this,SupportActivity.class);
                startActivity(intent3);

                return true;
            case R.id.menu8:

                finish();
                System.exit(0);

                return true;
            case R.id.menu9:

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