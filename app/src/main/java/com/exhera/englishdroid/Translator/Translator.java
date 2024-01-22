package com.exhera.englishdroid.Translator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.exhera.englishdroid.OCR.OcrActivity;
import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.Locale;

public class Translator extends AppCompatActivity {

    AdView mAdView;
    private Spinner fromSpinner, toSpinner;
    private TextInputEditText sourceEdit;
    ImageView micIV;
    int i1,i2;
    private ImageButton ocrTrans;
    private MaterialButton translateBtn;
    private EditText translatedTV;
    String[] fromLanguages={"From","English","Afrikaans","Albanian","Arabic","Belarusian","Bulgarian","Bengali",
            "Catalan","Chinese","Czech","Croatian","Danish","Dutch","Esperanto","Estonian","Finnish","French","German",
            "Greek","Galician","Georgian","Gujarati","Hebrew","Hindi","Haitian","Hungarian","Irish","Indonesian","Icelandic",
            "Italian","Japanese","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Norwegian",
            "Persian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Swedish","Swahili",
            "Spanish","Tamil","Telugu","Thai","Tagalog","Turkish","Ukrainian","Urdu","Vietnamese","Welsh"};

    String[] toLanguages={"To","English","Afrikaans","Albanian","Arabic","Belarusian","Bulgarian","Bengali",
            "Catalan","Chinese","Czech","Croatian","Danish","Dutch","Esperanto","Estonian","Finnish","French","German",
            "Greek","Galician","Georgian","Gujarati","Hebrew","Hindi","Haitian","Hungarian","Irish","Indonesian","Icelandic",
            "Italian","Japanese","Kannada","Korean","Lithuanian","Latvian","Macedonian","Marathi","Malay","Maltese","Norwegian",
            "Persian","Polish","Portuguese","Romanian","Russian","Slovak","Slovenian","Swedish","Swahili",
            "Spanish","Tamil","Telugu","Thai","Tagalog","Turkish","Ukrainian","Urdu","Vietnamese","Welsh"};

    private static final int REQUEST_PERMISSION_CODE= 1;
    int languageCode, fromLanguageCode,toLanguageCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_translator);

//        Dictionary_add dictionary_add=  new Dictionary_add();
        sourceEdit = findViewById(R.id.idEditSource);
        fromSpinner = findViewById(R.id.idFromSpinner);
        toSpinner = findViewById(R.id.idtoSpinner);
        ocrTrans= findViewById(R.id.ocrButtonTrans);
        micIV= findViewById(R.id.idIVMic);
        translateBtn=findViewById(R.id.idBtnTranslate);
        translatedTV=findViewById(R.id.idTVTranslatedTV);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_translator);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        translatedTV.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {

                ClipboardManager clipboard= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("text",translatedTV.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(Translator.this, "Full Translated Text Copied...", Toast.LENGTH_SHORT).show();


                return false;
            }
        });


        fromSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                i1=i;
                fromLanguageCode=getLanguageCode(fromLanguages[i]);
                System.out.println(fromLanguages[i]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ArrayAdapter fromAdapter= new ArrayAdapter(this,R.layout.spinner_item,fromLanguages);
        fromAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fromSpinner.setAdapter(fromAdapter);

        toSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                i2=i;
                toLanguageCode=getLanguageCode(toLanguages[i]);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        
        ArrayAdapter toAdapter=new ArrayAdapter(this,R.layout.spinner_item,toLanguages);
        toAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        toSpinner.setAdapter(toAdapter);
        
        translateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                translatedTV.setText("");
                if (sourceEdit.getText().toString().isEmpty()){
                    Toast.makeText(Translator.this, "Please enter your text to translate", Toast.LENGTH_SHORT).show();
                }else if (fromLanguageCode ==111111){
                    Toast.makeText(Translator.this, "Please select source language ", Toast.LENGTH_SHORT).show();
                }
                else if (toLanguageCode ==111111){
                    Toast.makeText(Translator.this, "Please select the language to translate", Toast.LENGTH_SHORT).show();
                }else {
                        translateText(fromLanguageCode,toLanguageCode,sourceEdit.getText().toString());
                }
            }
        });



        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text");
                try {
                    startActivityForResult(intent,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Translator.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });





        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){

            System.out.println(bundle.getString("RETURN_VALUE"));
            sourceEdit.setText(bundle.getString("RETURN_VALUE"));
            fromSpinner.setSelection(bundle.getInt("I1"));

            toSpinner.setSelection(bundle.getInt("I2"));
            translateText(bundle.getInt("FROML"),bundle.getInt("TOL"), bundle.getString("RETURN_VALUE"));

        }


        ocrTrans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ocrTranslator();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_PERMISSION_CODE){
            if (resultCode==RESULT_OK && data!=null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdit.setText(result.get(0));
            }
        }

    }

    private void translateText(int fromLanguageCode, int toLanguageCode, String source){

        translatedTV.setText("Translating...");

        FirebaseTranslatorOptions options = new FirebaseTranslatorOptions.Builder()
                .setSourceLanguage(fromLanguageCode)
                .setTargetLanguage(toLanguageCode)
                .build();

        FirebaseTranslator translator = FirebaseNaturalLanguage.getInstance().getTranslator(options);
        FirebaseModelDownloadConditions conditions = new FirebaseModelDownloadConditions.Builder().build();
        translatedTV.setText("If the first time takes a few seconds...");
        translator.downloadModelIfNeeded(conditions).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void unused) {

                translatedTV.setText("Translating...");

                translator.translate(source).addOnSuccessListener(new OnSuccessListener<String>() {
                    @Override
                    public void onSuccess(String s) {
                             translatedTV.setText(s);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(Translator.this, "Fail to translate"+ e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Translator.this, "Fail to download language Model "+ e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

 }

//    String[] fromLanguage={"From","English","Afrikaans","Arabic","Belarusian","Bulgarian","Bengali",
//            "Catalan","Czech","Welsh","Hindi","Urdu"};
//


    private int getLanguageCode(String language) {

        int languageCode=111111;

        switch (language){
            case "English":
                languageCode= FirebaseTranslateLanguage.EN;
                break;

            case "Afrikaans":
                languageCode= FirebaseTranslateLanguage.AF;
                break;

            case "Arabic":
                languageCode= FirebaseTranslateLanguage.AR;
                break;

            case "Bulgarian":
                languageCode=FirebaseTranslateLanguage.BG;
                break;
            case "Belarusian":
                languageCode= FirebaseTranslateLanguage.BE;
                break;

            case "Bengali":
                languageCode= FirebaseTranslateLanguage.BN;
                break;

            case "Catalan":
                languageCode= FirebaseTranslateLanguage.CA;
                break;
            case "Czech":
                languageCode= FirebaseTranslateLanguage.CS;
                break;
            case "Welsh":
                languageCode= FirebaseTranslateLanguage.CY;
                break;
            case "Danish":
                languageCode= FirebaseTranslateLanguage.DA;
                break;
            case "German":
                languageCode= FirebaseTranslateLanguage.DE;
                break;
            case "Greek":
                languageCode= FirebaseTranslateLanguage.EL;
                break;
            case "Spanish":
                languageCode= FirebaseTranslateLanguage.ES;
                break;
            case "Estonian":
                languageCode= FirebaseTranslateLanguage.ET;
                break;
            case "Persian":
                languageCode= FirebaseTranslateLanguage.FA;
                break;
            case "Finnish":
                languageCode= FirebaseTranslateLanguage.FI;
                break;
            case "French":
                languageCode= FirebaseTranslateLanguage.FR;
                break;
            case "Irish":
                languageCode= FirebaseTranslateLanguage.GA;
                break;
            case "Esperanto":
                languageCode= FirebaseTranslateLanguage.EO;
                break;

            case "Galician":
                languageCode= FirebaseTranslateLanguage.GL;
                break;
            case "Gujarati":
                languageCode= FirebaseTranslateLanguage.GU;
                break;
            case "Hebrew":
                languageCode= FirebaseTranslateLanguage.HE;
                break;

            case "Hindi":
                languageCode= FirebaseTranslateLanguage.HI;
                break;

            case "Croatian":
                languageCode= FirebaseTranslateLanguage.HR;
                break;
            case "Haitian":
                languageCode= FirebaseTranslateLanguage.HT;
                break;
            case "Hungarian":
                languageCode= FirebaseTranslateLanguage.HU;
                break;
            case "Indonesian":
                languageCode= FirebaseTranslateLanguage.ID;
                break;
            case "Icelandic":
                languageCode= FirebaseTranslateLanguage.IS;
                break;
            case "Italian":
                languageCode= FirebaseTranslateLanguage.IT;
                break;
            case "Japanese":
                languageCode= FirebaseTranslateLanguage.JA;
                break;

            case "Georgian":
                languageCode= FirebaseTranslateLanguage.KA;
                break;
            case "Kannada":
                languageCode= FirebaseTranslateLanguage.KN;
                break;
            case "Korean":
                languageCode= FirebaseTranslateLanguage.KO;
                break;
            case "Lithuanian":
                languageCode= FirebaseTranslateLanguage.LT;
                break;
            case "Latvian":
                languageCode= FirebaseTranslateLanguage.LV;
                break;
            case "Macedonian":
                languageCode= FirebaseTranslateLanguage.MK;
                break;
            case "Marathi":
                languageCode= FirebaseTranslateLanguage.MR;
                break;
            case "Malay":
                languageCode= FirebaseTranslateLanguage.MS;
                break;
            case "Maltese":
                languageCode= FirebaseTranslateLanguage.MT;
                break;
            case "Dutch":
                languageCode= FirebaseTranslateLanguage.NL;
                break;

            case "Norwegian":
                languageCode= FirebaseTranslateLanguage.NO;
                break;
            case "Polish":
                languageCode= FirebaseTranslateLanguage.PL;
                break;
            case "Portuguese":
                languageCode= FirebaseTranslateLanguage.PT;
                break;
            case "Romanian":
                languageCode= FirebaseTranslateLanguage.RO;
                break;
            case "Russian":
                languageCode= FirebaseTranslateLanguage.RU;
                break;
            case "Slovak":
                languageCode= FirebaseTranslateLanguage.SK;
                break;
            case "Slovenian":
                languageCode= FirebaseTranslateLanguage.SL;
                break;
            case "Albanian":
                languageCode= FirebaseTranslateLanguage.SQ;
                break;
            case "Swedish":
                languageCode= FirebaseTranslateLanguage.SV;
                break;
            case "Swahili":
                languageCode= FirebaseTranslateLanguage.SW;
                break;

            case "Tamil":
                languageCode= FirebaseTranslateLanguage.TA;
                break;
            case "Telugu":
                languageCode= FirebaseTranslateLanguage.TE;
                break;
            case "Thai":
                languageCode= FirebaseTranslateLanguage.TH;
                break;
            case "Tagalog":
                languageCode= FirebaseTranslateLanguage.TL;
                break;
            case "Turkish":
                languageCode= FirebaseTranslateLanguage.TR;
                break;
            case "Ukrainian":
                languageCode= FirebaseTranslateLanguage.UK;
                break;
            case "Vietnamese":
                languageCode= FirebaseTranslateLanguage.VI;
                break;
            case "Chinese":
                languageCode= FirebaseTranslateLanguage.ZH;
                break;

            case "Urdu":
                languageCode= FirebaseTranslateLanguage.UR;
                break;

            default:
                languageCode=111111;







        }
        return  languageCode;
    }

    public void ocrTranslator() {

        fromSpinner.setSelection(1);
        fromLanguageCode=getLanguageCode(fromLanguages[1]);
        if (toLanguageCode ==111111){
            Toast.makeText(Translator.this, "Please select the language to translate", Toast.LENGTH_SHORT).show();
        }else {

           Intent intent = new Intent(Translator.this, OcrActivity.class);
           intent.putExtra("FROM",2);
           intent.putExtra("FROM_LANGUAGE",fromLanguageCode);
           intent.putExtra("TO_LANGUAGE",toLanguageCode);
           intent.putExtra("I1",1);
           intent.putExtra("I2",i2);
           startActivity(intent);
           finish();

        }




    }
}