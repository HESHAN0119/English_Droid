package com.exhera.englishdroid.ShortNote;

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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ShortNote_view extends AppCompatActivity {

    ImageButton addNoteButton;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db= FirebaseFirestore.getInstance();
    private StorageReference storageReference;
    private List<ShortNote> shortNoteList;
    private RecyclerView recyclerView;
    private ShortNoteRecyclerAdapter shortNoteRecyclerAdapter;
    private ProgressBar progressBar;
    private TextView emptySNText;
    AdView mAdView;

    private CollectionReference collectionReference= db.collection("ShortNote");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_short_note_view);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_shortNoteView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        addNoteButton= findViewById(R.id.add_shortNote_imageButton);
        progressBar= findViewById(R.id.progressBarView);

        emptySNText= findViewById(R.id.emptyShortNoteText);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();

        shortNoteList = new ArrayList<>();

        recyclerView= findViewById(R.id.recyclerView_shortNote);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNoteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(ShortNote_view.this,ShortNote_add.class));
                finish();

            }
        });


    }


    @Override
    protected void onStart() {
        super.onStart();
        progressBar.setVisibility(View.VISIBLE);
        collectionReference.whereEqualTo("userId",JournalApi.getInstance()
                .getUserId()).orderBy("title", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onSuccess(QuerySnapshot querySnapshot) {

                        progressBar.setVisibility(View.INVISIBLE);

                        if ( !querySnapshot.isEmpty()){
                            for (QueryDocumentSnapshot shortNotes: querySnapshot){
                                ShortNote shortNote=shortNotes.toObject(ShortNote.class);
                                shortNoteList.add(shortNote);
                            }


                            shortNoteRecyclerAdapter = new ShortNoteRecyclerAdapter(ShortNote_view.this, shortNoteList, new ShortNoteRecyclerAdapter.ItemClickListener() {
                                @Override
                                public void onItemClick(ShortNote shortNote) {

                                    AlertDialog.Builder builder = new AlertDialog.Builder(ShortNote_view.this);
                                    builder.setTitle(shortNote.getTitle());
                                    builder.setMessage(shortNote.getDescription());
                                    builder.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.cancel();

                                        }
                                    });

                                    builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            progressBar.setVisibility(View.VISIBLE);
                                            collectionReference.document(shortNote.getDocName()).delete();
                                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(shortNote.getImageUrl());
                                            reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    Toast.makeText(ShortNote_view.this, "Deleted", Toast.LENGTH_SHORT).show();
                                                    finish();
                                                    startActivity(getIntent());
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {

                                                }
                                            });

                                        }
                                    });

                                    builder.setNeutralButton("Edit", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                            String uid=shortNote.getUserId();
                                            String uname=shortNote.getUserName();
                                            String title=shortNote.getTitle();
                                            String description=shortNote.getDescription();
                                            String docName=shortNote.getDocName();
                                            String url=shortNote.getImageUrl();


                                            Intent intent=new Intent(ShortNote_view.this,ShortNote_update.class);
                                            intent.putExtra("UID",uid);
                                            intent.putExtra("UNAME",uname);
                                            intent.putExtra("TITLE",title);
                                            intent.putExtra("DESCRIPTION",description);
                                            intent.putExtra("DOCNAME",docName);
                                            intent.putExtra("URL",url);


                                            startActivity(intent);
                                            finish();


                                        }
                                    });

                                    builder.show();


                                }
                            });
                            recyclerView.setAdapter(shortNoteRecyclerAdapter);
                            shortNoteRecyclerAdapter.notifyDataSetChanged();

                        }else {
                            emptySNText.setVisibility(View.VISIBLE);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });


    }
}