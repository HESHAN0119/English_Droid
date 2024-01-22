package com.exhera.englishdroid.PassiveVoice;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Passive_Voice extends AppCompatActivity {

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_passive_voice);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_passive);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}