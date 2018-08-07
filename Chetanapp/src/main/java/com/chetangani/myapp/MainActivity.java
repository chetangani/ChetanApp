package com.chetangani.myapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.chetangani.myapp.database.Database;
import com.chetangani.myapp.fragments.cards.AllCards;
import com.chetangani.myapp.fragments.cards.Card_Details;
import com.chetangani.myapp.fragments.cards.GetSet_CardDetails;
import com.chetangani.myapp.fragments.expenses.AddExpenses;
import com.chetangani.myapp.fragments.expenses.ViewDayExpenses;
import com.chetangani.myapp.fragments.expenses.ViewExpenses;
import com.chetangani.myapp.fragments.fueltracker.AllFuelDetails;
import com.chetangani.myapp.fragments.fueltracker.Fuel_Details;
import com.chetangani.myapp.fragments.fueltracker.GetSet_Fueldetails;
import com.chetangani.myapp.fragments.services.AllServices;

import java.util.Objects;

import static android.Manifest.permission.READ_PHONE_STATE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.chetangani.myapp.values.Constants.SEARCH_ID;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int RequestPermissionCode = 1;
    public static final String PREFS_NAME = "MyAppPrefsFile";

    Toolbar toolbar;
    private Fragment fragment;
    FragmentManager fragmentManager;
    Database database;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    TextView tv_user, tv_email;

    public enum Steps {
        FORM0(AllCards.class),
        FORM1(AllServices.class),
        FORM2(AllFuelDetails.class),
        FORM3(Fuel_Details.class),
        FORM4(Card_Details.class),
        FORM5(AddExpenses.class),
        FORM6(ViewDayExpenses.class),
        FORM7(ViewExpenses.class);

        private Class clazz;

        Steps(Class clazz) {
            this.clazz = clazz;
        }

        public Class getFragClass() {
            return clazz;
        }
    }

    @SuppressLint("CommitPrefEdits")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        fragmentManager = getSupportFragmentManager();

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

    private void loadscreen() {
        database = new Database(MainActivity.this);
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
        ft.replace(R.id.container_main, new ViewExpenses());
        ft.commit();
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
        ActivityCompat.requestPermissions(MainActivity.this, new String[]
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

    public Database getDatabase() {
        return this.database;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_cards:
                switchPopBackContent(Steps.FORM0);
                break;

            case R.id.nav_services:
                switchPopBackContent(Steps.FORM1);
                break;

            case R.id.nav_fueltrackers:
                switchPopBackContent(Steps.FORM2);
                break;

            case R.id.nav_expenses:
                switchPopBackContent(Steps.FORM7);
                break;

            case R.id.nav_logout:
                editor.putString("login", "No");
                editor.putString("name", "");
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();
                Intent intent = new Intent(MainActivity.this, LoginScreen.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void hidekeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showsnackbar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
    }

    /*public void switchfragment() {
        @SuppressLint("CommitTransaction")
        Fuel_Details fuelDetails = Fuel_Details.newInstance(sample);

        // Defines enter transition for all fragment views
        Slide slideTransition = new Slide(Gravity.END);
        slideTransition.setDuration(1000);
        sharedElementFragment2.setEnterTransition(slideTransition);

        // Defines enter transition only for shared element
        ChangeBounds changeBoundsTransition = (ChangeBounds) TransitionInflater.from(this).inflateTransition(R.transition.slide_change_bound);
        fuelDetails.setSharedElementEnterTransition(changeBoundsTransition);

        getFragmentManager().beginTransaction()
                .replace(R.id.container_main, fuelDetails)
                .addSharedElement(blueView, getString(R.string.blue_name))
                .commit();
    }*/

    public void switchFuelContent(Steps currentForm, GetSet_Fueldetails value) {
        Bundle bundle = new Bundle();
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        bundle.putSerializable(SEARCH_ID, value);
        fragment.setArguments(bundle);
        ft.replace(R.id.container_main, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void switchCardContent(Steps currentForm, GetSet_CardDetails value) {
        Bundle bundle = new Bundle();
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        bundle.putSerializable(SEARCH_ID, value);
        fragment.setArguments(bundle);
        ft.replace(R.id.container_main, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void switchExpensesContent(Steps currentForm, String value) {
        Bundle bundle = new Bundle();
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        bundle.putSerializable(SEARCH_ID, value);
        fragment.setArguments(bundle);
        ft.replace(R.id.container_main, fragment, currentForm.name());
        ft.addToBackStack(currentForm.name());
        ft.commit();
    }

    public void switchPopBackContent(Steps currentForm) {
        try {
            fragment = (Fragment) currentForm.getFragClass().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.container_main, fragment, currentForm.name());
        ft.commit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
