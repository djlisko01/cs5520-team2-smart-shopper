package com.example.smartshopper;

import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ResetPasswordActivity extends AppCompatActivity {
  private Uri uri;
  private TextView resetText;
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_reset_password);
    Log.v("ResetPasswordActivity", "true");
    resetText = findViewById(R.id.resetText);
    uri = getIntent().getData();
    if (uri != null) {
      List<String> parameters = uri.getPathSegments();

      String param = parameters.get(parameters.size() - 1);

      resetText.setText(param);

    }
  }
}
