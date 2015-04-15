package com.example.william.eventsly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;


public class AttendeeEvents extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_events);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_attendee, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem Item)
    {
        switch(Item.getItemId())
        {
            case R.id.action_SearchEvents:
                Intent getSearchEventsScreenIntent = new Intent(this, SearchEvents.class);
                startActivity(getSearchEventsScreenIntent);
                finish();
                break;

            case R.id.action_UnregisterFromAnEvent:
                Intent getUnregisterFromEventScreenIntent = new Intent(this, UnregisterFromEvent.class);
                startActivity(getUnregisterFromEventScreenIntent);
                finish();
                break;

            case R.id.action_SignIntoAnEvent:
                Intent getSignIntoEventScreenIntent = new Intent(this, CheckIntoEvent.class);
                startActivity(getSignIntoEventScreenIntent);
                finish();
                break;

            case R.id.action_ProfileDetails:
                Intent getProfileDetailsScreenIntent = new Intent(this, AttendeeEvents.class);
                startActivity(getProfileDetailsScreenIntent);
                finish();
                break;
            default:
                break;
        }

        return true;
    }

}
