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
import android.widget.TextView;
import android.widget.Toast;

import com.exhera.englishdroid.util.JournalApi;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import javax.annotation.Nullable;

public class LoginActivity extends AppCompatActivity {



    private Button createAcctButton;
    private CardView loginCard;
    private ImageView backImage;
private ProgressBar progressBar;

    private EditText  password;
    private TextView loginText;
    private AutoCompleteTextView emailAddres;
    private TextView forgetPassword;


    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser currentUser;


    AdView mAdView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference collectionReference = db.collection("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}
        setContentView(R.layout.activity_login);

        progressBar= findViewById(R.id.progressBar_login);

//        progressBar.getProgressDrawable().setColorFilter(
//                Color.RED, android.graphics.PorterDuff.Mode.SRC_IN);



        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_login);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        firebaseAuth = FirebaseAuth.getInstance();

        emailAddres = findViewById(R.id.email_login);
        password = findViewById(R.id.password_login);

        createAcctButton = findViewById(R.id.button_signup);
        loginCard= findViewById(R.id.loginCardView);
        backImage = findViewById(R.id.back_image_loginPage);
        loginText= findViewById(R.id.textViewLogin);
        forgetPassword= findViewById(R.id.forgetPassword_textView);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,ForgetPasswprdActivity.class));
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        loginCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginEmailPasswordUser(emailAddres.getText().toString().trim(),
                        password.getText().toString().trim());

            }
        });



        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                loginEmailPasswordUser(emailAddres.getText().toString().trim(),
                        password.getText().toString().trim());

            }
        });

        createAcctButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,CreateAccountActivity.class));
                finish();
            }
        });


    }


    private void loginEmailPasswordUser(String email, String pwd) {



        if (!TextUtils.isEmpty(email)
                && !TextUtils.isEmpty(pwd)) {
            progressBar.setVisibility(View.VISIBLE);

            try {

                firebaseAuth.signInWithEmailAndPassword(email, pwd)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                FirebaseUser user = firebaseAuth.getCurrentUser();

                                if (user!=null){
//                                    assert user != null;


                                    final String currentUserId = user.getUid();

                                    collectionReference
                                            .whereEqualTo("userId", currentUserId)
                                            .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                @Override
                                                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
                                                                    @Nullable FirebaseFirestoreException e) {

                                                    if (e != null) {
                                                    }
                                                    assert queryDocumentSnapshots != null;
                                                    if (!queryDocumentSnapshots.isEmpty()) {

                                                        progressBar.setVisibility(View.INVISIBLE);
                                                        for (QueryDocumentSnapshot snapshot : queryDocumentSnapshots) {
                                                            JournalApi journalApi = JournalApi.getInstance();
                                                            journalApi.setUsername(snapshot.getString("username"));
                                                            journalApi.setUserId(snapshot.getString("userId"));

                                                            //Go to ListActivity
                                                            try {
                                                                startActivity(new Intent(LoginActivity.this,
                                                                        HomeActivity.class));
                                                                finish();
                                                            } catch (Exception exception) {
                                                                exception.printStackTrace();
                                                            }


                                                        }


                                                    }

                                                }
                                            });

                                } else  {

                                   progressBar.setVisibility(View.INVISIBLE);
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Please check your email and password!  ", Toast.LENGTH_SHORT).show();

                            }
                        });

            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            progressBar.setVisibility(View.INVISIBLE);
            Toast.makeText(LoginActivity.this,
                    "Please enter email and password",
                    Toast.LENGTH_LONG)
                    .show();
        }
    }




}