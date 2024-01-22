package com.exhera.englishdroid.Dictionary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class Dictionary_view extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<Dictionary> dictionaryList;
    private RecyclerView recyclerView;
    private DictionaryAdapter dictionaryAdapter;
    private ImageButton addButton;
    private ProgressBar progressBar;
    TextView empty_txt;

    AdView mAdView;

    private CollectionReference collectionReference = db.collection("Dictionary");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_dictioanry_view);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_dicView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        addButton=findViewById(R.id.imageButton_add);
        progressBar=findViewById(R.id.progressBar_view);
        empty_txt=findViewById(R.id.emptyText);

        dictionaryList = new ArrayList<>();
        recyclerView= findViewById(R.id.recyclerView_dictionary);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                addWord();

            }
        });

    }

    public void addWord() {

        startActivity(new Intent(Dictionary_view.this,Dictionary_add.class));
        finish();
    }


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        collectionReference.whereEqualTo("userId", JournalApi.getInstance().getUserId()).orderBy("word", Query.Direction.ASCENDING)
        .get()
        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onSuccess(QuerySnapshot querySnapshot) {

                if (!querySnapshot.isEmpty()){
                    for (QueryDocumentSnapshot dics : querySnapshot){
                        Dictionary dictionary =dics.toObject(Dictionary.class);
                        dictionaryList.add(dictionary);

                    }

                        progressBar.setVisibility(View.INVISIBLE);

                    dictionaryAdapter = new DictionaryAdapter(Dictionary_view.this, dictionaryList, new DictionaryAdapter.ItemClickListener() {
                        @Override
                        public void onItemClick(Dictionary dictionary) {

//                          Alert box creating
                            AlertDialog.Builder builder = new AlertDialog.Builder(Dictionary_view.this);
                            builder.setTitle(dictionary.getWord());
                            builder.setMessage(dictionary.getDescription());
                            builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.setNeutralButton("Update", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    showToas(dictionary.getWord()+dictionary.getUserId());
                                    String uid=dictionary.getUserId();
                                    String word=dictionary.getWord();
                                    String description=dictionary.getDescription();
                                    String time= dictionary.getTimeAdded();
                                    Intent intent=new Intent(Dictionary_view.this,Dictionary_update.class);
                                    intent.putExtra("ID",uid);
                                    intent.putExtra("WORD",word);
                                    intent.putExtra("DESCRIPTION",description);
                                    intent.putExtra("TIME",time);
                                    startActivity(intent);
                                    finish();

                                }
                            });
                            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String time= dictionary.getTimeAdded();
                                    collectionReference.document(time).delete();

                                    dialogInterface.cancel();

                                    finish();
                                    startActivity(getIntent());

                                }
                            });
                            builder.show();

                        }
                    });
                    recyclerView.setAdapter(dictionaryAdapter);
                    dictionaryAdapter.notifyDataSetChanged();

                }else {
                    progressBar.setVisibility(View.INVISIBLE);
                    empty_txt.setVisibility(View.VISIBLE);
                }

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void showToas(String msg){

        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}