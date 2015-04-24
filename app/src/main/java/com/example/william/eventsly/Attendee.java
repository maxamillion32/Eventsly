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
    // when clicked goes to attendee check in screen
    public void onCheckInClick(View view)
    {
        Intent getCheckInScreenIntent = new Intent(this, AttendeeCheckIn.class);
        startActivity(getCheckInScreenIntent);

        finish();
    }
    // still need to fix this button
    public void onCheckOutClick(View view)
    {
        Toast.makeText(this, "You have been Checked Out of your current event.", Toast.LENGTH_SHORT).show();
    }
    // when clicked goes to badge screen
    public void onBadgeClick(View view)
    {
        Intent getBadgeScreenIntent = new Intent(this, AttendeeBadge.class);
        startActivity(getBadgeScreenIntent);

        finish();
    }
    // when back button is pressed on phone goes to role screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Role.class);
        startActivity(getPreviousScreenIntent);

        finish();
    }

}
