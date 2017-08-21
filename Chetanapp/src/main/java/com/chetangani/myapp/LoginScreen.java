package com.chetangani.myapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {
    public static final String PREFS_NAME = "MyAppPrefsFile";

    TextInputLayout til_name, til_email, til_password;
    EditText et_name, et_email, et_password;
    Button signup_btn;
    String name="", email="", password="";
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        til_name = (TextInputLayout) findViewById(R.id.til_signup_name);
        til_email = (TextInputLayout) findViewById(R.id.til_signup_email);
        til_password = (TextInputLayout) findViewById(R.id.til_signup_password);
        et_name = (EditText) findViewById(R.id.et_signup_name);
        et_email = (EditText) findViewById(R.id.et_signup_email);
        et_password = (EditText) findViewById(R.id.et_signup_password);
        signup_btn = (Button) findViewById(R.id.signup_btn);

        sharedPref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        editor = sharedPref.edit();

        if (sharedPref.getString("login", "").equals("Yes")) {
            Intent intent = new Intent(LoginScreen.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        et_password.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    signup_details(v);
                }
                return false;
            }
        });

        signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup_details(v);
            }
        });
    }

    private void signup_details(View view) {
        name = et_name.getText().toString();
        if (!name.equals("")) {
            email = et_email.getText().toString();
            password = et_password.getText().toString();
            if (!password.equals("")) {
                String login = "Yes";
                editor.putString("login", login);
                editor.putString("name", name);
                editor.putString("email", email);
                editor.putString("password", password);
                editor.commit();
                hidekeyboard();
                Intent intent = new Intent(LoginScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else showsnackbar(view, "Enter Password", et_password);
        } else showsnackbar(view, "Enter User Name", et_name);
    }

    public void hidekeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        } catch (Exception e) {
        }
    }

    public void showsnackbar(View view, String message, EditText editText) {
        Snackbar.make(view, message, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        editText.setError(message);
    }
}
