package com.example.smartshopper;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.smartshopper.common.PlatformHelpers;
import com.example.smartshopper.utilities.LocalStorage;

public class LoginActivity extends MenuActivity {
  Button loginButton;
  PlatformHelpers platformHelpers;
  EditText emailAddressET;
  EditText passwordET;
  LocalStorage localStorage;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);

    platformHelpers = new PlatformHelpers(getApplicationContext());
    loginButton = findViewById(R.id.loginButton);
    loginButton.setOnClickListener(view -> {
      loginUser();
    });
  }

  @SuppressLint("RestrictedApi")
  public void loginUser() {
    emailAddressET = findViewById(R.id.editTextEmailAddress);
    passwordET = findViewById(R.id.editTextPassword);
    String emailAddress = emailAddressET.getText().toString();
    String password = passwordET.getText().toString();
    localStorage = new LocalStorage(this);

    platformHelpers.checkEmail(emailAddress, password, userInterface -> {
      if (userInterface != null) {
        localStorage.setUser(userInterface);
        Toast.makeText(getApplicationContext(), "Welcome back "+ userInterface.getUsername() + "!", Toast.LENGTH_LONG).show();
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        this.startActivity(mainActivityIntent);
      } else {
        Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
      }
    });
  }

  public void sendToForgotPasswordActivity(View view) {
    Intent forgotPasswordIntent = new Intent(this, ForgotPasswordActivity.class);
    startActivity(forgotPasswordIntent);
  }

  public void sendToCreateAccountActivity(View view) {
//    TODO: put this back when done debugging/implementing tags
//    Intent createAccountIntent = new Intent(this, CreateAccountActivity.class);
//    startActivity(createAccountIntent);
      Intent followTagsIntent = new Intent(this, FollowTagsActivity.class);
      startActivity(followTagsIntent);
  }

}
