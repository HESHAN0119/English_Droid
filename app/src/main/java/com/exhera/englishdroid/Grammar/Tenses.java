package com.exhera.englishdroid.Grammar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.exhera.englishdroid.Grammar.TensesAll.Tenses_twel;
import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

public class Tenses extends AppCompatActivity implements View.OnClickListener {

    int simple;
    CardView simplePast,simplePresent,simpleFuture;
    CardView presentConti,pastConti,futureConti;
    CardView presentPerfect, pastPerfect, futurePerfect;
    CardView presentPerfectConti,pastPerfectConti,futurePerfectConti;
AdView mAdView,mAdView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_tenses);





        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView2 = findViewById(R.id.adView_tenses2);
        AdRequest adRequest1 = new AdRequest.Builder().build();
        mAdView2.loadAd(adRequest1);




        simplePresent= findViewById(R.id.simplePresentCard);
        simplePresent.setOnClickListener(this);

        simplePast= findViewById(R.id.simplePastCard);
        simplePast.setOnClickListener(this);

        simpleFuture= findViewById(R.id.simpleFutureCard);
        simpleFuture.setOnClickListener(this);



        presentConti= findViewById(R.id.presentContiCard);
        presentConti.setOnClickListener(this);

        pastConti=findViewById(R.id.pastContiCard);
        pastConti.setOnClickListener(this);

        futureConti= findViewById(R.id.futureContiCard);
        futureConti.setOnClickListener(this);


        presentPerfect=findViewById(R.id.presentPerfectCard);
        presentPerfect.setOnClickListener(this);

        pastPerfect = findViewById(R.id.pastPerfectCard);
        pastPerfect.setOnClickListener(this);

        futurePerfect= findViewById(R.id.futurePerfectCard);
        futurePerfect.setOnClickListener(this);



        presentPerfectConti= findViewById(R.id.presentPerfectContiCard);
        presentPerfectConti.setOnClickListener(this);

        pastPerfectConti= findViewById(R.id.pastPerfectContiCard);
        pastPerfectConti.setOnClickListener(this);

        futurePerfectConti= findViewById(R.id.futurePerfectContiCard);
        futurePerfectConti.setOnClickListener(this);








    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.simplePresentCard:
                simple=1;
                break;

            case R.id.simplePastCard:
                simple=2;
                break;

            case R.id.simpleFutureCard:
                simple=3;
                break;


            case R.id.presentContiCard:
                simple=4;
                break;

            case R.id.pastContiCard:
                simple=5;
                break;

            case R.id.futureContiCard:
                simple=6;
                break;



            case R.id.presentPerfectCard:
                simple=7;
                break;

            case R.id.pastPerfectCard:
                simple=8;
                break;

            case R.id.futurePerfectCard:
                simple=9;
                break;


            case R.id.presentPerfectContiCard:
                simple=10;
                break;

            case R.id.pastPerfectContiCard:
                simple=11;
                break;

            case R.id.futurePerfectContiCard:
                simple=12;
                break;




        }

        Intent intent= new Intent(Tenses.this, Tenses_twel.class);
        intent.putExtra("SIMPLE",simple);
        startActivity(intent);

    }
}