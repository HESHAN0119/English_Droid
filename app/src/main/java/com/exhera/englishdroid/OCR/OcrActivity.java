package com.exhera.englishdroid.OCR;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.exhera.englishdroid.Dictionary.Dictionary_add;
import com.exhera.englishdroid.R;
import com.exhera.englishdroid.ShortNote.ShortNote_add;
import com.exhera.englishdroid.Translator.Translator;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

public class OcrActivity extends AppCompatActivity {


    EditText mResultEt;
    ImageView mPreviewIv;


    private static  final int CAMERA_REQUEST_CODE =200;
    private static  final int STORAGE_REQUEST_CODE =400;
    private static  final int IMAGE_PICK_GALLERY_CODE =1000;
    private static  final int IMAGE_PICK_CAMERA_CODE =1001;

    String cameraPermission[];
    String storagePermission[];
    Uri image_uri;
    int got_data;
    String return_text;
    int fromL,i1;
    int toL,i2;
    AdView mAdView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocr);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        mAdView = findViewById(R.id.adView_ocr);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mResultEt=findViewById(R.id.resultEt);
        mPreviewIv=findViewById(R.id.imageIv);

        //camera permission
        cameraPermission = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Bundle bundle = getIntent().getExtras();
        if (bundle !=null){
            int got_data=bundle.getInt("CHECK");
            if (got_data==1){
                showImageDialog();
            }
        }

        Bundle bundle_translator = getIntent().getExtras();
        if (bundle_translator !=null){
           got_data=bundle_translator.getInt("FROM");
            fromL =bundle_translator.getInt("FROM_LANGUAGE");
            toL=bundle_translator.getInt("TO_LANGUAGE");
            i1=bundle_translator.getInt("I1");
            i2=bundle_translator.getInt("I2");
            if (got_data==2){
                showImageDialog();

            }
        }





    }

    //action Bar menu

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_ocr,menu);
        return super.onCreateOptionsMenu(menu);


    }
    //handle action bar


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.addImage){
            showImageDialog();
        }
        if (id== R.id.sent_dict){
            String dict_send_data=mResultEt.getText().toString();
            if (!TextUtils.isEmpty(dict_send_data)){
                Intent intent = new Intent(OcrActivity.this, Dictionary_add.class);
                intent.putExtra("TEXT",dict_send_data);
                startActivity(intent);

                Toast.makeText(this, "Sending Data to dictionary...", Toast.LENGTH_SHORT).show();
                finish();
            }else {
                Toast.makeText(this, "Result text is empty !", Toast.LENGTH_SHORT).show();
            }


        }
        if (id== R.id.sent_sNote){
            String dict_send_data=mResultEt.getText().toString();

            if (!TextUtils.isEmpty(dict_send_data)){
                Intent intent = new Intent(OcrActivity.this, ShortNote_add.class);
                intent.putExtra("TEXT",dict_send_data);
                startActivity(intent);

                Toast.makeText(this, "Sending Data to Short note...", Toast.LENGTH_SHORT).show();
                finish();

            }else {
                Toast.makeText(this, "Result text is empty !", Toast.LENGTH_SHORT).show();
            }



        }

        if (id== R.id.clipboard_copy){

            if (!TextUtils.isEmpty(mResultEt.getText())){

                ClipboardManager clipboard= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("text",mResultEt.getText().toString());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Text Copied...", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(this, "Result text is empty!", Toast.LENGTH_SHORT).show();
            }


        }


        return super.onOptionsItemSelected(item);
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
                    //gallery option clicked
                    if (!checkStoragePermission()){

                        requestStoragePermission();
                    }else {
                        pickGallery();
                    }
                }
            }
        });

        dialog.create().show();

    }

    private void pickGallery() {

        Intent intent =  new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,IMAGE_PICK_GALLERY_CODE);
    }

    private void pickCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"NewPic");//title of the pic
        values.put(MediaStore.Images.Media.DESCRIPTION,"Image to text"); //description
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent,IMAGE_PICK_CAMERA_CODE);

    }

    private void requestStoragePermission() {

        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);

        return result;
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;


                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted) {
                        pickCamera();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:

                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = grantResults[0] ==
                            PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted) {
                        pickGallery();
                    } else {
                        Toast.makeText(this, "permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode ==RESULT_OK){
            if (requestCode==IMAGE_PICK_GALLERY_CODE){
                CropImage.activity(data.getData()).
                        setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE){
                CropImage.activity(image_uri)
                        .setGuidelines(CropImageView.Guidelines.ON)
                        .start(this);
            }

            if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode ==RESULT_OK){
                    Uri resultUri=result.getUri();
                    mPreviewIv.setImageURI(resultUri);

                    BitmapDrawable bitmapDrawable =(BitmapDrawable)mPreviewIv.getDrawable();
                    Bitmap bitmap = bitmapDrawable.getBitmap();

                    TextRecognizer recognizer = new TextRecognizer.Builder(getApplicationContext()).build();
                    if (!recognizer.isOperational()){
                        Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                    }else {
                        Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                        SparseArray<TextBlock> items= recognizer.detect(frame);
                        StringBuilder sb =new StringBuilder();

                        for (int i=0; i<items.size();i++){
                            TextBlock myItem= items.valueAt(i);
                            sb.append(myItem.getValue());
                            sb.append("\n");
                        }
                        if (got_data==2){
                            System.out.println(sb.toString());
                            Intent intent =new Intent(OcrActivity.this, Translator.class);
                            intent.putExtra("RETURN_VALUE",sb.toString());
                            intent.putExtra("FROML",fromL);
                            intent.putExtra("TOL",toL);
                            intent.putExtra("I1",i1);
                            intent.putExtra("I2",i2);

                            startActivity(intent);
                            finish();
                        }else {
                            mResultEt.setText(sb.toString());
                        }




                    }
                }
                else if (resultCode ==CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE){
                    Exception error= result.getError();
                    Toast.makeText(this, ""+error, Toast.LENGTH_SHORT).show();
                }
            }



        }
    }



}