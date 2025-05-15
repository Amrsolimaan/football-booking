package com.example.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import org.json.JSONArray;
import org.json.JSONObject;

import android.text.Editable;
import android.text.TextWatcher;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TextView tvGreeting;
    private CardView searchCard;
    private Button ahlyBookButton, zamalekBookButton, alahliSaudiBookButton, pyramidsBookButton,
            ismailyBookButton, futureBookButton;
    private CardView ahlyFieldCard, zamalekFieldCard, alahliSaudiFieldCard, pyramidsFieldCard,
            ismailyFieldCard, futureFieldCard;
    private EditText searchEditText;
    private LinearLayout searchResultsContainer;
    private String userName;
    private String userEmail;
    private SessionManager sessionManager;

    // Map to store all field names and their corresponding views
    private Map<String, View> fieldsMap = new HashMap<>();
    private List<String> fieldNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // تهيئة مدير الجلسات
        sessionManager = new SessionManager(this);

        // Initialize views
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvGreeting = findViewById(R.id.tvGreeting);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        searchCard = findViewById(R.id.search_card);
        searchEditText = findViewById(R.id.search_edit_text);
        searchResultsContainer = findViewById(R.id.search_results_container);

        // Initialize field cards
        ahlyFieldCard = findViewById(R.id.ahly_field_card);
        zamalekFieldCard = findViewById(R.id.zamalek_field_card);
        alahliSaudiFieldCard = findViewById(R.id.alahli_saudi_field_card);
        pyramidsFieldCard = findViewById(R.id.pyramids_field_card);
        ismailyFieldCard = findViewById(R.id.ismaily_field_card);
        futureFieldCard = findViewById(R.id.future_field_card);

        // Initialize book buttons
        ahlyBookButton = findViewById(R.id.ahly_book_button);
        zamalekBookButton = findViewById(R.id.zamalek_book_button);
        alahliSaudiBookButton = findViewById(R.id.alahli_saudi_book_button);
        pyramidsBookButton = findViewById(R.id.pyramids_book_button);
        ismailyBookButton = findViewById(R.id.ismaily_book_button);
        futureBookButton = findViewById(R.id.future_book_button);

        // Get user information from Intent
        userName = getIntent().getStringExtra("username");
        userEmail = getIntent().getStringExtra("email");

        if (userName != null) {
            tvGreeting.setText("أهلا " + userName);
            // إذا لم يتم تمرير البريد الإلكتروني عبر Intent، قم بالبحث عنه في ملف المستخدمين
            if (userEmail == null) {
                findUserEmailByName();
            }
        }

        // Setup fields data
        setupFieldsData();

        // Setup navigation drawer
        setupNavigationDrawer();

        // Setup search functionality
        setupSearch();

        // Set click listeners for field cards and book buttons
        setFieldClickListeners();
    }

    private void setupNavigationDrawer() {
        // Initialize ActionBarDrawerToggle for integrating the drawer with the toolbar
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        updateNavigationHeader();
    }

    private void updateNavigationHeader() {
        View headerView = navigationView.getHeaderView(0);
        TextView navHeaderName = headerView.findViewById(R.id.nav_header_name);
        TextView navHeaderEmail = headerView.findViewById(R.id.nav_header_email);

        navHeaderName.setText(userName);
        if (userEmail != null) {
            navHeaderEmail.setText(userEmail);
        }
    }

    private void setupFieldsData() {
        fieldNames.add("ملعب النادي الأهلي");
        fieldNames.add("ملعب الزمالك");
        fieldNames.add("ملعب الأهلي السعودي");
        fieldNames.add("ملعب بيراميدز");
        fieldNames.add("ملعب الإسماعيلي");
        fieldNames.add("ملعب فيوتشر");

        fieldsMap.put("ملعب النادي الأهلي", ahlyFieldCard);
        fieldsMap.put("ملعب الزمالك", zamalekFieldCard);
        fieldsMap.put("ملعب الأهلي السعودي", alahliSaudiFieldCard);
        fieldsMap.put("ملعب بيراميدز", pyramidsFieldCard);
        fieldsMap.put("ملعب الإسماعيلي", ismailyFieldCard);
        fieldsMap.put("ملعب فيوتشر", futureFieldCard);
    }

    private void setupSearch() {
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterSearchResults(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void filterSearchResults(String query) {
        searchResultsContainer.removeAllViews();
        searchResultsContainer.setVisibility(query.isEmpty() ? View.GONE : View.VISIBLE);

        if (query.isEmpty()) {
            return;
        }

        for (String fieldName : fieldNames) {
            if (fieldName.contains(query)) {
                TextView resultItem = new TextView(this);
                resultItem.setText(fieldName);
                resultItem.setTextSize(18);
                resultItem.setPadding(30, 20, 30, 20);
                resultItem.setBackgroundResource(android.R.color.white);
                resultItem.setTextColor(getResources().getColor(R.color.soccer_green));

                View divider = new View(this);
                divider.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));
                divider.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, 1));

                resultItem.setOnClickListener(v -> {
                    openFieldDetails(fieldName);
                    searchEditText.setText("");
                    searchResultsContainer.setVisibility(View.GONE);
                });

                searchResultsContainer.addView(resultItem);
                searchResultsContainer.addView(divider);
            }
        }

        if (searchResultsContainer.getChildCount() == 0) {
            TextView noResults = new TextView(this);
            noResults.setText("لا توجد نتائج");
            noResults.setTextSize(18);
            noResults.setPadding(30, 20, 30, 20);
            noResults.setBackgroundResource(android.R.color.white);
            noResults.setTextColor(getResources().getColor(android.R.color.darker_gray));
            searchResultsContainer.addView(noResults);
        }
    }

    private void findUserEmailByName() {
        try {
            FileInputStream fis = openFileInput("users.json");
            byte[] data = new byte[fis.available()];
            fis.read(data);
            fis.close();

            JSONArray usersArray = new JSONArray(new String(data));

            for (int i = 0; i < usersArray.length(); i++) {
                JSONObject user = usersArray.getJSONObject(i);
                if (user.getString("name").equals(userName)) {
                    userEmail = user.getString("email");
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFieldClickListeners() {
        ahlyFieldCard.setOnClickListener(v -> openFieldDetails("ملعب النادي الأهلي"));
        zamalekFieldCard.setOnClickListener(v -> openFieldDetails("ملعب الزمالك"));
        alahliSaudiFieldCard.setOnClickListener(v -> openFieldDetails("ملعب الأهلي السعودي"));
        pyramidsFieldCard.setOnClickListener(v -> openFieldDetails("ملعب بيراميدز"));
        ismailyFieldCard.setOnClickListener(v -> openFieldDetails("ملعب الإسماعيلي"));
        futureFieldCard.setOnClickListener(v -> openFieldDetails("ملعب فيوتشر"));

        ahlyBookButton.setOnClickListener(v -> openFieldDetails("ملعب النادي الأهلي"));
        zamalekBookButton.setOnClickListener(v -> openFieldDetails("ملعب الزمالك"));
        alahliSaudiBookButton.setOnClickListener(v -> openFieldDetails("ملعب الأهلي السعودي"));
        pyramidsBookButton.setOnClickListener(v -> openFieldDetails("ملعب بيراميدز"));
        ismailyBookButton.setOnClickListener(v -> openFieldDetails("ملعب الإسماعيلي"));
        futureBookButton.setOnClickListener(v -> openFieldDetails("ملعب فيوتشر"));
    }

    /**
     * دالة مساعدة لفتح صفحة تفاصيل الملعب
     * @param fieldName اسم الملعب المراد عرض تفاصيله
     */
    private void openFieldDetails(String fieldName) {
        Intent intent = new Intent(home.this, FieldDetailsActivity.class);
        intent.putExtra("field_name", fieldName);
        startActivity(intent);
    }

    // تسجيل الخروج من التطبيق
    private void logoutUser() {
        // مسح بيانات الجلسة
        sessionManager.logout();

        // الانتقال إلى شاشة تسجيل الدخول
        Intent intent = new Intent(home.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Toast.makeText(this, "أنت بالفعل في الصفحة الرئيسية", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_map) {
            Toast.makeText(this, "الانتقال إلى صفحة الخرائط", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_account) {
            Intent i = new Intent(home.this, ProfileActivity.class);
            i.putExtra("email", userEmail);
            startActivity(i);
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "الإعدادات", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "عن التطبيق", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_logout) {
            // تسجيل الخروج
            logoutUser();
            return true;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        // Close drawer on back press if it's open, otherwise do normal back operation
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}