package com.exhera.englishdroid.FindPP;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class FindPp extends AppCompatActivity {

    private TextInputEditText presentWord,pastWord,ppWord;
    private Button searchBtn, clearBtn ;
    private ProgressBar progressBar;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private List<PP> PpList;
    private CollectionReference collectionReference = db.collection("FindPp");
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_find_pp);

        presentWord= findViewById(R.id.idPresentEditText);
        pastWord= findViewById(R.id.idPastEditText);
        ppWord = findViewById(R.id.idPpEditText);
        progressBar=findViewById(R.id.progressBar_pp);

        searchBtn = findViewById(R.id.searchPpButton);
        clearBtn= findViewById(R.id.clearButton);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_findPp);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        clearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presentWord.setText(null);
                pastWord.setText(null);
                ppWord.setText(null);
            }
        });

        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String present_word= presentWord.getText().toString().trim().toLowerCase();
                String past_word= pastWord.getText().toString().trim().toLowerCase();
                String pp_word= ppWord.getText().toString().trim().toLowerCase();

                if (!TextUtils.isEmpty(present_word)){
                    searchWord("present",present_word);
                }
                else if (!TextUtils.isEmpty(past_word)){
//                    searchWord("past",past_word);
                    Toast.makeText(FindPp.this, "Please input the present word", Toast.LENGTH_SHORT).show();
                }

                else if (!TextUtils.isEmpty(pp_word)){
//                    searchWord("pastP",pp_word);
                    Toast.makeText(FindPp.this, "Please input the present word", Toast.LENGTH_SHORT).show();
                }


            }
        });




    }

    private void searchWord(String time, String word) {
        progressBar.setVisibility(View.VISIBLE);
        collectionReference.whereEqualTo(time,word).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                if (!querySnapshot.isEmpty()){
                    for (QueryDocumentSnapshot pps : querySnapshot){
                        PP pp =pps.toObject(PP.class);
                        presentWord.setText(pp.getPresent());
                        pastWord.setText(pp.getPast());
                        ppWord.setText(pp.getPastP());
                    }
                }else {
                    presentWord.setText(word);
                    pastWord.setText(word+"ed");
                    ppWord.setText(word+"ed");

                }

                progressBar.setVisibility(View.INVISIBLE);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                System.out.println(e);
            }
        });

    }



}