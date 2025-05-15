package com.example.finalproject;
import android.content.Context;

import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class SessionManager {
    private static final String SESSION_FILE = "session.json";
    private Context context;

    public SessionManager(Context context) {
        this.context = context;
    }

    // حفظ بيانات تسجيل الدخول
    public void saveLoginSession(String email, String username) {
        try {
            JSONObject sessionData = new JSONObject();
            sessionData.put("isLoggedIn", true);
            sessionData.put("email", email);
            sessionData.put("username", username);

            FileOutputStream fos = context.openFileOutput(SESSION_FILE, Context.MODE_PRIVATE);
            fos.write(sessionData.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // التحقق مما إذا كان المستخدم مسجل الدخول
    public boolean isLoggedIn() {
        File file = new File(context.getFilesDir(), SESSION_FILE);
        if (!file.exists()) {
            return false;
        }

        try {
            FileInputStream fis = context.openFileInput(SESSION_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            JSONObject sessionData = new JSONObject(new String(data));
            return sessionData.getBoolean("isLoggedIn");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // الحصول على معلومات المستخدم المسجل الدخول
    public JSONObject getUserDetails() {
        try {
            FileInputStream fis = context.openFileInput(SESSION_FILE);
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            return new JSONObject(new String(data));
        } catch (Exception e) {
            e.printStackTrace();
            return new JSONObject();
        }
    }

    // تسجيل الخروج - مسح البيانات المخزنة
    public void logout() {
        try {
            JSONObject sessionData = new JSONObject();
            sessionData.put("isLoggedIn", false);

            FileOutputStream fos = context.openFileOutput(SESSION_FILE, Context.MODE_PRIVATE);
            fos.write(sessionData.toString().getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}