package com.example.william.eventsly;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

public class PlannerEvents extends ActionBarActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planner_events);

    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem Item)
    {
        switch(Item.getItemId())
        {
            case R.id.action_CreateEvent:
                Intent getCreateEventScreenIntent = new Intent(this, CreateEvent.class);
                startActivity(getCreateEventScreenIntent);
                finish();
                break;

            case R.id.action_ViewEvent:
                Intent getViewEventScreenIntent = new Intent(this, ViewEvent.class);
                startActivity(getViewEventScreenIntent);
                finish();
                break;

            case R.id.action_EditEvent:
                Intent getEditEventScreenIntent = new Intent(this, EditEvent.class);
                startActivity(getEditEventScreenIntent);
                finish();
                break;

            case R.id.action_SwitchToAttendee:
                Intent getAttendeeEventsScreenIntent = new Intent(this, AttendeeEvents.class);
                startActivity(getAttendeeEventsScreenIntent);
                finish();
                break;

            case R.id.action_CheckInAttendee:
                Intent getCheckInAttendeeScreen = new Intent(this, CheckInAttendee.class);
                startActivity(getCheckInAttendeeScreen);
                finish();
                break;

            default:
                break;
        }

        return true;
    }

}
