package com.ashishlakhmani.event.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;

import com.ashishlakhmani.event.R;
import com.ashishlakhmani.event.classes.LoginBackground;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
        setContentView(R.layout.activity_login);
        statusBarTask();

        SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        if (sharedPreferences.contains("username")) {
            Intent intent = new Intent(this, MainActivity.class);
            MainActivity.username = sharedPreferences.getString("username", "username");
            startActivity(intent);
            finish();
        }
    }

    public void onLogin(View view) {
        EditText username = (EditText) findViewById(R.id.username);
        EditText password = (EditText) findViewById(R.id.password);

        if (username.getText().toString().length() == 0 && password.getText().toString().length() == 0) {
            if (username.getText().toString().length() == 0) {
                username.setError("Please Enter a Username.");
                username.requestFocus();
            } else if (password.getText().toString().length() == 0) {
                password.setError("Please Enter a Password.");
                password.requestFocus();
            }
        } else {
            jumpToNext(username.getText().toString(), password.getText().toString());
        }
    }

    private void statusBarTask() {
        //To change color of status bar according to App Theme (Only API 21 & above)..
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = this.getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }
    }

    private void jumpToNext(String username, String password) {
        LoginBackground loginBackground = new LoginBackground(this, this);
        loginBackground.execute(username, password);
    }
}
