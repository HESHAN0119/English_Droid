package com.exhera.englishdroid.Grammar.VerbAll;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Verb_be extends AppCompatActivity {

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_verb_be);


        ImageView arrowBe_2=findViewById(R.id.arrow_be2);
        ImageView principle_img=findViewById(R.id.principleImage);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_verb_be);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        arrowBe_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Verb_be.this,Verb_be_two.class));
            }
        });


    }
}