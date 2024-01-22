package com.exhera.englishdroid.ShortNote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.exhera.englishdroid.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ShortNote_update extends AppCompatActivity implements View.OnClickListener {


    private static final int GALLERY_CODE = 1;
    private static  final int IMAGE_PICK_CAMERA_CODE =1001;

    private Button updateButton;
    private EditText title,description;
    private ImageView imageview,imageGallery;

    private String upId,uptitle,upDescription,updocname,upname,upUrl;


    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference;

    private CollectionReference collectionReference = db.collection("ShortNote");
    private Uri imageUri;
    private ProgressBar progressBar;

    AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_short_note_update);

        storageReference = FirebaseStorage.getInstance().getReference();


        title= findViewById(R.id.title_shortNote_update);
        description= findViewById(R.id.description_shortNote_update);
        imageview= findViewById(R.id.image_shortNote_update);
        imageGallery= findViewById(R.id.update_image_button);
        imageGallery.setOnClickListener(this);
        updateButton= findViewById(R.id.button_saveShoertNote_update);
        updateButton.setOnClickListener(this);
        progressBar= findViewById(R.id.progressBarUpdate);


        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_shortNoteUpdate);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);



        Bundle bundle= getIntent().getExtras();
        if (bundle  != null){
//            actionBar.setTitle("Update My Dictionary");
            upId=bundle.getString("UID");
            uptitle=bundle.getString("TITLE");
            upDescription= bundle.getString("DESCRIPTION");
            upUrl=bundle.getString("URL");

            updocname=bundle.getString("DOCNAME");



            title.setText(uptitle);
            description.setText(upDescription);


            Picasso.get()
                    .load(upUrl)
                    .placeholder(R.drawable.image_three)
                    .into(imageview);

            imageview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImageDialog();
                }
            });


            updateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    try {
                        if (imageUri !=null) {
                            progressBar.setVisibility(View.VISIBLE);

                            StorageReference reference = FirebaseStorage.getInstance().getReferenceFromUrl(upUrl);
                            reference.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ShortNote_update.this, "Start Updating", Toast.LENGTH_SHORT).show();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }






                    final String titleValue = title.getText().toString().trim();
                    final String descriptionValue = description.getText().toString().trim();


                    if (!TextUtils.isEmpty(titleValue) &&
                            !TextUtils.isEmpty(descriptionValue)
                            ) {
                        if (imageUri != null) {

                        final StorageReference filepath = storageReference //.../journal_images/our_image.jpeg
                                .child("shortNote_images")
                                .child("my_image" + Timestamp.now().getSeconds()); // my_image_887474737

                        filepath.putFile(imageUri)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                        filepath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {

                                                String imageUrl = uri.toString();

                                                System.out.println(imageUrl);

                                                updateShortNote(titleValue, descriptionValue, updocname, imageUrl);

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                    } else {
                            Toast.makeText(ShortNote_update.this, "Start Updating", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.VISIBLE);
                            updateShortNote_2(titleValue, descriptionValue, updocname);

                        }
                    }









                }
            });







        }else {

        }


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

                    pickCamera();


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






    private void  updateShortNote(String title, String description,String docname, String url){
        collectionReference.document(docname).update("title",title,"description",description,"imageUrl",url)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ShortNote_update.this, ShortNote_view.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }


    private void  updateShortNote_2(String title, String description,String docname){
        collectionReference.document(docname).update("title",title,"description",description)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(ShortNote_update.this, ShortNote_view.class));
                        finish();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });




    }




    @Override
    public void onClick(View view) {


        switch (view.getId()) {
            case R.id.update_image_button:
                //get image from gallery/phone

                showImageDialog();



//                Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
//                galleryIntent.setType("image/*");
//                startActivityForResult(galleryIntent, GALLERY_CODE);
                break;
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


}