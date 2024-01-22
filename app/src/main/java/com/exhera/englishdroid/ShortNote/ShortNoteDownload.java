package com.exhera.englishdroid.ShortNote;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.ablanco.zoomy.TapListener;
import com.ablanco.zoomy.Zoomy;
import com.exhera.englishdroid.R;
import com.squareup.picasso.Picasso;

public class ShortNoteDownload extends AppCompatActivity {


    ImageView imagepreview;
    String Url;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_short_note_download);





        Bundle bundle= getIntent().getExtras();
        if (bundle!=null){
            Url=bundle.getString("URL");
            System.out.println(bundle.getString("URL"));

            imagepreview= findViewById(R.id.singleImage);

            Picasso.get()
                    .load(Url)
                    .placeholder(R.drawable.image_three)
                    .into(imagepreview);
        }
        Zoomy.Builder builder= new Zoomy.Builder(this)
                .target(imagepreview).animateZooming(false)
                .enableImmersiveMode(false)
                .tapListener(new TapListener() {
                    @Override
                    public void onTap(View v) {
                        Toast.makeText(ShortNoteDownload.this, "Zoom open", Toast.LENGTH_SHORT).show();
                    }
                });

//
        builder.register();


        imagepreview.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ClipboardManager clipboard= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("text",Url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(ShortNoteDownload.this, "Url Copied...", Toast.LENGTH_SHORT).show();
                Toast.makeText(ShortNoteDownload.this, "Search the Url on Internet or Share it...", Toast.LENGTH_SHORT).show();

                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_zoom,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.url_copy:

                ClipboardManager clipboard= (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip=ClipData.newPlainText("text",Url);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(this, "Url Copied...", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "Search the Url on Internet or Share it...", Toast.LENGTH_SHORT).show();


        }

        return super.onOptionsItemSelected(item);
    }


}