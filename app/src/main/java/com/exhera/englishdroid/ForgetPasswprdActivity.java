package com.exhera.englishdroid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgetPasswprdActivity extends AppCompatActivity {

    private EditText emailEditText;
    private Button resetPwButton;
    private ProgressBar progressBar;
    AdView mAdView;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_passwprd);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_resetPw);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        emailEditText= findViewById(R.id.resetPasswordEditText);
        resetPwButton= findViewById(R.id.resetPasswordButton);
        progressBar = findViewById(R.id.reset_progressBar);
        auth = FirebaseAuth.getInstance();
        
        
        resetPwButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
        
    }

    private void resetPassword() {
        String email= emailEditText.getText().toString().trim();

        if (email.isEmpty()){
            emailEditText.setError("Email is required!");
            emailEditText.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            emailEditText.setError("PLease provide valid email");
            emailEditText.requestFocus();
            return;
        }
        
        
        progressBar.setVisibility(View.VISIBLE);
        
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    Toast.makeText(ForgetPasswprdActivity.this, "Check Your email to reset your password !", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(ForgetPasswprdActivity.this, "Try again something wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}