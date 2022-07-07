package com.hiten_khatri.xmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.interfaces.ItemChangeListener;
import com.denzcoskun.imageslider.models.SlideModel;
import com.denzcoskun.imageslider.constants.ScaleTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.hiten_khatri.xmusic.PlaySong.musicService;

public class StartupActivity extends AppCompatActivity {

    ImageSlider imageSlider;
    public Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startup);

        imageSlider=findViewById(R.id.image_slider);
        button=findViewById(R.id.button6);

        List<SlideModel> slideModels = new ArrayList<>();

        slideModels.add(new SlideModel(R.drawable.intro_1,"",ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_2,"",ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_3,"",ScaleTypes.FIT));
        slideModels.add(new SlideModel(R.drawable.intro_4,"",ScaleTypes.FIT));

        imageSlider.setImageList(slideModels,ScaleTypes.FIT);

        imageSlider.setItemChangeListener(new ItemChangeListener() {
            @Override
            public void onItemChanged(int i) {
                if(i==3)
                {
                    button.setText("Go");
                }
                else
                {
                    button.setText("Next");
                }
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageSlider.startSliding(140);
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        imageSlider.stopSliding();
                    }
                }, 150);
                
                if(button.getText().toString().equals("Go"))
                {
                    finish();
                    Intent intent=new Intent(StartupActivity.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });

    }
}