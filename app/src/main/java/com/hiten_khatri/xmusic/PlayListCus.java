package com.hiten_khatri.xmusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Random;

import static com.hiten_khatri.xmusic.MainActivity.musicFilesArrayList;
import static com.hiten_khatri.xmusic.MainActivity.songList;
import static com.hiten_khatri.xmusic.PlayList_.play_list;
import static com.hiten_khatri.xmusic.PlayList_.play_list1;

public class PlayListCus extends ArrayAdapter<ArrayList<String>> {

    public TextView textView;
    public ArrayList<String> arr;
    public Context context;
    public ImageView imageView;
    public static ArrayList<MusicFiles> playAll;
    public static ArrayList<MusicFiles> playAll1;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;

    public PlayListCus(@NonNull Context context, int resource, ArrayList arr) {
        super(context, resource,arr);
        this.arr=arr;
        this.context=context;
    }

    @SuppressLint({"ViewHolder", "CommitPrefEdits", "SetTextI18n", "NonConstantResourceId"})
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        convertView= LayoutInflater.from(getContext()).inflate(R.layout.cus_play_list,parent,false);
        textView=convertView.findViewById(R.id.textView1);
        imageView=convertView.findViewById(R.id.imageView8);
        textView.setText(arr.get(position));

        sharedPreferences=getContext().getSharedPreferences("playLists",Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        gson=new Gson();
        if(play_list1!=null && !play_list1.equals("[]")) {
            SharedPreferences sharedPreferences1 = getContext().getSharedPreferences(play_list.get(position), Context.MODE_PRIVATE);
            String string = sharedPreferences1.getString(play_list.get(position), null);

            convertView.setOnClickListener(v -> {
                if (string != null) {
                    ArrayList<String> playListSongs = gson.fromJson(string, new TypeToken<ArrayList<String>>() {}.getType());
                    Intent intent = new Intent(getContext(), PlayListSongList.class);
                    intent.putExtra("playListName", play_list.get(position));
                    intent.putStringArrayListExtra("playListSongs", playListSongs);
                    getContext().startActivity(intent);
                } else {
                    ArrayList<String> temp1 = new ArrayList<>();
                    temp1.add("No songs added in this playlist!");
                    Intent intent = new Intent(getContext(), PlayListSongList.class);
                    intent.putExtra("playListName", play_list.get(position));
                    intent.putStringArrayListExtra("playListSongs", temp1);
                    getContext().startActivity(intent);
                }
            });

            if (string != null) {
                imageView.setOnClickListener(v -> {
                    PopupMenu popupMenu = new PopupMenu(getContext(), v);
                    popupMenu.getMenuInflater().inflate(R.menu.menu3, popupMenu.getMenu());
                    popupMenu.show();
                    popupMenu.setOnMenuItemClickListener(item -> {

                        int id = item.getItemId();
                        switch (id) {
                            case R.id.menu1:
                                removeFav(position);
                                notifyDataSetChanged();

                                return true;
                            case R.id.menu2:
                                ArrayList<String> playListSongs = gson.fromJson(string, new TypeToken<ArrayList<String>>() {
                                }.getType());
                                SharedPreferences.Editor editor1=getContext().getSharedPreferences("LAST_PLAYED",Context.MODE_PRIVATE).edit();
                                editor1.putString("PLAYLIST_NAME",play_list.get(position));
                                playAll = new ArrayList<>();
                                for (int i = 0; i < playListSongs.size(); i++) {
                                    playAll.add(musicFilesArrayList.get(songList.indexOf(playListSongs.get(i))));
                                }
                                Intent intent = new Intent(getContext(), PlaySong.class);
                                intent.putExtra("position", 0);
                                intent.putExtra("currentSong", playListSongs.get(0));
                                intent.putExtra("songList", "playAll");
                                getContext().startActivity(intent);

                                return true;
                            case R.id.menu3:
                                ArrayList<String> playListSongs1 = gson.fromJson(string, new TypeToken<ArrayList<String>>() {
                                }.getType());
                                playAll1 = new ArrayList<>();
                                for (int i = 0; i < playListSongs1.size(); i++) {
                                    playAll1.add(musicFilesArrayList.get(songList.indexOf(playListSongs1.get(i))));
                                }
                                Random random = new Random();
                                int rpos = random.nextInt(playAll1.size());
                                Intent intent1 = new Intent(getContext(), PlaySong.class);
                                intent1.putExtra("position", rpos);
                                intent1.putExtra("currentSong", playListSongs1.get(rpos));
                                intent1.putExtra("songList", "playAll1");
                                getContext().startActivity(intent1);

                                return true;
                        }

                        return true;
                    });
                });
            }
        }
        else
        {
            convertView.setOnClickListener(v -> Toast.makeText(getContext(), "Create a playlist first!", Toast.LENGTH_SHORT).show());
            imageView.setOnClickListener(v -> {});
        }

        return convertView;
    }

    public void removeFav(int position)
    {
        Toast.makeText(getContext(), "Playlist Removed", Toast.LENGTH_SHORT).show();
        play_list.remove(position);
        String favList21 = gson.toJson(play_list);
        editor.putString("playLists", favList21);
        editor.apply();
        arr.clear();
        arr.addAll(play_list);
    }
}