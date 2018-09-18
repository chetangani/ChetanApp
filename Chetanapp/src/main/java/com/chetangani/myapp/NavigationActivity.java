package com.chetangani.myapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.BlankFragment;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.chetangani.myapp.values.Constants.CARDS_LAYOUT;
import static com.chetangani.myapp.values.Constants.EXPENSES_LAYOUT;
import static com.chetangani.myapp.values.Constants.FUEL_LAYOUT;
import static com.chetangani.myapp.values.Constants.LAYOUT;
import static com.chetangani.myapp.values.Constants.SERVICES_LAYOUT;

public class NavigationActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private static final int RequestPermissionCode = 1;
    public static final String PREFS_NAME = "MyAppPrefsFile";

    Toolbar toolbar;
    Database database;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    TextView tv_user, tv_email;

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        //noinspection deprecation
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPref.edit();

        navigationView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                tv_user = findViewById(R.id.user_name);
                tv_user.setText(sharedPref.getString("name", ""));
                tv_email = findViewById(R.id.user_email);
                if (!sharedPref.getString("email", "").equals("")) {
                    tv_email.setText(sharedPref.getString("email", ""));
                } else tv_email.setVisibility(View.GONE);
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkPermissionsMandAbove();
            }
        }, 1000);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_cards:
                startActivityprocess(NavigationActivity.this, CARDS_LAYOUT);
                break;

            case R.id.nav_services:
                startActivityprocess(NavigationActivity.this, SERVICES_LAYOUT);
                break;

            case R.id.nav_fueltrackers:
                startActivityprocess(NavigationActivity.this, FUEL_LAYOUT);
                break;

            case R.id.nav_expenses:
                startActivityprocess(NavigationActivity.this, EXPENSES_LAYOUT);
                break;

            case R.id.nav_logout:
                editor.putString("login", "No");
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();
                Intent intent = new Intent(NavigationActivity.this, LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void startActivityprocess(Context context, String value) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(LAYOUT, value);
        startActivity(intent);
    }

    @TargetApi(23)
    public void checkPermissionsMandAbove() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkPermission()){
                loadscreen();
            } else requestPermission();
        } else {
            loadscreen();
        }
    }

    @TargetApi(23)
    private void requestPermission() {
        ActivityCompat.requestPermissions(NavigationActivity.this, new String[]
                {
                        WRITE_EXTERNAL_STORAGE,
                        READ_PHONE_STATE
                }, RequestPermissionCode);
    }

    @TargetApi(23)
    private boolean checkPermission() {
        int FirstPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int SecondPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), READ_PHONE_STATE);
        return FirstPermissionResult == PackageManager.PERMISSION_GRANTED &&
                SecondPermissionResult == PackageManager.PERMISSION_GRANTED;
    }

    @TargetApi(23)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case RequestPermissionCode:
                if (grantResults.length > 0) {
                    boolean ReadStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadPhoneStatePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (ReadStoragePermission  && ReadPhoneStatePermission) {
                        loadscreen();
                    } else checkPermissionsMandAbove();
                }
                break;
        }
    }

    private void loadscreen() {
        database = new Database(NavigationActivity.this);
        database.open();

        Cursor banks = database.getbanks();
        if (banks.getCount() == 0) {
            String[] bank_name = getResources().getStringArray(R.array.bank_names);
            String[] bank_code = getResources().getStringArray(R.array.bank_codes);
            String[] bank_images = getResources().getStringArray(R.array.bank_images);
            for (int i = 0; i < bank_name.length; i++) {
                database.insertbankdetails(bank_name[i], bank_code[i], bank_images[i]);
            }
        }

        Cursor category = database.getCategorydetails();
        if (category.getCount() == 0) {
            String[] categories = getResources().getStringArray(R.array.category_list);
            String[] category_code = getResources().getStringArray(R.array.category_code_list);
            for (int i = 0; i < categories.length; i++) {
                database.insertcategorydetails(categories[i], category_code[i]);
            }
        }

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.navigate_container, new BlankFragment());
        ft.commit();
    }
}
