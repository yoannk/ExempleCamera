package com.example.exemplecamera;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ViewPhotoActivity extends AppCompatActivity {

    Context context;
    String path = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_photo);
        context = this;

        Intent intent = getIntent();

        if (intent != null) {
            path = intent.getStringExtra("path");
        }

        Button btnLoadPhoto = findViewById(R.id.btnLoadPhoto);

        btnLoadPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText txtNomPhotoLoad = findViewById(R.id.txtNomPhotoLoad);
                String nomPhoto = txtNomPhotoLoad.getText().toString().trim().toLowerCase();

                if (nomPhoto.isEmpty()) {
                    Toast.makeText(context, "Veuillez saisir le nom de la photo", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (path.isEmpty()) {
                    Toast.makeText(context, "Le chemin est introuvable !", Toast.LENGTH_SHORT).show();
                    return;
                }

                ImageView imgPhoto = findViewById(R.id.imgPhoto);
                try {
                    Bitmap photo = loadPhoto(path, nomPhoto + ".jpg");
                    imgPhoto.setImageBitmap(photo);
                } catch (FileNotFoundException e) {
                    Toast.makeText(context, "Aucune image avec ce nom a été trouvé", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private Bitmap loadPhoto(String path, String nomPhoto) throws FileNotFoundException {
        File file = new File(path, nomPhoto);
        return BitmapFactory.decodeStream(new FileInputStream(file));
    }
}
