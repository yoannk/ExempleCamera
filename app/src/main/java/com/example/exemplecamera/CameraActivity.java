package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    Context context;
    Bitmap photo;
    Uri uri;
    String path;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        context = this;

        Button btnCamera = findViewById(R.id.btnCamera);
        Button btnSavePhoto = findViewById(R.id.btnSavePhoto);
        Button btnVoirPhotosEnregistrees = findViewById(R.id.btnVoirPhotosEnregistrees);

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

        btnSavePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText txtNomPhoto = findViewById(R.id.txtNomPhoto);
                String nomPhoto = txtNomPhoto.getText()
                        .toString()
                        .replaceAll("\\W+", "") // replaces any character that isn't a number, letter or underscore with nothing
                        .trim()
                        .toLowerCase();

                if (nomPhoto.isEmpty()) {
                    Toast.makeText(context, "Veuillez saisir un nom de photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (photo != null) {
                    try {
                        nomPhoto += ".jpg";
                        path = savePhoto(photo, nomPhoto);
                        Toast.makeText(context, nomPhoto + " a bien été enregistrée", Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Toast.makeText(context, "Une erreur est survenue pendant l'enregistrement de la photo", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btnVoirPhotosEnregistrees.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewPhotoActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (requestCode == 100 && resultCode == RESULT_OK) { // mode camera
            if (intent != null && intent.hasExtra("data")) {

                // on récupère la photo à partir de l'intent
                Bundle extras = intent.getExtras();
                photo = (Bitmap) extras.get("data");

                if (photo != null) {
                    ImageView imgPhoto = findViewById(R.id.imgPhoto);
                    imgPhoto.setImageBitmap(photo);
                }

                ImageView imgPhoto = findViewById(R.id.imgPhoto);
                imgPhoto.setImageBitmap(photo);
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

        return path.getParent();
    }
}
