package com.example.finalproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class FieldDetailsActivity extends AppCompatActivity {

    private ImageView fieldImage;
    private TextView fieldNameText, dateText, timeText, priceText, ownerNameText, phoneText;
    private Button confirmButton;
    private String fieldName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_details);

        // تهيئة العناصر
        fieldImage = findViewById(R.id.field_image);
        fieldNameText = findViewById(R.id.field_name);
        dateText = findViewById(R.id.date_text);
        timeText = findViewById(R.id.time_text);
        priceText = findViewById(R.id.price_text);
        ownerNameText = findViewById(R.id.owner_name);
        phoneText = findViewById(R.id.phone_number);
        confirmButton = findViewById(R.id.confirm_button);

        // الحصول على اسم الملعب من Intent
        fieldName = getIntent().getStringExtra("field_name");

        // ضبط بيانات الملعب بناءً على الاسم
        setFieldDetails(fieldName);

        // إعداد زر تأكيد الحجز
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FieldDetailsActivity.this, "تم تأكيد حجز " + fieldName, Toast.LENGTH_SHORT).show();
                // يمكن إضافة كود هنا للانتقال إلى صفحة تأكيد الحجز أو إرسال بيانات الحجز للخادم
            }
        });
    }

    private void setFieldDetails(String name) {
        // تهيئة متغيرات لبيانات الملعب
        int imageResource = 0;
        String displayName = "";
        String date = "";
        String time = "";
        String price = "";
        String owner = "";
        String phone = "";

        // جلب بيانات الملعب بناءً على الاسم
        if (name.equals("ملعب النادي الأهلي")) {
            imageResource = R.drawable.ahly;
            displayName = "ملعب النادي الأهلي";
            date = "24 أبريل 2024";
            time = "٦:٠٠ م - ٧:٠٠ م";
            price = "سعر 500 جم";
            owner = "Amr Soliman";
            phone = "0123456789";
        } else if (name.equals("ملعب الزمالك")) {
            imageResource = R.drawable.zamalek;
            displayName = "ملعب الزمالك";
            date = "25 أبريل 2024";
            time = "٧:٠٠ م - ٨:٠٠ م";
            price = "سعر 450 جم";
            owner = "Mohamed Ahmed";
            phone = "0111222333";
        } else if (name.equals("ملعب الأهلي السعودي")) {
            imageResource = R.drawable.alahli_saudi;
            displayName = "ملعب الأهلي السعودي";
            date = "26 أبريل 2024";
            time = "٧:٣٠ م - ٨:٣٠ م";
            price = "سعر 550 جم";
            owner = "Khalid Mohammed";
            phone = "0555666777";
        } else if (name.equals("ملعب بيراميدز")) {
            imageResource = R.drawable.pyramids;
            displayName = "ملعب بيراميدز";
            date = "27 أبريل 2024";
            time = "٥:٠٠ م - ٦:٠٠ م";
            price = "سعر 480 جم";
            owner = "Ahmed Hassan";
            phone = "0101234567";
        } else if (name.equals("ملعب الإسماعيلي")) {
            imageResource = R.drawable.ismaily;
            displayName = "ملعب الإسماعيلي";
            date = "28 أبريل 2024";
            time = "٤:٠٠ م - ٥:٠٠ م";
            price = "سعر 420 جم";
            owner = "Mahmoud Ali";
            phone = "0122333444";
        } else if (name.equals("ملعب فيوتشر")) {
            imageResource = R.drawable.future;
            displayName = "ملعب فيوتشر";
            date = "29 أبريل 2024";
            time = "٦:٣٠ م - ٧:٣٠ م";
            price = "سعر 470 جم";
            owner = "Tarek Youssef";
            phone = "0133444555";
        } else {
            // ملعب افتراضي في حالة عدم التوافق
            imageResource = R.drawable.ahly;
            displayName = name;
            date = "25 أبريل 2024";
            time = "٨:٠٠ م - ٩:٠٠ م";
            price = "سعر 400 جم";
            owner = "مالك الملعب";
            phone = "01000000000";
        }

        // تعيين البيانات للعناصر في الواجهة
        fieldImage.setImageResource(imageResource);
        fieldNameText.setText(displayName);
        dateText.setText(date);
        timeText.setText(time);
        priceText.setText(price);
        ownerNameText.setText(owner);
        phoneText.setText(phone);
    }
}