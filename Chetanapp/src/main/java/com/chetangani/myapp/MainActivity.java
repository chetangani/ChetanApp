package com.chetangani.myapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

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

import static com.chetangani.myapp.values.Constants.CARDS_LAYOUT;
import static com.chetangani.myapp.values.Constants.EXPENSES_LAYOUT;
import static com.chetangani.myapp.values.Constants.FUEL_LAYOUT;
import static com.chetangani.myapp.values.Constants.LAYOUT;
import static com.chetangani.myapp.values.Constants.SEARCH_ID;
import static com.chetangani.myapp.values.Constants.SERVICES_LAYOUT;

public class MainActivity extends AppCompatActivity {
    public static final String PREFS_NAME = "MyAppPrefsFile";

    private Fragment fragment;
    FragmentManager fragmentManager;
    Database database;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public static String layout="";

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

        Intent intent = getIntent();
        layout = Objects.requireNonNull(intent.getExtras()).getString(LAYOUT);

        fragmentManager = getSupportFragmentManager();

        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPref.edit();

        database = new Database(MainActivity.this);
        database.open();

        if (!TextUtils.isEmpty(layout)) {
            switch (layout) {
                case CARDS_LAYOUT:
                    switchPopBackContent(Steps.FORM0);
                    break;

                case SERVICES_LAYOUT:
                    switchPopBackContent(Steps.FORM1);
                    break;

                case FUEL_LAYOUT:
                    switchPopBackContent(Steps.FORM2);
                    break;

                case EXPENSES_LAYOUT:
                    switchPopBackContent(Steps.FORM7);
                    break;
            }
        } else finish();
    }

    public Database getDatabase() {
        return this.database;
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

    public void setActionBarTitle(String title) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        database.close();
    }
}
