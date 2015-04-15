package com.example.william.eventsly;
import android.app.ActionBar;
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

        ActionBar actionBar = getActionBar();
        actionBar.hide();

    }

    public void onPlannerClick(View view)
    {
        Intent getPlannerScreenIntent = new Intent(this, PlannerEvents.class);

        startActivity(getPlannerScreenIntent);

        finish();
    }

    public void onAttendeeClick(View view)
    {
        Intent getAttendeeScreenIntent = new Intent(this, AttendeeEvents.class);

        startActivity(getAttendeeScreenIntent);

        finish();
    }
}
