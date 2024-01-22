package com.exhera.englishdroid.Dictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Locale;

public class Dictionary_update extends AppCompatActivity {

    String upId,upWord, upDescription,time;
    EditText word_update_editTxt,description_update_editTxt;
    protected static final int RESULT_SPEECH=1;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;

    private ImageButton btnSpeak_word;
    private ImageButton btnSpeak_description;
    private int check;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final CollectionReference collectionReference = db.collection("Dictionary");
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_dictionary_update);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_upWord);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        word_update_editTxt= findViewById(R.id.word_update);
        description_update_editTxt= findViewById(R.id.description_update);

        btnSpeak_word = findViewById(R.id.word_speek_dictionary_update);
        btnSpeak_description = findViewById(R.id.description_speek_dictionary_update);


        firebaseAuth = FirebaseAuth.getInstance();

        ActionBar actionBar = getSupportActionBar();

        Bundle bundle= getIntent().getExtras();
        if (bundle  != null){
            actionBar.setTitle("Update My Dictionary");
            upId=bundle.getString("ID");
            upWord=bundle.getString("WORD");
            upDescription= bundle.getString("DESCRIPTION");
            time=bundle.getString("TIME");

            word_update_editTxt.setText(upWord);
            description_update_editTxt.setText(upDescription);


        }else {

        }



        btnSpeak_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try {
                    startActivityForResult(intent,RESULT_SPEECH);
                    word_update_editTxt.setText("");
                } catch (Exception e) {
                    Toast.makeText(Dictionary_update.this, "Your Device does not support", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

                check=0;
            }
        });

        btnSpeak_description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check=1;

                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try {
                    startActivityForResult(intent,RESULT_SPEECH);
                    description_update_editTxt.setText("");
                } catch (Exception e) {
                    Toast.makeText(Dictionary_update.this, "Your Device does not support", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });




    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode==RESULT_OK && data !=null){
                    ArrayList<String> text= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (check==0){
                        word_update_editTxt.setText(text.get(0));
                    }
                    else if (check==1){
                        description_update_editTxt.setText(text.get(0));

                    }


                }
                break;
        }
    }

    private void  updateData(String word, String description,String time){
        collectionReference.document(time).update("word",word,"description",description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(Dictionary_update.this,Dictionary_view.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }

    public void update(View view) {
    updateData(word_update_editTxt.getText().toString().trim().toLowerCase(Locale.ROOT),description_update_editTxt.getText().toString().trim(),time);

    }

}