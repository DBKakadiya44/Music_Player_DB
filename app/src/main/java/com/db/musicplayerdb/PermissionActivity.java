package com.db.musicplayerdb;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.db.musicplayerdb.databinding.ActivityPermissionBinding;

import java.io.File;

public class PermissionActivity extends AppCompatActivity {
    ActivityPermissionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPermissionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        binding.appCompatButton.setOnClickListener(v -> {

            if (!checkPermission()) {
                requestPermission();
            }else {
                Adsdata();
            }
        });

    }

    private boolean checkPermission() {
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            int p1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int p2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return p1 == PackageManager.PERMISSION_GRANTED && p2 == PackageManager.PERMISSION_GRANTED;

        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            int p1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            int p2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
            return p1 == PackageManager.PERMISSION_GRANTED && p2 == PackageManager.PERMISSION_GRANTED;

        } else {
            int p1 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
            return p1 == PackageManager.PERMISSION_GRANTED;
        }

    }

    private void requestPermission() {
        String[] permissions = new String[]{
                android.Manifest.permission.READ_MEDIA_AUDIO,
                WRITE_EXTERNAL_STORAGE
        };

        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"}, 200);
        } else if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(this, permissions, 100);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{"android.permission.READ_EXTERNAL_STORAGE","android.permission.WRITE_EXTERNAL_STORAGE"}, 100);
        }

    }


    public void Adsdata() {
        startActivity(new Intent(PermissionActivity.this, StartActivity.class));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 100:
                if (grantResults.length > 0) {
                    boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (p1) {
                        Adsdata();
                    }else {
                        requestPermission();
                    }
                }
                break;
            case 200:
                if (grantResults.length > 0) {

                    boolean p1 = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean p3 = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (p1 && p3) {
                        Adsdata();
                    }else {
                        requestPermission();
                    }
                    break;

                }


        }
    }

}