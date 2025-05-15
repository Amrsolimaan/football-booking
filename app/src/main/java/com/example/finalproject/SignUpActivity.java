// SignUpActivity.java
package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SignUpActivity extends AppCompatActivity {

    private TextInputEditText emailInput, passwordInput,confirmPasswordInput ,NameInput,addressInput,phoneInput;
    private Button signupButton;
    private TextView loginText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        emailInput = findViewById(R.id.email_input);
        NameInput = findViewById(R.id.nameinput);
        phoneInput = findViewById(R.id.phone_input);
        addressInput = findViewById(R.id.address_input);
        passwordInput = findViewById(R.id.password_input);
        confirmPasswordInput = findViewById(R.id.confirm_password_input);
        signupButton = findViewById(R.id.signup_button);
        loginText = findViewById(R.id.login_text);


        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email    = emailInput.getText().toString().trim();
                String name  = NameInput.getText().toString().trim();
                String address   = addressInput.getText().toString().trim();
                String phone     = phoneInput.getText().toString().trim();
                String password  = passwordInput.getText().toString().trim();
                String confirmpass = confirmPasswordInput.getText().toString().trim();

                if (email.isEmpty() ||name.isEmpty() ||
                        address.isEmpty() || phone.isEmpty() || password.isEmpty() || confirmpass.isEmpty()) {
                    Toast.makeText(SignUpActivity.this, "Please fill in all fields", Toast.LENGTH_LONG).show();
                    return;
                }

                if (!password.equals(confirmpass)) {
                    Toast.makeText(SignUpActivity.this, "Passwords do not match", Toast.LENGTH_LONG).show();
                    return;
                }

                try {
                    JSONObject user = new JSONObject();
                    user.put("email", email);
                    user.put("name", name);
                    user.put("address", address);
                    user.put("phone", phone);
                    user.put("password", password);

                    JSONArray usersArray;

                    FileInputStream fis = null;
                    try {
                        fis = openFileInput("users.json");
                        byte[] data = new byte[fis.available()];
                        fis.read(data);
                        fis.close();
                        String jsonString = new String(data);
                        usersArray = new JSONArray(jsonString);

                    /**    [
                        {"email":"a@a.com", "name":"Ali"},
                        {"email":"b@b.com", "name":"Omar"}

] **/

                    } catch (Exception e) {
                        usersArray = new JSONArray();
                    }

                    usersArray.put(user);

                    FileOutputStream fos = openFileOutput("users.json", MODE_PRIVATE);
                    fos.write(usersArray.toString().getBytes());
                    fos.close();

                    Toast.makeText(SignUpActivity.this, "User registered successfully", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                    finish();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(SignUpActivity.this, "Error saving user data", Toast.LENGTH_LONG).show();
                }
            }
        });


        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(SignUpActivity.this, "Log in clicked", Toast.LENGTH_SHORT).show();

                 startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                 finish();
            }
        });
    }
}
