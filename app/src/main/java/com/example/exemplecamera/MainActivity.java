package com.example.exemplecamera;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    Context context;
    ArrayList<String> permissions;
    ArrayList<String> permissionsRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        if (Build.VERSION.SDK_INT >= 23) //telephone supérieur à android 5.1
        {
            permissions = new ArrayList<>();
            permissions.add(Manifest.permission.CAMERA);
            permissions.add(Manifest.permission.INTERNET);
            permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            permissions.add(Manifest.permission.READ_EXTERNAL_STORAGE);
            //appelle la méthode permission
            callPermissions();
        } else {
            redirectActivity();
        }
    }

    private void callPermissions() {
        permissionsRequest = new ArrayList<>();

        for (int i = 0; i < permissions.size(); i++) {
            String permissionDemande = permissions.get(i).toString();

            if (ContextCompat.checkSelfPermission(context, permissionDemande)
                    != PackageManager.PERMISSION_GRANTED) {

                permissionsRequest.add(permissionDemande);
            }
        }

        if (permissionsRequest.isEmpty()) //toutes les permissions ont été approuvées
        {
            redirectActivity();
        } else //demande l'acceptation des permissions
        {
            String[] request = new String[permissionsRequest.size()];
            request = permissionsRequest.toArray(request);
            ActivityCompat.requestPermissions(this, request, 100);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        /*switch (requestCode) {
            case 100: {
                //utilisation spécifique en fonction de l'application
            }
            break;
        }*/

        redirectActivity();
    }

    private void redirectActivity() {
        Intent intent = new Intent(context, CameraActivity.class);
        startActivity(intent);
    }
}
