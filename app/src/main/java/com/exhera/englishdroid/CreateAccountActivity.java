package com.exhera.englishdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exhera.englishdroid.util.JournalApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CreateAccountActivity extends AppCompatActivity {
    private Button createAcctButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;

    //Firestore connection
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");


    private AutoCompleteTextView emailEditText;
    private EditText passwordEditText;
    private EditText userNameEditText;
    private CardView createAccountCard;
    private ProgressBar progressBar;
    private ImageView backImage;
    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_create_account);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_createAcc);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        firebaseAuth = FirebaseAuth.getInstance();


        createAcctButton = findViewById(R.id.signup_createaccount);
        emailEditText = findViewById(R.id.email_account);
        passwordEditText = findViewById(R.id.password_account);
        userNameEditText = findViewById(R.id.username_account);
        createAccountCard= findViewById(R.id.cardView_createAccount);
        progressBar= findViewById(R.id.progressBar_createAccount);
        backImage= findViewById(R.id.back_image_createAccount);






        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                currentUser = firebaseAuth.getCurrentUser();

                if (currentUser != null) {
                    //user is already loggedin..
                }else {
                    //no user yet...
                }

            }
        };


        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateAccountActivity.this,
                        LoginActivity.class);
                startActivity(intent);
                finish();


            }
        });

        createAccountCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (!TextUtils.isEmpty(emailEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString())
                        && !TextUtils.isEmpty(userNameEditText.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String username = userNameEditText.getText().toString().trim();

                    createUserEmailAccount(email, password, username);

                }else {
                    Toast.makeText(CreateAccountActivity.this,
                            "Empty Fields Not Allowed",
                            Toast.LENGTH_LONG)
                            .show();
                }


            }
        });



        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(emailEditText.getText().toString())
                        && !TextUtils.isEmpty(passwordEditText.getText().toString())
                        && !TextUtils.isEmpty(userNameEditText.getText().toString())) {

                    progressBar.setVisibility(View.VISIBLE);
                    String email = emailEditText.getText().toString().trim();
                    String password = passwordEditText.getText().toString().trim();
                    String username = userNameEditText.getText().toString().trim();

                    createUserEmailAccount(email, password, username);

                }else {
                    Toast.makeText(CreateAccountActivity.this,
                            "Empty Fields Not Allowed",
                            Toast.LENGTH_LONG)
                            .show();
                }



            }


        });



    }




    private void createUserEmailAccount(String email, String password, final String username) {
        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(username)) {

//            progressBar.setVisibility(View.VISIBLE);

            firebaseAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                //we take user to AddJournalActivity
                                currentUser = firebaseAuth.getCurrentUser();
                                assert currentUser != null;
                                final String currentUserId = currentUser.getUid();

                                //Create a user Map so we can create a user in the User collection
                                Map<String, String> userObj = new HashMap<>();
                                userObj.put("userId", currentUserId);
                                userObj.put("username", username);

                                //save to our firestore database
                                collectionReference.add(userObj)
                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                            @Override
                                            public void onSuccess(DocumentReference documentReference) {
                                                documentReference.get()
                                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                                if (Objects.requireNonNull(task.getResult()).exists()) {
//                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    String name = task.getResult()
                                                                            .getString("username");

                                                                    JournalApi journalApi = JournalApi.getInstance(); //Global API
                                                                    journalApi.setUserId(currentUserId);
                                                                    journalApi.setUsername(name);
                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                    Intent intent = new Intent(CreateAccountActivity.this,
                                                                            HomeActivity.class);
                                                                    intent.putExtra("username", name);
                                                                    intent.putExtra("userId", currentUserId);
                                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                    startActivity(intent);
                                                                    finish();



                                                                }else {

//                                                                    progressBar.setVisibility(View.INVISIBLE);
                                                                }

                                                            }
                                                        });

                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                            }else {
                                //something went wrong
                            }


                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });

        }else {

        }
    }







    @Override
    protected void onStart() {
        super.onStart();


        currentUser = firebaseAuth.getCurrentUser();
        firebaseAuth.addAuthStateListener(authStateListener);

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent = new Intent(CreateAccountActivity.this,
                LoginActivity.class);
        startActivity(intent);

    }
}