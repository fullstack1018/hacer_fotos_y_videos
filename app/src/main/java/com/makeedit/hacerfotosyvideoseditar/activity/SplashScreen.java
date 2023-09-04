package com.makeedit.hacerfotosyvideoseditar.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.makeedit.hacerfotosyvideoseditar.R;

import java.util.Locale;


public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Locale currentLocale = getResources().getConfiguration().locale;
        String systemLanguage = currentLocale.getLanguage();

        setLocale(this, systemLanguage);

        Configuration config = new Configuration();
        config.locale = currentLocale;

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                Intent i = new Intent(SplashScreen.this, StartActivity.class);
                startActivity(i);
            }
        }, SPLASH_TIME_OUT);
    }
    public static void setLocale(Activity activity, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            config.setLocale(locale);
        }
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}
