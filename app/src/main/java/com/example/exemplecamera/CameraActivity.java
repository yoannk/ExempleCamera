package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    Context context;
    File file;
    Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = this;

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
                String path = "";

                if (photo != null) {
                    try {
                        path = savePhoto(photo, "test.jpg");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    ImageView imgPhoto = findViewById(R.id.imgPhoto);
                    imgPhoto.setImageBitmap(photo);
                }
            }
        }
    }

    private String savePhoto(Bitmap bitmap, String nomPhoto) throws IOException {
        // répertoire ou l'on sauvegarde l'image
        File photostorage = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
        // fichier du nom de la photo
        File path = new File(photostorage, nomPhoto);

        FileOutputStream fos = new FileOutputStream(path);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        fos.close();

        return path.getAbsolutePath();
    }

    private Bitmap loadPhoto(String path, String nomPhoto) throws FileNotFoundException {
        File file = new File(path, nomPhoto);
        return BitmapFactory.decodeStream(new FileInputStream(file));
    }
}
