package com.exhera.englishdroid.ShortNote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Calendar;
import java.util.Date;

public class ShortNote_add extends AppCompatActivity implements View.OnClickListener {

    private static final int GALLERY_CODE = 1;
    private static  final int CAMERA_REQUEST_CODE =200;
    private static  final int IMAGE_PICK_CAMERA_CODE =1001;

    private Button saveButton;
    private EditText title,description;
    private ImageView imageview;
    private ImageView imageGallery;
    private ImageButton getDataTitle,getDataDescription;

    private String currentUserId;
    private String currentUserName;
    ProgressBar progressBar;




    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseUser user;


    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;


    private final CollectionReference collectionReference = db.collection("ShortNote");
    private Uri imageUri;
    AdView mAdView;

    String cameraPermission[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_short_note_add);

        storageReference = FirebaseStorage.getInstance().getReference();
        firebaseAuth = FirebaseAuth.getInstance();

        progressBar= findViewById(R.id.progressBar_add_shotnote);
        title= findViewById(R.id.title_shortNote_add);
        description= findViewById(R.id.description_shortNote_add);
        imageview= findViewById(R.id.image_shortNote_add);
        imageGallery= findViewById(R.id.add_image_button);
        imageGallery.setOnClickListener(this);
        saveButton= findViewById(R.id.button_saveShoertNote_add);
        saveButton.setOnClickListener(this);

        getDataTitle= findViewById(R.id.get_data_title);
        getDataDescription= findViewById(R.id.get_data_description);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_shortNoteAdd);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);




        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            String gotData=bundle.getString("TEXT");
            getDataTitle.setVisibility(View.VISIBLE);
            getDataDescription.setVisibility(View.VISIBLE);
            getDataTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    title.setText(gotData);
                }
            });


            getDataDescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    description.setText(gotData);
                }
            });

        }






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





        imageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImageDialog();
            }
        });



        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    }





    private void requestCameraPermission() {

        ActivityCompat.requestPermissions(this,cameraPermission, CAMERA_REQUEST_CODE);

    }

    private boolean checkCameraPermission() {

        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }






    private void showImageDialog() {

        String [] items={" Camara ", " Gallery"};
        AlertDialog.Builder dialog=new AlertDialog.Builder(this);
        //set Title
        dialog.setTitle("Select Image");
        dialog.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                if (i == 0){
                    //camera option clicked

                    if (!checkCameraPermission()){

                        requestCameraPermission();
                    }else {
                        pickCamera();
                    }





                }
                if (i ==1){

                    Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    galleryIntent.setType("image/*");
                    startActivityForResult(galleryIntent, GALLERY_CODE);

                }
            }
        });

        dialog.create().show();

    }


    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");//title of the pic
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image to text"); //description
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }




    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.button_saveShoertNote_add:
                //saveJournal
                saveShortNote();
                break;
            case R.id.add_image_button:
                //get image from gallery/phone
               showImageDialog();
                break;
        }

    }

    private void saveShortNote() {

        final String titleValue = title.getText().toString().trim();
        final String descriptionValue = description.getText().toString().trim();


        if (!TextUtils.isEmpty(titleValue) &&
                !TextUtils.isEmpty(descriptionValue)
                && imageUri != null) {

            final Date currentTime = Calendar.getInstance().getTime();
            final String doc_name= currentTime.toString();

            progressBar.setVisibility(View.VISIBLE);


            final StorageReference filepath = storageReference //.../journal_images/our_image.jpeg
                    .child("shortNote_images")
                    .child("my_image"+Timestamp.now().getSeconds()); // my_image_887474737

            filepath.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl=uri.toString();

                                    ShortNote shortNote= new ShortNote();

                                    shortNote.setTitle(titleValue);
                                    shortNote.setDescription(descriptionValue);
                                    shortNote.setUserId(currentUserId);
                                    shortNote.setUserName(currentUserName);
                                    shortNote.setImageUrl(imageUrl);
                                    shortNote.setTimeAdded(new Timestamp(new Date()));
                                    shortNote.setDocName(doc_name);

                                    collectionReference.document(doc_name).set(shortNote).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            startActivity(new Intent(ShortNote_add.this,
                                                    ShortNote_view.class));
                                            finish();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {

                                        }
                                    });


                                }
                            });

                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                }
            });


        }else {
            Toast.makeText(this, "Please fill the Title, Description and insert a image", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK){

            if (requestCode == GALLERY_CODE) {
                if (data != null) {

                    CropImage.activity(data.getData()).
                            setGuidelines(CropImageView.Guidelines.ON)
                            .start(this);

//                    imageUri = data.getData(); // we have the actual path to the image
//                    imageview.setImageURI(imageUri);//show image

                }

            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(imageUri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }




            if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode ==RESULT_OK){
                    Uri resultUri=result.getUri();
                    imageUri=resultUri;
                    imageview.setImageURI(resultUri);

                }
                else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error= result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }



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