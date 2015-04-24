package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class HostViewEvent extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button EditEvent, SaveEditEvent;

    EditText DateAndTime, Address, Description;

    Spinner ChooseEvent, EventType;

    ListView EventGuestList;

    ArrayList<String> EventList = new ArrayList<>();
    ArrayList<String> GuestList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_view_event);
        // referencing all items to their id in the layout
        DateAndTime = (EditText) findViewById(R.id.editTextViewDateAndTime);
        Address = (EditText) findViewById(R.id.editTextViewAddress);
        Description = (EditText) findViewById(R.id.editTextViewDescription);
        EventType = (Spinner) findViewById(R.id.spinnerEventType);
        EditEvent = (Button) findViewById(R.id.btnEditEvent);
        SaveEditEvent = (Button) findViewById(R.id.btnSaveEditEvent);
        ChooseEvent = (Spinner) findViewById(R.id.spinnerChooseEvent);
        EventGuestList = (ListView) findViewById(R.id.listViewRegisteredGuests);
        // making all the fields not editable or clickable
        DateAndTime.setEnabled(false);
        Address.setEnabled(false);
        Description.setEnabled(false);
        EventType.setEnabled(false);
        // hides save button
        SaveEditEvent.setVisibility(View.GONE);
        // opening database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // makes sure events table exists if not it will create it
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS events " + "(id integer primary key, eventname VARCHAR, dateandtime VARCHAR, address VARCHAR, eventtype VARCHAR, description VARCHAR);");
        // used to fill the EventType spinner with a list
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.EventTypes_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        EventType.setAdapter(adapter);
        // used to fill the ChooseEvent spinner with a list
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ChooseEvent.setAdapter(spinnerAdapter);
        // creating adapter for GuestList ListView
        final ArrayAdapter<String> listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, GuestList);

        int id = 1;
        String stringID = Integer.toString(id);
        // loop that fills ChooseEvent Spinner with current event names that have been created
        while (getEvents(stringID) != 0)
        {
            EventList.add(getEventName(stringID));
            spinnerAdapter.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }
        // when an event is selected in ChooseEvent Spinner
        ChooseEvent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                // queries that set what is returned to these fields based on what event is chosen by the ChooseEvent Spinner
                String eventname = ChooseEvent.getSelectedItem().toString();
                DateAndTime.setText(getDateAndTime(eventname));
                Address.setText(getAddress(eventname));
                EventType.setSelection(getSpinnerIndex(EventType, getEventType(eventname)));
                Description.setText(getDescription(eventname));

                int rowid = 1;
                String stringRowID = Integer.toString(rowid);
                // loop that fills the GuestList ListView
                // this will show all the guests that are signed up for the event chosen in the ChooseEvent Spinner
                while (getNameListCount(eventname, stringRowID) != 0)
                {
                    GuestList.add(getRegisteredGuestsFirstName(eventname, stringRowID) + " " + getRegisteredGuestsLastName(eventname, stringRowID));

                    rowid++;
                    stringRowID = Integer.toString(rowid);
                }
                // sets the GuestList after it is filled
                EventGuestList.setAdapter(listAdapter);
            }

            // if nothing is selected by ChooseEvent Spinner
            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
    }

    // when clicked fields become editable
    public void onEditEventClick(View view)
    {
        // all fields are set editable and clickable
        DateAndTime.setEnabled(true);
        Address.setEnabled(true);
        Description.setEnabled(true);
        EventType.setEnabled(true);
        // hides edit button
        EditEvent.setVisibility(View.GONE);
        // shows save button
        SaveEditEvent.setVisibility(View.VISIBLE);
    }

    // when clicked saves any edits made to an event
    public void onSaveEditEventClick(View view)
    {
        // grabs all the text from each item
        String eventname = ChooseEvent.getSelectedItem().toString();
        String dateandtime = DateAndTime.getText().toString();
        String address = Address.getText().toString();
        String eventtype = EventType.getSelectedItem().toString();
        String description = Description.getText().toString();
        // checks to see if DateAndTime field is empty
        if (dateandtime.equals(""))
        {
            Toast.makeText(this, "Please enter a date and time for your event.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // checks to see if Address field is empty
            if (address.equals(""))
            {
                Toast.makeText(this, "Please enter an address for your event.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // checks to see if EventType has a valid event selected
                if (eventtype.equals("Select Event Type"))
                {
                    Toast.makeText(this, "Please select an event type.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // checks to see if Description is empty
                    if (description.equals(""))
                    {
                        Toast.makeText(this, "Please enter a description for your event.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // updates the database based on what is entered in the fields above
                        EventslyDB.execSQL("UPDATE events SET dateandtime = '" + dateandtime +
                                "' WHERE eventname = '" + eventname + "';");

                        EventslyDB.execSQL("UPDATE events SET address = '" + address +
                                "' WHERE eventname = '" + eventname + "';");

                        EventslyDB.execSQL("UPDATE events SET eventtype = '" + eventtype +
                                "' WHERE eventname = '" + eventname + "';");

                        EventslyDB.execSQL("UPDATE events SET description = '" + description +
                                "' WHERE eventname = '" + eventname + "';");

                        Toast.makeText(this, "Event Edit Successful", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }
        // making all fields not editable and clickable
        DateAndTime.setEnabled(false);
        Address.setEnabled(false);
        Description.setEnabled(false);
        EventType.setEnabled(false);
        // show edit button
        EditEvent.setVisibility(View.VISIBLE);
        // hide save button
        SaveEditEvent.setVisibility(View.GONE);
    }

    // when clicked goes to host menu screen
    public void onMainMenuClick(View view)
    {
        Intent getHostMenuScreenIntent = new Intent(this, Host.class);
        startActivity(getHostMenuScreenIntent);
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

    // returns a count of the total events on the same row as the ID that is passed in
    public int getEvents(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getCount();
    }

    // returns the eventname that is on the same row as the ID that is passed in
    public String getEventName(String stringID)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname FROM events WHERE id =?", new String[]{stringID});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventname"));
    }

    // returns the dateandtime that is on the same row as the eventname that is passed in
    public String getDateAndTime(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT dateandtime FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("dateandtime"));
    }

    // returns the address that is on the same row as the eventname that is passed in
    public String getAddress(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT address FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("address"));
    }

    // returns the eventtype that is on the same row as the eventname that is passed in
    public String getEventType(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventtype FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("eventtype"));
    }

    // returns the description that is on the same row as the eventname that is passed in
    public String getDescription(String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT description FROM events WHERE eventname =?", new String[]{eventname});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("description"));
    }

    // returns the firstname that is on the same row as the eventname and ID that is passed in
    public String getRegisteredGuestsFirstName(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT firstname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("firstname"));
    }

    // returns the lastname that is on the same row as the eventname and ID that is passed in
    public String getRegisteredGuestsLastName(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT lastname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("lastname"));
    }

    // returns a count of guests that is on the same row as the eventname and ID that is passed in
    public int getNameListCount(String eventname, String rowid)
    {
        Cursor c = EventslyDB.rawQuery("SELECT firstname FROM eventguestlist WHERE eventname=? and rowid=?", new String[]{eventname, rowid});
        c.moveToFirst();
        return c.getCount();
    }

    // loops through events to give them an index or ID
    public int getSpinnerIndex(Spinner chooseEvent, String spinnerEvent)
    {
        int index = 0;

        for (int i = 0; i < chooseEvent.getCount(); i++)
        {
            if (chooseEvent.getItemAtPosition(i).equals(spinnerEvent))
            {
                index = i;
            }
        }
        return index;
    }
}




