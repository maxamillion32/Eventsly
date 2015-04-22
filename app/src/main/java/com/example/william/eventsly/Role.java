package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Role extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role);

    }

    public void onHostClick(View view)
    {
        Intent getHostScreenIntent = new Intent(this, Host.class);

        startActivity(getHostScreenIntent);

        finish();
    }

    public void onAttendeeClick(View view)
    {
        Intent getAttendeeScreenIntent = new Intent(this, Attendee.class);

        startActivity(getAttendeeScreenIntent);

        finish();
    }

    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Login.class);
        startActivity(getPreviousScreenIntent);

        finish();
    }

}
