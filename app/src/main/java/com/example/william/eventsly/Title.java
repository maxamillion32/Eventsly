package com.example.william.eventsly;
import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;

public class Title extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);

        ActionBar actionBar = getActionBar();
        actionBar.hide();
    }

    public void onLoginClick(View view)
    {
        Intent getLoginScreenIntent = new Intent(this, Login.class);

        startActivity(getLoginScreenIntent);

        finish();
    }

    public void onSignUpClick(View view)
    {
        Intent getSignUpScreenIntent = new Intent(this, SignUp.class);

        startActivity(getSignUpScreenIntent);

        finish();
    }

    public void onExitClick(View view)
    {

        finish();
        System.exit(0);
    }
}


