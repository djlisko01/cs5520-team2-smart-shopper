package com.example.smartshopper;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.se.omapi.Session;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class ForgotPasswordActivity extends AppCompatActivity {
  TextView linkText;
  Button resetPasswordButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_forgot_password);
    linkText = findViewById(R.id.resetLink);
    resetPasswordButton = findViewById(R.id.passwordResetButton);
    Log.v("ForgotPasswordActivity", "true");

    resetPasswordButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        sendEmail();
//        Intent resetIntent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
//        startActivity(resetIntent);
      }
    });


    linkText.setOnClickListener(view -> {

      String targetUrl = (String) linkText.getText();
//      if (!targetUrl.startsWith("http://")) {
//        targetUrl = "http://" + targetUrl;
//      }
      Intent navigate = new Intent(Intent.ACTION_VIEW);
      navigate.setData(Uri.parse(targetUrl));
      this.startActivity(navigate);
    });
  }

  protected void sendEmail() {
    Log.i("Send email", "");

//    String[] TO = {"lord.k@northeastern.edu"};
//    String[] CC = {"xyz@gmail.com"};
//    Intent emailIntent = new Intent(Intent.ACTION_SEND);
//    emailIntent.setData(Uri.parse("mailto:"));
//    emailIntent.setType("text/plain");
//
//
//
//    emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
////    emailIntent.putExtra(Intent.EXTRA_CC, CC);
//    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
//    emailIntent.putExtra(Intent.EXTRA_TEXT, "Email message goes here");
//
//    try {
//      Log.v("emailIntent", emailIntent.toString());
//      startActivity(Intent.createChooser(emailIntent, "Send mail..."));
//      finish();
//      Log.i("Finished sending email...", "");
//    } catch (android.content.ActivityNotFoundException ex) {
//      Toast.makeText(this,
//        "There is no email client installed.", Toast.LENGTH_SHORT).show();
//    }
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 122);
    }
    SmsManager smsManager = SmsManager.getDefault();
    smsManager.sendTextMessage("12072271792", null, "hi!", null, null);
  }
}
