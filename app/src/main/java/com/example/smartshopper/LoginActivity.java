package com.example.smartshopper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
  }

  public void sendToForgotPasswordActivity(View view) {
    Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(forgotPasswordIntent);
  }

  public void sendToCreateAccountActivity(View view) {
    Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
    startActivity(createAccountIntent);
  }

}
