package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;


public class Attendee extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee);

    }

    public void onCheckInClick(View view)
    {
        Intent getCheckInScreenIntent = new Intent(this, AttendeeCheckIn.class);
        startActivity(getCheckInScreenIntent);

        finish();
    }

    public void onCheckOutClick(View view)
    {

        Toast.makeText(this, "You have been Checked Out of your current event.", Toast.LENGTH_SHORT).show();
    }

    public void onBadgeClick(View view)
    {
        Intent getBadgeScreenIntent = new Intent(this, AttendeeBadge.class);
        startActivity(getBadgeScreenIntent);

        finish();
    }
}
