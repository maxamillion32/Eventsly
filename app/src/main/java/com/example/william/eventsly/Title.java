package com.example.william.eventsly;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Title extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        View decorView = getWindow().getDecorView();
        // hiding status bar
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
        // hiding actionbar
        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    // when clicked goes to login screen
    public void onLoginClick(View view)
    {
        Intent getLoginScreenIntent = new Intent(this, Login.class);
        startActivity(getLoginScreenIntent);

        finish();
    }

    // when clicked goes to signup screen
    public void onSignUpClick(View view)
    {
        Intent getSignUpScreenIntent = new Intent(this, SignUp.class);
        startActivity(getSignUpScreenIntent);

        finish();
    }

    // when clicked closes app
    public void onExitClick(View view)
    {
        finish();
        System.exit(0);
    }
}


