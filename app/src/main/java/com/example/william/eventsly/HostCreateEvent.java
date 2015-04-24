package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class HostCreateEvent extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button CreateEvent;

    EditText EventName, DateAndTime, Address, Description;

    Spinner EventType;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_create_event);
        // referencing all items to their id in the layout
        CreateEvent = (Button) findViewById(R.id.btnCreateEvent);
        EventName = (EditText) findViewById(R.id.editTextEventName);
        DateAndTime = (EditText) findViewById(R.id.editTextDateAndTime);
        Address = (EditText) findViewById(R.id.editTextAddress);
        Description = (EditText) findViewById(R.id.editTextDescription);
        EventType = (Spinner) findViewById(R.id.spinnerEventType);
        // creates an adapter for our EventType Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EventType.setAdapter(adapter);
        // opening database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // creates events table if it doesn't exist
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS events " + "(id integer primary key, eventname VARCHAR, dateandtime VARCHAR, address VARCHAR, eventtype VARCHAR, description VARCHAR);");
    }

    // when clicked this will create an event
    public void onCreateEventClick(View view)
    {
        // grabs all the texts from the fields
        String eventname = EventName.getText().toString();
        String dateandtime = DateAndTime.getText().toString();
        String address = Address.getText().toString();
        String eventtype = EventType.getSelectedItem().toString();
        String description = Description.getText().toString();
        // checks to see if the EventName field is empty
        if (eventname.equals(""))
        {
            Toast.makeText(this, "Please enter a name for your event.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // checks to see if the DateAndTime field is empty
            if (dateandtime.equals(""))
            {
                Toast.makeText(this, "Please enter a date and time for your event.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // checks to see if the Address field is empty
                if (address.equals(""))
                {
                    Toast.makeText(this, "Please enter an address for your event.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // checks to see if an EventType has been selected
                    if (eventtype.equals("Select Event Type"))
                    {
                        Toast.makeText(this, "Please select an event type.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // checks to see if the Description field is empty
                        if (description.equals(""))
                        {
                            Toast.makeText(this, "Please enter a description for your event.", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // inserts all the fields into the database
                            EventslyDB.execSQL("INSERT INTO events (eventname, dateandtime, address, eventtype, description) VALUES ('" +
                                    eventname + "', '" + dateandtime + "', '" + address + "', '" + eventtype + "', '" + description + "');");

                            Toast.makeText(this, "Event Creation Successful", Toast.LENGTH_SHORT).show();
                            // closing database
                            EventslyDB.close();
                            // goes to host menu screen
                            Intent getHostScreenIntent = new Intent(this, Host.class);
                            startActivity(getHostScreenIntent);

                            finish();

                        }
                    }
                }
            }
        }
    }

    // when clicked goes to host menu screen
    public void onCancelCreateEventClick(View view)
    {
        Intent getHostScreenIntent = new Intent(this, Host.class);
        startActivity(getHostScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

    // when back button is pressed on phone goes to host menu screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Host.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

}
