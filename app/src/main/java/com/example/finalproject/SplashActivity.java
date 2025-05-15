package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import org.json.JSONObject;

public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_TIMEOUT = 1000; // 1 second
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        // Initialize views
        CardView logoContainer = findViewById(R.id.logo_container);
        ImageView logoImage = findViewById(R.id.logo_image);
        TextView appTitle = findViewById(R.id.app_title);
        TextView appSubtitle = findViewById(R.id.app_subtitle);

        // Apply animations
        applyLogoAnimation(logoContainer);
        applyTextAnimation(appTitle, 300);
        applyTextAnimation(appSubtitle, 500);

        // Check login status after splash timeout
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLoginStatus();
            }
        }, SPLASH_TIMEOUT);
    }

    private void applyLogoAnimation(View view) {
        AnimationSet animSet = new AnimationSet(true);

        // Scale animation
        Animation scaleAnim = new ScaleAnimation(
                0.5f, 1.0f, // Start and end values for X axis scaling
                0.5f, 1.0f, // Start and end values for Y axis scaling
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point of X scaling
                Animation.RELATIVE_TO_SELF, 0.5f); // Pivot point of Y scaling
        scaleAnim.setDuration(700);

        // Alpha animation
        Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        alphaAnim.setDuration(700);

        // Add animations to set
        animSet.addAnimation(scaleAnim);
        animSet.addAnimation(alphaAnim);

        // Start animation
        view.startAnimation(animSet);
    }

    private void applyTextAnimation(View view, int startDelay) {
        Animation alphaAnim = new AlphaAnimation(0.0f, 1.0f);
        alphaAnim.setDuration(700);
        alphaAnim.setStartOffset(startDelay);
        view.startAnimation(alphaAnim);
    }

    private void checkLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            // المستخدم مسجل الدخول بالفعل، انتقل إلى الصفحة الرئيسية
            try {
                JSONObject userDetails = sessionManager.getUserDetails();
                String username = userDetails.getString("username");
                String email = userDetails.getString("email");

                Intent intent = new Intent(SplashActivity.this, home.class);
                intent.putExtra("username", username);
                intent.putExtra("email", email);
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            } catch (Exception e) {
                e.printStackTrace();
                goToLoginActivity();
            }
        } else {
            // المستخدم غير مسجل الدخول، انتقل إلى صفحة تسجيل الدخول
            goToLoginActivity();
        }
    }

    private void goToLoginActivity() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }
}