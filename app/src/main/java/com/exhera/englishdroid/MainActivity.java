package com.exhera.englishdroid;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.exhera.englishdroid.R;
import com.exhera.englishdroid.util.JournalApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class MainActivity extends AppCompatActivity {

    private CardView startCard;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    private FirebaseFirestore db =  FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");
    AdView mAdView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_main);

        startCard  = findViewById(R.id.cardView_start);

        startCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });



        firebaseAuth = FirebaseAuth.getInstance();






        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();
                if (currentUser != null) {
                    currentUser = firebaseAuth.getCurrentUser();
                    final String currentUserId = currentUser.getUid();

                    collectionReference
                            .whereEqualTo("userId", currentUserId)
                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                @Override
                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                    @Nullable FirebaseFirestoreException e) {
                                    if (e != null) {
                                        return;
                                    }

                                    String name;

                                    if (!queryDocumentSnapshots.isEmpty()) {
                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                            JournalApi journalApi = JournalApi.getInstance();
                                            journalApi.setUserId(snapshot.getString("userId"));
                                            journalApi.setUsername(snapshot.getString("username"));

                                            startActivity(new Intent(MainActivity.this,
                                                    HomeActivity.class));
                                            finish();


                                        }
                                    }

                                }
                            });

                }else {

                }
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_main);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }


}