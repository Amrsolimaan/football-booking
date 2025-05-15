// MainActivity.java
package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput;
    private Button loginButton;
    private TextView signupText;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // تهيئة مدير الجلسات
        sessionManager = new SessionManager(this);

        emailInput    = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton   = findViewById(R.id.login_button);
        signupText    = findViewById(R.id.signup_text);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email    = emailInput.getText().toString().trim();
                String password = passwordInput.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this,
                            "Please enter email and password",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    FileInputStream fis = openFileInput("users.json");
                    byte[] data = new byte[fis.available()];
                    fis.read(data);
                    fis.close();
                    JSONArray usersArray = new JSONArray(new String(data));

                    boolean emailFound     = false;
                    boolean passwordCorrect = false;
                    String  userName       = "";

                    for (int i = 0; i < usersArray.length(); i++) {
                        JSONObject user = usersArray.getJSONObject(i);
                        if (user.getString("email")
                                .equalsIgnoreCase(email)) {
                            emailFound = true;
                            if (user.getString("password")
                                    .equals(password)) {
                                passwordCorrect = true;
                                userName = user.optString("name", "");
                            }
                            break;
                        }
                    }

                    if (!emailFound) {
                        Toast.makeText(MainActivity.this,
                                "Email not found. Please register first.",
                                Toast.LENGTH_LONG).show();
                    } else if (!passwordCorrect) {
                        Toast.makeText(MainActivity.this,
                                "Incorrect password. Please try again.",
                                Toast.LENGTH_LONG).show();
                    } else {
                        // حفظ حالة تسجيل الدخول
                        sessionManager.saveLoginSession(email, userName);

                        Intent intent = new Intent(MainActivity.this, home.class);
                        intent.putExtra("username", userName);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this,
                            "Error reading user data",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
            }
        });
    }
}