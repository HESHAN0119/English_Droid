package com.exhera.englishdroid;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Handler;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.exhera.englishdroid.Dictionary.Dictionary_view;
import com.exhera.englishdroid.FindPP.FindPp;
import com.exhera.englishdroid.Grammar.Preposition;
import com.exhera.englishdroid.Grammar.Report;
import com.exhera.englishdroid.Grammar.Tenses;
import com.exhera.englishdroid.Grammar.VerbAll.Verb;
import com.exhera.englishdroid.Grammar.WhQuestion;
import com.exhera.englishdroid.OCR.OcrActivity;
import com.exhera.englishdroid.PassiveVoice.Passive_Voice;
import com.exhera.englishdroid.ShortNote.ShortNote_view;
import com.exhera.englishdroid.Translator.Translator;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class HomeActivity extends AppCompatActivity {

    private final int ID_translator=1;
    private final int Id_findPp=3;
    private final int ID_ocr=2;

    private TextView homeTitle,homeDescription;

    AdView mAdView;

    CardView shortNoteCard,dictionaryCard;
    private ImageView menu;
    private ImageButton dictionaryImgButton,shortNoteImgButton,tensesImgButton,
            passiveImgButton,verbImgButton,prepositionImgButton,
            whqImgButton,reportImgButton;

    private Button tensesButton,verbButton,reportedButton,whqButton,prepositionButton;

    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();



    private CollectionReference collectionReference = db.collection("Journal");


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            Objects.requireNonNull(this.getSupportActionBar()).hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_home);




        homeTitle= findViewById(R.id.home_title);
        homeDescription= findViewById(R.id.home_description);
        shortNoteCard= findViewById(R.id.imageShortNoteCard);
        dictionaryCard= findViewById(R.id.dictionaryCard);
        menu= findViewById(R.id.menuHome);
        shortNoteImgButton= findViewById(R.id.myShortNoteImgButton);
        dictionaryImgButton=findViewById(R.id.myDictionaryImgButton);
        tensesImgButton= findViewById(R.id.tensesImgButton);
        passiveImgButton= findViewById(R.id.passiveImgButton);
        verbImgButton= findViewById(R.id.verbImgButton);
        prepositionImgButton= findViewById(R.id.prepositionImgButton);
        whqImgButton= findViewById(R.id.whqImgButton);
        reportImgButton= findViewById(R.id.reportedImgButton);
        passiveImgButton= findViewById(R.id.passiveImgButton);
        mAdView = findViewById(R.id.adView_home);


        tensesButton= findViewById(R.id.tenses_button);
        verbButton = findViewById(R.id.verb_button);
        reportedButton = findViewById(R.id.report_button);
        whqButton = findViewById(R.id.whq_button);
        prepositionButton= findViewById(R.id.preposition_button);


        CardView tensesCard = findViewById(R.id.tenses_card);
        CardView passiveCard = findViewById(R.id.passiveCardButton);
        CardView verbsCard= findViewById(R.id.verbCard);
        CardView preposition_Card=findViewById(R.id.prepositionCard);
        CardView whQCard=findViewById(R.id.whCard);
        CardView reportCard= findViewById(R.id.reportCard);
        Button passiveButton = findViewById(R.id.passiveButton);


        tensesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, Tenses.class));
            }
        });

        tensesImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Tenses.class));
            }
        });

        tensesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Tenses.class));
            }
        });

        passiveCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, Passive_Voice.class));
            }
        });

        passiveImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Passive_Voice.class));
            }
        });



        passiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Passive_Voice.class));

            }
        });

        verbsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Verb.class));
            }
        });

        verbImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Verb.class));
            }
        });
        verbButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Verb.class));
            }
        });


        preposition_Card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Preposition.class));
            }
        });

        prepositionImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Preposition.class));
            }
        });
        prepositionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(HomeActivity.this, Preposition.class));
            }
        });


        whQCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WhQuestion.class));
            }
        });

        whqImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WhQuestion.class));
            }
        });
        whqButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, WhQuestion.class));
            }
        });

        reportCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Report.class));
            }
        });

        reportImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Report.class));
            }
        });

        reportedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this, Report.class));
            }
        });



        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable(){
            public void run(){
                title_description();
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String [] items={" Sign out ", " Cancel"};
                AlertDialog.Builder alert=new AlertDialog.Builder(HomeActivity.this);
                //set Title
                alert.setTitle("Sign out");
                alert.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if (i == 0){
                            //camera option clicked
                            logout();

                        }
                        if (i ==1){
                            //gallery option clicked
                            dialogInterface.cancel();

                        }
                    }
                });

                alert.create().show();




            }
        });

        shortNoteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoShortNote();
            }
        });

        shortNoteImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoShortNote();
            }
        });

        shortNoteCard.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                homeTitle.setTextSize(18);
                homeTitle.setText("Image Short Note");
                homeDescription.setTextSize(16);
                homeDescription.setText("My Note helps you to save your notes with photos.");


                return false;
            }
        });




        dictionaryCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDictionary();
            }
        });

        dictionaryImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewDictionary();
            }
        });

//


        dictionaryCard.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
//                if(motionEvent.getAction() == MotionEvent.ACTION_UP){

                    homeTitle.setTextSize(18);
                    homeTitle.setText("My Dictionary");
                    homeDescription.setTextSize(16);
                    homeDescription.setText("This personal dictionary will help you to improve your vocabulary. You can add at least one new word to this per day.");

                    title_description();

                    // Do what you want
//                    return true;

                return false;
            }
        });

        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNav_bar);
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_translator,R.drawable.ic_translator));
        bottomNavigation.add(new MeowBottomNavigation.Model(ID_ocr,R.drawable.ic_ocr_recognition_1));
        bottomNavigation.add(new MeowBottomNavigation.Model(Id_findPp,R.drawable.ic_findppnew));


        bottomNavigation.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item) {



            }
        });




        bottomNavigation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return false;
            }
        });

        bottomNavigation.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {

                String name;
                switch (item.getId()){

                    case ID_translator: name="Translator";



                        homeTitle.setText("Translator");
                        homeDescription.setTextSize(16);
                        homeDescription.setText("");

                    Toast.makeText(HomeActivity.this, "Translator", Toast.LENGTH_SHORT).show();
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                startActivity(new Intent(HomeActivity.this, Translator.class));
                            }
                        }, 500);

                    break;
                    case ID_ocr: name="OCR";

                    break;
                    case Id_findPp: name="Find Pp";
                        homeTitle.setTextSize(18);
                        homeTitle.setText("Find Pp");
                        homeDescription.setTextSize(16);
                        homeDescription.setText("FInd Pp helps you to find the past form and past participle form of any word");

                        Toast.makeText(HomeActivity.this, "Find Past and Past Participle", Toast.LENGTH_SHORT).show();
                        final Handler handler2 = new Handler();
                        handler2.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                // Do something after 5s = 5000ms
                                startActivity(new Intent(HomeActivity.this, FindPp.class));
                            }
                        }, 500);


                    break;
                    default:name="";
                }


            }
        });

        bottomNavigation.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {


                String name;
                switch (item.getId()){

                    case ID_translator: name="Translator";

                        break;
                    case ID_ocr: name="OCR";
                        Toast.makeText(HomeActivity.this, "Image ---> Text Converter", Toast.LENGTH_SHORT).show();
                        int check=1;
                        Intent intent = new Intent(HomeActivity.this, OcrActivity.class);
                        intent.putExtra("CHECK",check);
                        startActivity(intent);
                        break;
                    case Id_findPp: name="Find Pp";

                        break;
                    default:name="";
                }

            }
        });

        bottomNavigation.show(ID_ocr,true);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
    }

    public void logout() {

        if (user != null && firebaseAuth != null) {
            firebaseAuth.signOut();

            startActivity(new Intent(HomeActivity.this,
                    MainActivity.class));
            //finish();
        }
    }

    public void viewDictionary() {

        startActivity(new Intent(HomeActivity.this, Dictionary_view.class));
    }

    public void gotoShortNote() {
        startActivity(new Intent(HomeActivity.this, ShortNote_view.class));
    }

    public void title_description(){



            final Handler handler3 = new Handler();
            handler3.postDelayed(new Runnable() {
                @Override
                public void run() {
                    // Do something after 5s = 5000ms
                    homeTitle.setTextSize(18);
                    homeTitle.setText("OCR (Optical Character Recognition)");
                    homeDescription.setTextSize(16);
                    homeDescription.setText("It is a technology that recognizes text within a digital image. Now you can recognize text in scanned documents and images. ");
                }
            }, 5000);
//        homeTitle.setText("English Droid");
//        homeDescription.setText("Modern way to learn English");


    }




    @Override
    protected void onStart() {
        super.onStart();
        MeowBottomNavigation bottomNavigation = findViewById(R.id.bottomNav_bar);
        bottomNavigation.show(ID_ocr,true);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

    }
}