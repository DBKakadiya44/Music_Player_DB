package com.db.musicplayerdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.db.musicplayerdb.databinding.ActivityPrivacyBinding;

public class PrivacyActivity extends AppCompatActivity {
    ActivityPrivacyBinding binding;
    int check = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        binding.imageView.setOnClickListener(v -> {
            check=1;
            binding.imageView2.setVisibility(View.VISIBLE);
        });
        binding.imageView2.setOnClickListener(v -> {
            check=0;
            binding.imageView2.setVisibility(View.INVISIBLE);
        });
        binding.appCompatButton.setOnClickListener(v -> {
            if(check==1){
                startActivity(new Intent(PrivacyActivity.this , PermissionActivity.class));
            }else {
                Toast.makeText(this, "Accept Privacy Concern first..", Toast.LENGTH_SHORT).show();
            }
        });

    }
}