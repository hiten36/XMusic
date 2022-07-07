package com.hiten_khatri.xmusic;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.media.session.MediaSessionCompat;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.ArrayList;

import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_DELETE;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_NEXT;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_PLAY;
import static com.hiten_khatri.xmusic.ApplicationClass.ACTION_PREVIOUS;
import static com.hiten_khatri.xmusic.ApplicationClass.CHANNEL_ID_2;
import static com.hiten_khatri.xmusic.MainActivity.songs1;
import static com.hiten_khatri.xmusic.PlaySong.currentSongList;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.songs;
import static com.hiten_khatri.xmusic.PlaySong.swipeDismiss;

public class MusicService extends Service implements MediaPlayer.OnCompletionListener{

    ActionPlaying actionPlaying;
    IBinder mBinder=new MyBinder();
    public static MediaPlayer mediaPlayer;
    ArrayList<MusicFiles> musicFiles=new ArrayList<>();
    Uri uri;
    int position=-1;
    MediaSessionCompat mediaSessionCompat;
    public static final String MUSIC_FILE_LAST_PLAYED="LAST_PLAYED";
    public static final String MUSIC_FILE="STORED_MUSIC";
    public static final String ARTIST_NAME="ARTIST_NAME";
    public static final String SONG_NAME="SONG_NAME";
    public static final String CURRENT_SONG_LIST="CST";

    @Override
    public void onCreate() {
        super.onCreate();
        mediaSessionCompat=new MediaSessionCompat(getBaseContext(),"My Audio");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class MyBinder extends Binder {
        MusicService getService()
        {
            return MusicService.this;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        int myPosition=intent.getIntExtra("servicePosition",-1);
        String actionName=intent.getStringExtra("ActionName");
        if(myPosition!=-1)
        {
            playMedia(myPosition);
        }
        if(actionName!=null) {
            switch (actionName) {
                case "pause":
                    if (actionPlaying != null) {

                        actionPlaying.pauseFunc();
                    }
                    break;
                case "next":
                    if (actionPlaying != null) {
                        actionPlaying.nextFunc();
                    }
                    break;
                case "previous":
                    if (actionPlaying != null) {
                        actionPlaying.prevFunc();
                    }
                    break;
                case "delete":
                    stopForeground(true);
                    musicService.stop();
                    musicService.release();
                    stopSelf();
                    System.exit(0);
                    return START_NOT_STICKY;
            }
        }

        return START_STICKY;
    }

    private void playMedia(int startPosition) {

        if(songs!=null) {
            musicFiles = songs;
        }
        else
        {
            musicFiles=songs1;
        }
        position=startPosition;
        if(mediaPlayer!=null)
        {
            mediaPlayer.stop();
            mediaPlayer.release();
            if(musicFiles!=null)
            {
                createMediaPlayer(position);
                mediaPlayer.start();
            }
        }
        else
        {
            createMediaPlayer(position);
            mediaPlayer.start();
        }
    }

    void start()
    {
        mediaPlayer.start();
    }
    boolean isPlaying()
    {
        return mediaPlayer.isPlaying();
    }
    void stop()
    {
        mediaPlayer.stop();
    }
    void pause()
    {
        mediaPlayer.pause();
    }
    void release()
    {
        mediaPlayer.release();
    }
    int getDuration()
    {
        return mediaPlayer.getDuration();
    }
    void seekTo(int position)
    {
        mediaPlayer.seekTo(position);
    }
    int getAudioSessionId()
    {
        return mediaPlayer.getAudioSessionId();
    }
    int getCurrentPosition()
    {
        return mediaPlayer.getCurrentPosition();
    }
    void createMediaPlayer(int position)
    {
        uri=Uri.parse(musicFiles.get(position).getPath());
        mediaPlayer=MediaPlayer.create(getBaseContext(),uri);
        SharedPreferences.Editor editor=getSharedPreferences(MUSIC_FILE_LAST_PLAYED,MODE_PRIVATE).edit();
        editor.putString(MUSIC_FILE,uri.toString());
        editor.putString(ARTIST_NAME,musicFiles.get(position).getArtist());
        editor.putString(SONG_NAME,musicFiles.get(position).getTitle());
        editor.putString(CURRENT_SONG_LIST,currentSongList);
        editor.apply();
    }

    void OnCompleted()
    {
        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(actionPlaying!=null)
        {
            actionPlaying.nextFunc();
        }
    }

    void setCallBack(ActionPlaying actionPlaying)
    {
        this.actionPlaying=actionPlaying;
    }

    void showNotification(int pauseBtn,int position)
    {
        Intent intent=new Intent(this,NotificationReceiver.class).setAction(ACTION_PLAY);
        Intent intent2=new Intent(this,NotificationReceiver.class).setAction(ACTION_DELETE);
        PendingIntent pendingIntent1=PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        PendingIntent pendingIntent2=PendingIntent.getBroadcast(this,0,intent2,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent pintent=new Intent(this,NotificationReceiver.class).setAction(ACTION_PREVIOUS);
        PendingIntent previousIntent1=PendingIntent.getBroadcast(this,0,pintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nintent=new Intent(this,NotificationReceiver.class).setAction(ACTION_NEXT);
        PendingIntent nextIntent1=PendingIntent.getBroadcast(this,0,nintent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent resultPendingIntent = PendingIntent.getActivity(this, 0,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        byte[] picture=null;
        picture=getAlbumArt(musicFiles.get(position).getPath());
        Bitmap thumb=null;
        if(picture!=null)
        {
            thumb= BitmapFactory.decodeByteArray(picture,0,picture.length);
        }
        else
        {
            thumb=BitmapFactory.decodeResource(getResources(),R.drawable.music1);
        }
        Notification notification=new NotificationCompat.Builder(this,CHANNEL_ID_2).setSmallIcon(pauseBtn).setLargeIcon(thumb).setContentTitle(musicFiles.get(position).getTitle()).setContentText(musicFiles.get(position).getArtist()).addAction(R.drawable.previous,"Previous",previousIntent1).addAction(pauseBtn,"Pause",pendingIntent1).addAction(R.drawable.next,"Next",nextIntent1).addAction(R.drawable.search_close,"Close",pendingIntent2).setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSessionCompat.getSessionToken())).setPriority(NotificationCompat.PRIORITY_HIGH).setOnlyAlertOnce(true).setDeleteIntent(pendingIntent2).setVisibility(NotificationCompat.VISIBILITY_PUBLIC).setContentIntent(resultPendingIntent).build();

        startForeground(2,notification);
        if(swipeDismiss) {
            stopForeground(false);
        }



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