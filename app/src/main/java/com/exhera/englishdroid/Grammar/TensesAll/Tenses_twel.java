package com.exhera.englishdroid.Grammar.TensesAll;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Tenses_twel extends AppCompatActivity {

    private LinearLayout presentL,pastL,futureL;
    private LinearLayout presentContiL,pastContiL,futureContiL;
    private LinearLayout presentPerfectL,pastPerfectL,futurePerfectL;
    private LinearLayout presentPerfectContiL,pastPerfectContiL,futurePerfectContiL;

    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_tenses_twel);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_tenses12);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        presentL= findViewById(R.id.simplePresentLayout);
        pastL = findViewById(R.id.simplePastLayout);
        futureL= findViewById(R.id.simpleFutureLayout);

        presentContiL= findViewById(R.id.presentContinuousLayout);
        pastContiL= findViewById(R.id.pastContiLayout);
        futureContiL=findViewById(R.id.futureContiLayout);

        presentPerfectL=findViewById(R.id.presentPerfectLayout);
        pastPerfectL= findViewById(R.id.pastPerfectLayout);
        futurePerfectL = findViewById(R.id.futurePerfectLayout);

        presentPerfectContiL= findViewById(R.id.presentPerfectContiLayout);
        pastPerfectContiL= findViewById(R.id.pastPerfectContiLayout);
        futurePerfectContiL= findViewById(R.id.futurePerfectContiLayout);


        Bundle bundle= getIntent().getExtras();
        if (bundle!=null){
            if (bundle.getInt("SIMPLE")==1){
                simplePresentSet();
            }
            if (bundle.getInt("SIMPLE")==2){
                simplePastSet();
            }
            if (bundle.getInt("SIMPLE")==3){
                simpleFutureSet();
            }




            if (bundle.getInt("SIMPLE")==4){
                presentContiL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==5){
                pastContiL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==6){
                futureContiL.setVisibility(View.VISIBLE);
            }


            if (bundle.getInt("SIMPLE")==7){
                presentPerfectL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==8){
                pastPerfectL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==9){
                futurePerfectL.setVisibility(View.VISIBLE);
            }


            if (bundle.getInt("SIMPLE")==10){
                presentPerfectContiL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==11){
                pastPerfectContiL.setVisibility(View.VISIBLE);
            }

            if (bundle.getInt("SIMPLE")==12){
                futurePerfectContiL.setVisibility(View.VISIBLE);
            }



        }
    }

    private void simpleFutureSet() {
        System.out.println(2);
        futureL.setVisibility(View.VISIBLE);
    }

    private void simplePastSet() {
        System.out.println(1);
        pastL.setVisibility(View.VISIBLE);
    }

    private void simplePresentSet() {
        System.out.println(0);
        presentL.setVisibility(View.VISIBLE);
    }
}