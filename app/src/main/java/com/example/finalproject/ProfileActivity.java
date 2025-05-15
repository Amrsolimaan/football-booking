package com.example.finalproject;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.*;

public class ProfileActivity extends AppCompatActivity {
    private static final int REQ_PICK_IMAGE = 1001;

    private ImageView imgAvatar;
    private ImageButton btnCamera, btnEditName, btnEditPhone, btnEditAddress;
    private TextView tvHello, tvName, tvPhone, tvAddress;
    private Button btnChangePassword;

    private String userEmail;
    private JSONObject currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile); // هو نفس اللي عملناه في XML

        // Bind views
        imgAvatar        = findViewById(R.id.imgAvatar);
        btnCamera        = findViewById(R.id.btnCamera);
        tvHello          = findViewById(R.id.tvHello);
        tvName           = findViewById(R.id.tvName);
        tvPhone          = findViewById(R.id.tvPhone);
        tvAddress        = findViewById(R.id.tvAddress);
        btnEditName      = findViewById(R.id.btnEditName);
        btnEditPhone     = findViewById(R.id.btnEditPhone);
        btnEditAddress   = findViewById(R.id.btnEditAddress);
        btnChangePassword= findViewById(R.id.btnChangePassword);

        // استقبل الإيميل
        userEmail = getIntent().getStringExtra("email");

        // حمل بيانات المستخدم من JSON
        loadUserFromJson();

        // عرضها في الواجهة
        refreshUI();

        // اختيار صورة جديدة
        btnCamera.setOnClickListener(v -> {
            Intent pick = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pick, REQ_PICK_IMAGE);
        });

        // تعديل الحقول
        btnEditName.setOnClickListener(v -> showEditDialog("الاسم", tvName.getText().toString(), newVal -> {
            updateField("name", newVal);
        }));
        btnEditPhone.setOnClickListener(v -> showEditDialog("رقم تليفونك", tvPhone.getText().toString(), newVal -> {
            updateField("phone", newVal);
        }));
        btnEditAddress.setOnClickListener(v -> showEditDialog("العنوان", tvAddress.getText().toString(), newVal -> {
            updateField("address", newVal);
        }));


        btnChangePassword.setOnClickListener(v -> {
            // فتح صفحة تغيير كلمة السر وتمرير الإيميل
            Intent intent = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
            intent.putExtra("email", userEmail); // تمرير الإيميل للتأكد من المستخدم
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int req, int res, @Nullable Intent data) {
        super.onActivityResult(req, res, data);
        if (req == REQ_PICK_IMAGE && res == RESULT_OK && data != null) {
            Uri uri = data.getData();
            imgAvatar.setImageURI(uri);
            saveField("avatarUri", uri.toString());
        }
    }

    private void loadUserFromJson() {
        try {
            FileInputStream fis = openFileInput("users.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            JSONArray arr = new JSONArray(new String(data));

            // ابحث عن الكائن اللي إيميله userEmail
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("email").equalsIgnoreCase(userEmail)) {
                    currentUser = obj;
                    break;
                }
            }
            if (currentUser == null) {
                currentUser = new JSONObject();
                currentUser.put("email", userEmail);
            }
        } catch (Exception e) {
            e.printStackTrace();
            currentUser = new JSONObject();
        }
    }

    private void refreshUI() {
        String name    = currentUser.optString("name", "");
        String phone   = currentUser.optString("phone", "");
        String address = currentUser.optString("address", "");
        String avatar  = currentUser.optString("avatarUri", null);

        tvHello.setText(name);
        tvName.setText(name);
        tvPhone.setText(phone);
        tvAddress.setText(address);

        if (avatar != null && !avatar.isEmpty()) {
            imgAvatar.setImageURI(Uri.parse(avatar));
        }
    }

    private void showEditDialog(String title, String current, OnSaved callback) {
        EditText input = new EditText(this);
        input.setText(current);
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(input)
                .setPositiveButton("حفظ", (d, w) -> callback.onSaved(input.getText().toString()))
                .setNegativeButton("إلغاء", null)
                .show();
    }

    private void updateField(String key, String newVal) {
        try {
            currentUser.put(key, newVal);
            saveJsonToFile();
            refreshUI();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveField(String key, String newVal) {
        updateField(key, newVal);
    }

    private void saveJsonToFile() {
        try {
            // اقرأ كامل المصفوفة
            FileInputStream fis = openFileInput("users.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();
            JSONArray arr = new JSONArray(new String(data));

            // عوّض العنصر المناسب
            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = arr.getJSONObject(i);
                if (obj.getString("email").equalsIgnoreCase(userEmail)) {
                    arr.put(i, currentUser);
                    break;
                }
            }

            // اكتب الملف من جديد
            FileOutputStream fos = openFileOutput("users.json", MODE_PRIVATE);
            fos.write(arr.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    interface OnSaved { void onSaved(String newValue); }
}
