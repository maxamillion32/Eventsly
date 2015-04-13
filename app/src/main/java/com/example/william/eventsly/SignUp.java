package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class SignUp extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onCancelSignUpClick(View view) throws InterruptedException {
        Intent getTitleScreenIntent = new Intent(this, Title.class);

        startActivity(getTitleScreenIntent);

        wait(2000);
        finish();
        System.exit(0);

    }
}
