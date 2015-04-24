package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;


public class Host extends Activity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host);

    }
    // when clicked goes to host create event screen
    public void onCreateClick(View view)
    {
        Intent getCreateScreenIntent = new Intent(this, HostCreateEvent.class);
        startActivity(getCreateScreenIntent);

        finish();
    }
    // when clicked goes to host view screen
    public void onViewClick(View view)
    {
        Intent getViewScreenIntent = new Intent(this, HostViewEvent.class);
        startActivity(getViewScreenIntent);

        finish();
    }
    // when clicked goes to host event selection for check-in screen
    public void onCheckInClick(View view)
    {
        Intent getSelectEventCheckInScreenIntent = new Intent(this, HostSelectEventCheckIn.class);
        startActivity(getSelectEventCheckInScreenIntent);

        finish();
    }
    // when clicked goes to register guests screen
    public void onRegisterGuestsClick(View view)
    {
        Intent getRegisterGuestsScreenIntent = new Intent(this, HostRegisterList.class);
        startActivity(getRegisterGuestsScreenIntent);

        finish();
    }
    // when back button on phone is pressed goes to role screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Role.class);
        startActivity(getPreviousScreenIntent);

        finish();
    }


}
