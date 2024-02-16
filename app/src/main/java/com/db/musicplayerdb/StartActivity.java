package com.db.musicplayerdb;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import com.db.musicplayerdb.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Window window = this.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.maincolor));

        binding.appCompatButton.setOnClickListener(v -> {
            startActivity(new Intent(StartActivity.this, MainActivity.class));
            SplashActivity.editor.putInt("screen",11);
            SplashActivity.editor.commit();
            finish();
        });

        binding.share.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.TEXT", "Hey Download the app for fastest cricket updates : https://play.google.com/store/apps/details?id=" + getPackageName());
            intent.setType("text/plain");
            startActivity(intent);
        });

        binding.rate.setOnClickListener(v -> {
            String packageName = getPackageName();
            try {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + packageName)));
            } catch (ActivityNotFoundException unused) {
                startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://play.google.com/store/apps/details?id=" + packageName)));
            }
        });

        binding.privacy.setOnClickListener(v -> {
            String url = "https://www.freeprivacypolicy.com/blog/privacy-policy-url/";

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });


    }
}