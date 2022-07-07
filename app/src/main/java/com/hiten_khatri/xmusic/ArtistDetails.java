package com.hiten_khatri.xmusic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.graphics.Color;
import android.media.MediaMetadataRetriever;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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

import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.timer_cancel;
import static com.hiten_khatri.xmusic.PlaySong.timer_ok;
import static com.hiten_khatri.xmusic.PlaySong.timernum;

public class ArtistDetails extends AppCompatActivity {

    ArtistDetailsCus artistDetailsCus;
    public ListView artistListView;
    public ImageView artistImage;
    public static ArrayList<String> artistSongList;
    public static ArrayList<MusicFiles> artistSongList1;
    public Button shuffle;
    public Button playAll;
    public TextView head;
    public TextView artist;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_details);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setTitle("X Music");
        toolbar.setTitleTextColor(Color.parseColor("#FF8C00"));
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextAppearance(this,R.style.RobotoBoldTextAppearance);

        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

        artistImage=findViewById(R.id.imageView6);
        artistListView=findViewById(R.id.artistListView);
        artistSongList=new ArrayList<>();
        artistSongList1=new ArrayList<>();
        shuffle=findViewById(R.id.button5);
        playAll=findViewById(R.id.button7);
        head=findViewById(R.id.textView20);
        artist=findViewById(R.id.textView21);

        Intent intent=getIntent();
        String name=intent.getStringExtra("artistName");
        String path=intent.getStringExtra("artistPath");

        int j=0;
        for(int i=0;i<musicFilesArrayList.size();i++)
        {
            if(name.equals(musicFilesArrayList.get(i).getArtist()))
            {
                artistSongList.add(musicFilesArrayList.get(i).getTitle());
                artistSongList1.add(j,musicFilesArrayList.get(i));
                j++;
            }
        }

        byte[] image = getAlbumArt(path);
        if (image != null) {
            Glide.with(this).asBitmap().load(image).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL).override(250, 250)).into(artistImage);
        } else {
            Glide.with(this).asBitmap().load(R.drawable.music1).into(artistImage);
        }

        head.setText(name);
        if(artistSongList1.size()>1) {
            artist.setText(artistSongList1.size() + " Songs");
        }
        else{
            artist.setText(artistSongList1.size() + " Song");
        }

        artistDetailsCus=new ArtistDetailsCus(this,R.layout.rec_cus,artistSongList);
        artistListView.setAdapter(artistDetailsCus);

        playAll.setOnClickListener(v -> {
            Intent intent12 = new Intent(this, PlaySong.class);
            intent12.putExtra("position", 0);
            intent12.putExtra("currentSong", artistSongList.get(0));
            intent12.putExtra("songList", "artistDetails");
            startActivity(intent12);
        });

        shuffle.setOnClickListener(v -> {
            Random random = new Random();
            int rpos = random.nextInt(artistSongList.size());
            Intent intent1 = new Intent(this, PlaySong.class);
            intent1.putExtra("position", rpos);
            intent1.putExtra("currentSong", artistSongList.get(rpos));
            intent1.putExtra("songList", "artistDetails");
            startActivity(intent1);
        });
    }

    public byte[] getAlbumArt(String uri)
    {
        MediaMetadataRetriever retriever=new MediaMetadataRetriever();
        retriever.setDataSource(uri);
        byte[] art=retriever.getEmbeddedPicture();
        retriever.release();
        return art;
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

                Toast.makeText(this,"Item 1 Selected",Toast.LENGTH_LONG).show();

                return true;
            case R.id.menu2:

                shareApplication();

                return true;
            case R.id.menu3:
            case R.id.menu20:

                Intent intent1=new Intent(this,FavActivity.class);
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
                                Toast.makeText(this, "Enter time first!", Toast.LENGTH_SHORT).show();
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
                                Toast.makeText(this, "Enter time first!", Toast.LENGTH_SHORT).show();
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

                Intent intent2=new Intent(this,AboutAcitvity.class);
                startActivity(intent2);

                return true;
            case R.id.menu7:

                Intent intent3=new Intent(this,SupportActivity.class);
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