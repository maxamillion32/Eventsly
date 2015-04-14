package com.example.william.eventsly;
import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Title extends Activity
{

    Button Login, SignUp, Exit;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title);


    }

    public void onLoginClick(View view)
    {
        Intent getLoginScreenIntent = new Intent(this, Login.class);

        startActivity(getLoginScreenIntent);

        finish();
        System.exit(0);
    }

    public void onSignUpClick(View view)
    {
        Intent getSignUpScreenIntent = new Intent(this, SignUp.class);

        startActivity(getSignUpScreenIntent);

        finish();
        System.exit(0);
    }

    public void onExitClick(View view)
    {
        finish();
        System.exit(0);
    }
}


