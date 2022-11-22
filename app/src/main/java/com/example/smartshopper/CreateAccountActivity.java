package com.example.smartshopper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.models.User;
import com.example.smartshopper.utilities.LocalStorage;
import com.google.android.material.internal.NavigationMenuItemView;

public class CreateAccountActivity extends AppCompatActivity {
  Button createAccountButton;
  EditText usernameET;
  EditText emailET;
  EditText passwordET;
  EditText confirmPasswordET;
  PlatformHelpers platformHelpers;
  Context context;
  LocalStorage localStorage;
  NavigationMenuItemView signinButton;
  CheckBox agreeToTerms;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_create_account);
    createAccountButton = findViewById(R.id.createAccountButton);
    usernameET = findViewById(R.id.editTextCreateUsername);
    emailET = findViewById(R.id.editTextCreateEmail);
    passwordET = findViewById(R.id.editTextCreatePassword);
    confirmPasswordET = findViewById(R.id.editTextConfirmPassword);
    signinButton = findViewById(R.id.nav_logout);
    context = this;
    platformHelpers = new PlatformHelpers(context);
    localStorage = new LocalStorage(context);
    agreeToTerms = findViewById(R.id.agreeToTermsCheckbox);

    createAccountButton.setOnClickListener(view -> {
      String username = usernameET.getText().toString();
      String email = emailET.getText().toString();
      String password = passwordET.getText().toString();
      String confirmPassword = confirmPasswordET.getText().toString();
      Log.v("Checkbox value", String.valueOf(agreeToTerms.isChecked()));

//        todo: would be nice if we checked for valid email with regex here
      if (username.equals("") || email.equals("") || password.equals("") || confirmPassword.equals("") || !agreeToTerms.isChecked()) {
        Toast.makeText(getApplicationContext(), "All fields are required. Please verify you have filled out all fields and resubmit.", Toast.LENGTH_LONG).show();
      }
      else if (!password.equals(confirmPassword)) {
        Toast.makeText(getApplicationContext(), "Password fields must match", Toast.LENGTH_LONG).show();
      }
      else {
        createAccount(username, email, password);
      }
    });
  }

  public void createAccount(String username, String email, String password) {
    try {
      platformHelpers.createAccount(username, email, password, userInterface -> {
        if (userInterface != null) {
//          TODO: this should lead to a follow tags activity that hasn't been created yet
          Toast.makeText(context, "Welcome to Smart Shopper "+ username + "!", Toast.LENGTH_LONG).show();
          signIn(userInterface);
          Intent mainActivityIntent = new Intent(context, MainActivity.class);
          context.startActivity(mainActivityIntent);
        }
        else {
          Toast.makeText(context, "Username or email taken. Please try again.", Toast.LENGTH_LONG).show();
        }
      });
    } catch (Exception error) {
      Log.v("CreateAccountActivity Error", error.toString());
    }
  }
  @SuppressLint({"RestrictedApi"})
  public void signIn(User user) {
    try {
      localStorage.setUser(user);
      signinButton.setTitle("Sign Out");
      Toast.makeText(getApplicationContext(), "Successful Login", Toast.LENGTH_LONG).show();
    } catch (Exception error) {
      Log.v("error", error.toString());
    }
  }
}
