package com.exhera.englishdroid.Dictionary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;

import java.util.ArrayList;
import java.util.Calendar;

import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.exhera.englishdroid.R;
import com.exhera.englishdroid.util.JournalApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Date;
import java.util.Locale;

public class Dictionary_add extends AppCompatActivity {


    protected static final int RESULT_SPEECH=1;
    private Button saveButton;
    private EditText wordEditText;
    private EditText descriptionEditText;
    private ImageButton btnSpeak_word,getDataWord,getDataMeaning;
    private ImageButton btnSpeak_description;
    private static final String TAG = "Dictionary...............";
    private String currentUserId;
    private String currentUserName;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;

    private int check;

    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
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

        setContentView(R.layout.activity_dictionary_add);
        storageReference = FirebaseStorage.getInstance().getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        wordEditText = findViewById(R.id.word_add_editText);
        descriptionEditText = findViewById(R.id.description_add_edittext);
        saveButton=findViewById(R.id.buttpn_Add);
        btnSpeak_word = findViewById(R.id.word_speek_dictionary);
        btnSpeak_description = findViewById(R.id.description_speek_dictionary);
        getDataWord= findViewById(R.id.get_data_word);
        getDataMeaning= findViewById(R.id.get_data_meaning);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_addWord);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            String gotData=bundle.getString("TEXT");
            getDataMeaning.setVisibility(View.VISIBLE);
            getDataWord.setVisibility(View.VISIBLE);
            getDataWord.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    wordEditText.setText(gotData);
                }
            });


            getDataMeaning.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    descriptionEditText.setText(gotData);
                }
            });

        }



        btnSpeak_word.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,"en-US");
                try {
                    startActivityForResult(intent,RESULT_SPEECH);
                    wordEditText.setText("");
                } catch (Exception e) {
                    Toast.makeText(Dictionary_add.this, "Your Device does not support", Toast.LENGTH_SHORT).show();
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
                    descriptionEditText.setText("");
                } catch (Exception e) {
                    Toast.makeText(Dictionary_add.this, "Your Device does not support", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }

            }
        });









        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveDictionary();
            }
        });


        if (JournalApi.getInstance() != null) {
            currentUserId = JournalApi.getInstance().getUserId();
            currentUserName = JournalApi.getInstance().getUsername();



        }




        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = firebaseAuth.getCurrentUser();
                if (user != null) {

                } else {

                }

            }
        };


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case RESULT_SPEECH:
                if (resultCode==RESULT_OK && data !=null){
                    ArrayList<String> text= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    if (check==0){
                        wordEditText.setText(text.get(0));
                    }
                    else if (check==1){
                        descriptionEditText.setText(text.get(0));

                    }


                }
                break;
        }
    }

    private  void saveDictionary(){

        final String word = wordEditText.getText().toString().trim().toLowerCase(Locale.ROOT);
        final String description = descriptionEditText.getText().toString().trim();

        if (!TextUtils.isEmpty(word) &&
                !TextUtils.isEmpty(description)) {

            final Date currentTime = Calendar.getInstance().getTime();
            final String time= currentTime.toString();

            Dictionary dictionary= new Dictionary();
            dictionary.setUserId(currentUserId);
            dictionary.setUserName(currentUserName);
            dictionary.setWord(word);
            dictionary.setTimeAdded(time);
            dictionary.setDescription(description);



            collectionReference.document(time).set(dictionary).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    startActivity(new Intent(Dictionary_add.this,
                                    Dictionary_view.class));
                            finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//
////                            progressBar.setVisibility(View.INVISIBLE);
//                            startActivity(new Intent(Dictionary_add.this,
//                                    Dictionary_view.class));
//                            finish();
//                        }
//                    })
//                    .addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.d(TAG, "onFailure: " + e.getMessage());
//
//                        }
//                    });

        }




        }

    @Override
    protected void onStart() {
        super.onStart();
        user = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (firebaseAuth != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
    }



}