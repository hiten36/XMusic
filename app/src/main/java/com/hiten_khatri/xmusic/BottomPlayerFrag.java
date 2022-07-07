package com.hiten_khatri.xmusic;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;

import java.util.Random;

import static com.hiten_khatri.xmusic.AlbumDetailsCus.playNext1;
import static com.hiten_khatri.xmusic.AlbumDetailsCus.queueList1;
import static com.hiten_khatri.xmusic.ArtistDetailsCus.queueList4;
import static com.hiten_khatri.xmusic.MainActivity.PATH_TO_FRAG;
import static com.hiten_khatri.xmusic.MainActivity.SHOW_MINI_PLAYER;
import static com.hiten_khatri.xmusic.MainActivity.SONG_ARTIST1;
import static com.hiten_khatri.xmusic.MainActivity.SONG_NAME1;
import static com.hiten_khatri.xmusic.MainActivity.currentSong1;
import static com.hiten_khatri.xmusic.MainActivity.posi;
import static com.hiten_khatri.xmusic.MainActivity.songs1;
import static com.hiten_khatri.xmusic.MusicService.mediaPlayer;
import static com.hiten_khatri.xmusic.PlayListSongListCus.playNext2;
import static com.hiten_khatri.xmusic.PlayListSongListCus.queueList2;
import static com.hiten_khatri.xmusic.PlaySong.swipeDismiss;
import static com.hiten_khatri.xmusic.PlaySong.flag1;
import static com.hiten_khatri.xmusic.PlaySong.musicService;
import static com.hiten_khatri.xmusic.PlaySong.position;
import static com.hiten_khatri.xmusic.PlaySong.queuePos;
import static com.hiten_khatri.xmusic.PlaySong.repeat_bool;
import static com.hiten_khatri.xmusic.PlaySong.shuffle_bool;
import static com.hiten_khatri.xmusic.recCusAda.playNext3;
import static com.hiten_khatri.xmusic.recCusAda.queueList3;
import static com.hiten_khatri.xmusic.songAdapter.ViewHolder.playNext;
import static com.hiten_khatri.xmusic.songAdapter.ViewHolder.queueList;

public class BottomPlayerFrag extends Fragment implements ServiceConnection {

    FrameLayout frameLayout;
    ImageView miniImg;
    ImageView miniPrev;
    ImageView miniPause;
    ImageView miniNext;
    TextView miniSong;
    TextView miniArtist;
    View view;
    public boolean flag=false;

    public BottomPlayerFrag() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_bottom_player, container, false);

        miniImg=view.findViewById(R.id.mini_img);
        miniPrev=view.findViewById(R.id.mini_prev);
        miniPause=view.findViewById(R.id.mini_pause);
        miniNext=view.findViewById(R.id.mini_next);
        miniSong=view.findViewById(R.id.mini_song);
        miniArtist=view.findViewById(R.id.mini_artist);
        frameLayout=view.findViewById(R.id.frameLayout);

        miniSong.setSelected(true);
        miniArtist.setSelected(true);
        
        miniPause.setOnClickListener(v -> {
            if(musicService!=null)
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
                    musicService.pause();
                    position=posi;
                    musicService.showNotification(R.drawable.play,posi);
                    miniPause.setImageResource(R.drawable.play);
                }
                else
                {
                    musicService.start();
                    position=posi;
                    musicService.showNotification(R.drawable.pause,posi);
                    miniPause.setImageResource(R.drawable.pause);
                }
            }
            else
            {
                if(mediaPlayer==null) {
                    flag=true;
                    Intent intent1 = new Intent(getContext(), MusicService.class);
                    intent1.putExtra("servicePosition", posi);
                    requireContext().startService(intent1);
                    String textContent1 = songs1.get(posi).getTitle();
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    position=posi;
                }
                else
                {
                    if(flag)
                    {
                        flag=false;
                        miniPause.setImageResource(R.drawable.play);
                        mediaPlayer.pause();
                        position=posi;
                    }
                    else
                    {
                        flag=true;
                        miniPause.setImageResource(R.drawable.pause);
                        mediaPlayer.start();
                        position=posi;
                    }
                }
            }
        });
        miniPrev.setOnClickListener(v -> {
            if(musicService!=null)
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
                    musicService.createMediaPlayer(posi);
                    musicService.start();
                    String textContent1 = songs1.get(posi).getTitle();
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    position=posi;
                    musicService.showNotification(R.drawable.pause,posi);

                }

                else if(shuffle_bool)
                {
                    musicService.stop();
                    musicService.release();
                    Random random=new Random();
                    int rpos=random.nextInt(songs1.size());
                    updatePosition(rpos);
                    musicService.start();
                    String textContent1 = songs1.get(rpos).getTitle();
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,rpos);
                    posi=rpos;
                    position=posi;
                    musicService.createMediaPlayer(rpos);
                }

                else {
                    musicService.stop();
                    musicService.release();
                    if (posi != 0) {
                        posi = posi - 1;
                    } else {
                        posi = songs1.size() - 1;
                    }
                    position=posi;
                    musicService.createMediaPlayer(posi);
                    musicService.start();
                    String textContent1 = songs1.get(posi).getTitle();
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,posi);
                }
            }
            else
            {
                flag=true;
                if (posi != 0) {
                    posi = posi - 1;
                } else {
                    posi = songs1.size() - 1;
                }
                position=posi;
                Intent intent1=new Intent(getContext(),MusicService.class);
                intent1.putExtra("servicePosition",posi);
                getContext().startService(intent1);
                String textContent1 = songs1.get(posi).getTitle();
                miniSong.setText(textContent1);
                miniPause.setImageResource(R.drawable.pause);
//                musicService.showNotification(R.drawable.pause,posi);
            }

            byte[] image3 = getAlbumArt(songs1.get(posi).getPath());
            if (image3 != null) {
                Glide.with(getContext()).load(image3).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
            } else {
                Glide.with(getContext()).load(R.drawable.music1).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
            }
        });
        miniNext.setOnClickListener(v -> {
            if(musicService!=null) {
                flag1=false;
                swipeDismiss=false;
                if(queueList.size()>0 && queueList!=null)
                {
                    if(queuePos<queueList.size()) {
                        musicService.stop();
                        musicService.release();
                        musicService.createMediaPlayer(queueList.get(queuePos));
                        musicService.start();
                        String textContent1 = songs1.get(queueList.get(queuePos)).getTitle();
                        miniSong.setText(textContent1);
                        miniPause.setImageResource(R.drawable.pause);
                        musicService.showNotification(R.drawable.pause,queueList.get(queuePos));
                        posi = queueList.get(queuePos);
                        position=posi;
                        queuePos++;
                    }
                    if(queuePos==queueList.size())
                    {
                        queuePos=0;
                        queueList.clear();
                    }
                }

                else if(queueList1.size()>0 && queueList1!=null)
                {
                    if(queueList1.size()>0) {
                        if (queuePos < queueList1.size()) {
                            musicService.stop();
                            musicService.release();
                            musicService.createMediaPlayer(queueList1.get(queuePos));
                            Toast.makeText(getContext(), String.valueOf(queueList1.size()), Toast.LENGTH_SHORT).show();
                            musicService.start();
                            String textContent1 = songs1.get(queueList1.get(queuePos)).getTitle();
                            miniSong.setText(textContent1);
                            miniPause.setImageResource(R.drawable.pause);
                            musicService.showNotification(R.drawable.pause,queueList1.get(queuePos));
                            posi = queueList1.get(queuePos);
                            position=posi;
                            queuePos++;
                        }
                    }
                    if(queuePos==queueList1.size())
                    {
                        queuePos=0;
                        queueList1.clear();
                    }
                }

                else if(queueList2.size()>0 && queueList2!=null )
                {
                    if(queueList2.size()>0) {
                        if (queuePos < queueList2.size()) {
                            musicService.stop();
                            musicService.release();
                            musicService.createMediaPlayer(queueList2.get(queuePos));
                            musicService.start();
                            String textContent1 = songs1.get(queueList2.get(queuePos)).getTitle();
                            miniSong.setText(textContent1);
                            miniPause.setImageResource(R.drawable.pause);
                            musicService.showNotification(R.drawable.pause,queueList2.get(queuePos));
                            posi = queueList2.get(queuePos);
                            position=posi;
                            queuePos++;
                        }
                    }
                    if(queuePos==queueList2.size())
                    {
                        queuePos=0;
                        queueList2.clear();
                    }
                }

                else if(queueList3.size()>0 && queueList3!=null )
                {
                    if(queueList3.size()>0) {
                        if (queuePos < queueList3.size()) {
                            musicService.stop();
                            musicService.release();
                            musicService.createMediaPlayer(queueList3.get(queuePos));
                            musicService.start();
                            String textContent1 = songs1.get(queueList3.get(queuePos)).getTitle();
                            miniSong.setText(textContent1);
                            miniPause.setImageResource(R.drawable.pause);
                            musicService.showNotification(R.drawable.pause,queueList3.get(queuePos));
                            posi = queueList3.get(queuePos);
                            position=posi;
                            queuePos++;
                        }
                    }
                    if(queuePos==queueList3.size())
                    {
                        queuePos=0;
                        queueList3.clear();
                    }
                }

                else if(queueList4.size()>0 && queueList4!=null )
                {
                    if(queueList4.size()>0) {
                        if (queuePos < queueList4.size()) {
                            musicService.stop();
                            musicService.release();
                            musicService.createMediaPlayer(queueList4.get(queuePos));
                            musicService.start();
                            String textContent1 = songs1.get(queueList4.get(queuePos)).getTitle();
                            miniSong.setText(textContent1);
                            miniPause.setImageResource(R.drawable.pause);
                            musicService.showNotification(R.drawable.pause,queueList4.get(queuePos));
                            posi = queueList4.get(queuePos);
                            position=posi;
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
                    String textContent1 = songs1.get(Integer.parseInt(playNext)).getTitle();
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,Integer.parseInt(playNext));
                    posi=Integer.parseInt(playNext);
                    playNext="";
                }

                else if(!playNext1.equals(""))
                {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(Integer.parseInt(playNext1));
                    musicService.start();
                    String textContent1 = songs1.get(Integer.parseInt(playNext1)).getTitle();
                    posi=Integer.parseInt(playNext1);
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,posi);
                    playNext1="";
                }

                else if(!playNext2.equals(""))
                {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(Integer.parseInt(playNext2));
                    musicService.start();
                    String textContent1 = songs1.get(Integer.parseInt(playNext2)).getTitle();
                    posi=Integer.parseInt(playNext2);
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,posi);
                    playNext2="";
                }

                else if(!playNext3.equals(""))
                {
                    musicService.stop();
                    musicService.release();
                    musicService.createMediaPlayer(Integer.parseInt(playNext3));
                    musicService.start();
                    String textContent1 = songs1.get(Integer.parseInt(playNext3)).getTitle();
                    posi=Integer.parseInt(playNext3);
                    miniSong.setText(textContent1);
                    miniPause.setImageResource(R.drawable.pause);
                    musicService.showNotification(R.drawable.pause,posi);
                    playNext3="";
                }

                else {

                    if (repeat_bool) {
                        if (shuffle_bool) {
                            shuffle_bool = false;
                        }
                        musicService.stop();
                        musicService.release();
                        musicService.createMediaPlayer(posi);
                        position=posi;
                        musicService.start();
                        String textContent1 = songs1.get(posi).getTitle();
                        miniSong.setText(textContent1);
                        miniPause.setImageResource(R.drawable.pause);
                        musicService.showNotification(R.drawable.pause,posi);
                    } else if (shuffle_bool) {

                        musicService.stop();
                        musicService.release();
                        Random random = new Random();
                        int rpos = random.nextInt(songs1.size());
                        updatePosition(rpos);
                        musicService.createMediaPlayer(rpos);
                        musicService.start();
                        String textContent1 = songs1.get(rpos).getTitle();
                        miniSong.setText(textContent1);
                        miniPause.setImageResource(R.drawable.pause);
                        musicService.showNotification(R.drawable.pause,posi);
                        posi = rpos;
                        position=posi;

                    } else {
                        musicService.stop();
                        musicService.release();
                        if (posi != songs1.size() - 1) {
                            posi = posi + 1;
                        } else {
                            posi = 0;
                        }
                        position=posi;
                        musicService.createMediaPlayer(posi);
                        musicService.start();
                        String textContent1 = songs1.get(posi).getTitle();
                        miniSong.setText(textContent1);
                        miniPause.setImageResource(R.drawable.pause);
                        musicService.showNotification(R.drawable.pause,posi);
                    }
                }
            }
            else
            {
                flag=true;
                if (posi != songs1.size() - 1) {
                    posi = posi + 1;
                } else {
                    posi = 0;
                }
                position=posi;

                Intent intent1=new Intent(getContext(),MusicService.class);
                intent1.putExtra("servicePosition",posi);
                requireContext().startService(intent1);
                String textContent1 = songs1.get(posi).getTitle();
                miniSong.setText(textContent1);
                miniPause.setImageResource(R.drawable.pause);
//                musicService.showNotification(R.drawable.pause,posi);
            }

            byte[] image3 = getAlbumArt(songs1.get(posi).getPath());
            if (image3 != null) {
                Glide.with(requireContext()).load(image3).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
            } else {
                Glide.with(requireContext()).load(R.drawable.music1).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
            }
        });

        miniSong.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            if(musicService!=null)
            {
                intent.putExtra("currentTime",musicService.getCurrentPosition());
            }
            else if(mediaPlayer!=null)
            {

                intent.putExtra("currentTime",mediaPlayer.getCurrentPosition());
            }
            intent.putExtra("songList",currentSong1);
            intent.putExtra("position",posi);
            intent.putExtra("currentSong",miniSong.getText().toString());
            startActivity(intent);
        });
        miniImg.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            if(musicService!=null)
            {
                intent.putExtra("currentTime",musicService.getCurrentPosition());
            }
            else if(mediaPlayer!=null)
            {

                intent.putExtra("currentTime",mediaPlayer.getCurrentPosition());
            }
            intent.putExtra("songList",currentSong1);
            intent.putExtra("position",posi);
            intent.putExtra("currentSong",miniSong.getText().toString());
            startActivity(intent);
        });
        miniArtist.setOnClickListener(v -> {
            Intent intent=new Intent(getContext(),PlaySong.class);
            if(musicService!=null)
            {
                    intent.putExtra("currentTime",musicService.getCurrentPosition());
            }
            else if(mediaPlayer!=null)
            {

                    intent.putExtra("currentTime",mediaPlayer.getCurrentPosition());
            }
            intent.putExtra("songList",currentSong1);
            intent.putExtra("position",posi);
            intent.putExtra("currentSong",miniSong.getText().toString());
            startActivity(intent);
        });

        return view;
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

    @Override
    public void onResume() {
        if(SHOW_MINI_PLAYER)
        {
            frameLayout.setVisibility(View.VISIBLE);
            if(PATH_TO_FRAG!=null) {

                byte[] art = getAlbumArt(PATH_TO_FRAG);
                if(art!=null) {
                    Glide.with(requireContext()).load(art).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
                }
                else
                {
                    Glide.with(requireContext()).load(R.drawable.music1).centerCrop().fitCenter().apply(RequestOptions.bitmapTransform(new RoundedCorners(15))).into(miniImg);
                }
                miniSong.setText(SONG_NAME1);
                miniArtist.setText(SONG_ARTIST1);

                if(musicService!=null)
                {
                    if(musicService.isPlaying() || mediaPlayer.isPlaying())
                    {
                        miniPause.setImageResource(R.drawable.pause);
                    }
                    else
                    {
                        miniPause.setImageResource(R.drawable.play);
                    }
                }
            }
        }
        else
        {
            frameLayout.setVisibility(View.GONE);
        }

        super.onResume();
    }

    public void updatePosition(int position)
    {
        posi=position;
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder myBinder=(MusicService.MyBinder) service;
        musicService=myBinder.getService();
        musicService.showNotification(R.drawable.pause,posi);

    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        musicService=null;
    }
}