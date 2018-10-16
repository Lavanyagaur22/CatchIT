package com.codingblocks.device_admin;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity1 extends AppCompatActivity {
    TextToSpeech t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main1);
        ImageView imageView=findViewById(R.id.splashImg);

        TextView textView=findViewById(R.id.pokego);


        String s= "In Your Service...SO let's start to SPY WITH MY LITTLE EYE ";
        SpannableString ss1=  new SpannableString(s);
        ss1.setSpan(new RelativeSizeSpan(0.8f), 0,15, 0); // set size
        ss1.setSpan(new ForegroundColorSpan(Color.RED), 0, 5, 0);// set color

        textView.setText(ss1);



        Animation zoomanimation= AnimationUtils.loadAnimation(this,R.anim.zoompoke);
        textView.startAnimation(zoomanimation);
        LinearLayout ll=findViewById(R.id.ll);
//        ImageView bl=findViewById(R.id.pokebl);
//        Animation zoomanimation1= AnimationUtils.loadAnimation(this,R.anim.zoompoke);
//        bl.startAnimation(zoomanimation1);

        Animation animation= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade);
        imageView.startAnimation(animation);
        Thread timer=new Thread(){


            @Override
            public void run() {

                try {
                    sleep(7000);
                    Intent intent=new Intent(getApplicationContext(),WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                    super.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        };
        timer.start();


        t1=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status!= TextToSpeech.ERROR){
                    t1.setLanguage(Locale.UK);
                }

            }
        });

        ll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String toSpeak="So let's start to spy for protection !";
                t1.speak(toSpeak, TextToSpeech.QUEUE_FLUSH,null);
            }
        });





    }


}
