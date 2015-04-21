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


    public void onCreateClick(View view)
    {
        Intent getCreateScreenIntent = new Intent(this, HostCreateEvent.class);
        startActivity(getCreateScreenIntent);

        finish();
    }

    public void onViewClick(View view)
    {
        Intent getViewScreenIntent = new Intent(this, HostViewEvent.class);
        startActivity(getViewScreenIntent);

        finish();
    }

    public void onCheckInClick(View view)
    {
        Intent getSelectEventCheckInScreenIntent = new Intent(this, HostSelectEventCheckIn.class);
        startActivity(getSelectEventCheckInScreenIntent);

        finish();
    }

    public void onRegisterGuestsClick(View view)
    {
        Intent getRegisterGuestsScreenIntent = new Intent(this, HostRegisterList.class);
        startActivity(getRegisterGuestsScreenIntent);

        finish();
    }

}
