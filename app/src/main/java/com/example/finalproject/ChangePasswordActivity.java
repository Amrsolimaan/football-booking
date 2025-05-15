package com.example.finalproject;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChangePasswordActivity extends AppCompatActivity {

    private static final String TAG = "ChangePasswordActivity";
    private static final String USERS_FILE_NAME = "users.json";

    private EditText currentPasswordEditText;
    private EditText newPasswordEditText;
    private EditText confirmPasswordEditText;
    private Button saveButton;
    private Button cancelButton;

    private String userEmail;
    private JSONObject currentUser;
    private JSONArray usersArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        // استقبال البريد الإلكتروني من الشاشة السابقة
        userEmail = getIntent().getStringExtra("email");
        if (userEmail == null || userEmail.isEmpty()) {
            Toast.makeText(this, "خطأ في معلومات المستخدم", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // ربط عناصر الواجهة
        currentPasswordEditText = findViewById(R.id.et_current_password);
        newPasswordEditText = findViewById(R.id.et_new_password);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        saveButton = findViewById(R.id.btn_save);
        cancelButton = findViewById(R.id.btn_cancel);

        // تحميل بيانات المستخدم
        if (!loadUserData()) {
            Toast.makeText(this, "فشل في تحميل بيانات المستخدم", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // إعداد أحداث الأزرار
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInputs()) {
                    changePassword();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private boolean loadUserData() {
        try {
            // قراءة ملف المستخدمين
            FileInputStream fis = openFileInput(USERS_FILE_NAME);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            // تحويل البيانات إلى مصفوفة JSON
            usersArray = new JSONArray(new String(data));

            // البحث عن المستخدم الحالي باستخدام البريد الإلكتروني
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("email").equalsIgnoreCase(userEmail)) {
                    currentUser = user;
                    return true;
                }
            }

            Log.e(TAG, "لم يتم العثور على المستخدم: " + userEmail);
            return false;
        } catch (Exception e) {
            Log.e(TAG, "خطأ في قراءة ملف المستخدمين", e);
            return false;
        }
    }

    private boolean validateInputs() {
        String currentPassword = currentPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // التحقق من عدم وجود حقول فارغة
        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "جميع الحقول مطلوبة", Toast.LENGTH_SHORT).show();
            return false;
        }

        // التحقق من صحة كلمة المرور الحالية
        String storedPassword = currentUser.optString("password", "");
        if (!storedPassword.equals(currentPassword)) {
            Toast.makeText(this, "كلمة المرور الحالية غير صحيحة", Toast.LENGTH_SHORT).show();
            return false;
        }

        // التحقق من تطابق كلمتي المرور الجديدتين
        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "كلمتا المرور الجديدتين غير متطابقتين", Toast.LENGTH_SHORT).show();
            return false;
        }

        // التحقق من قوة كلمة المرور (اختياري)
        if (newPassword.length() < 6) {
            Toast.makeText(this, "يجب أن تتكون كلمة المرور من 6 أحرف على الأقل", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void changePassword() {
        try {
            String newPassword = newPasswordEditText.getText().toString().trim();

            // تحديث كلمة المرور للمستخدم الحالي
            currentUser.put("password", newPassword);

            // تحديث المستخدم في المصفوفة
            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("email").equalsIgnoreCase(userEmail)) {
                    usersArray.put(i, currentUser);
                    break;
                }
            }

            // حفظ المصفوفة المحدثة في الملف
            FileOutputStream fos = openFileOutput(USERS_FILE_NAME, MODE_PRIVATE);
            fos.write(usersArray.toString().getBytes());
            fos.close();

            Toast.makeText(this, "تم تحديث كلمة المرور بنجاح", Toast.LENGTH_SHORT).show();
            finish();
        } catch (Exception e) {
            Log.e(TAG, "خطأ في تحديث كلمة المرور", e);
            Toast.makeText(this, "فشل في تحديث كلمة المرور", Toast.LENGTH_SHORT).show();
        }
    }
}