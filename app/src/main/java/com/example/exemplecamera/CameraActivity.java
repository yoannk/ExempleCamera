package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;

public class CameraActivity extends AppCompatActivity {

    Context context;
    File file;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = this;
        File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);

        Button btnCamera = findViewById(R.id.btnCamera);
        //file = new File(photostorage, "temp.jpg");

        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {

                    // mode de fonctionnement de la camera
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);

                    if (intent.resolveActivity(getPackageManager()) != null) {
                        startActivityForResult(intent, 100);
                    }
                } else {
                    Toast.makeText(context, "La camera doit être activée", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Bitmap photo;

        if (requestCode == 100 && resultCode == RESULT_OK) { // mode camera
            if (intent != null && intent.hasExtra("data")) {

                // on récupère la photo à partir de l'intent
                Bundle extras = intent.getExtras();
                photo = (Bitmap) extras.get("data");

                if(photo != null) {
                    ImageView imgPhoto = findViewById(R.id.imgPhoto);
                    imgPhoto.setImageBitmap(photo);
                }
            }
        }
    }
}
