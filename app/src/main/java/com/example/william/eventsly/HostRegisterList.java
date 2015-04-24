package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class HostRegisterList extends Activity
{

    SQLiteDatabase EventslyDB = null;

    EditText FirstName, LastName, Email, Access;

    Spinner RegisterGuestEvent;

    ArrayList<String> EventRegisterList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_register_list);
        // referencing all items to their id in the layout
        FirstName = (EditText) findViewById(R.id.editTextRegisterFirstName);
        LastName = (EditText) findViewById(R.id.editTextRegisterLastName);
        Email = (EditText) findViewById(R.id.editTextRegisterEmail);
        Access = (EditText) findViewById(R.id.editTextRegisterAccess);
        RegisterGuestEvent = (Spinner) findViewById(R.id.spinnerRegisterGuestEvent);
        // setting adapter for RegisterGuestEvent Spinner
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, EventRegisterList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RegisterGuestEvent.setAdapter(spinnerAdapter);
        // opening database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // creates eventguestlist table if it doesn't exist
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS eventguestlist " + "(id integer primary key, eventname VARCHAR, firstname VARCHAR, lastname VARCHAR, email VARCHAR, accesslevel VARCHAR);");

        int id = 1;
        String stringID = Integer.toString(id);
        // a loop to add names to our RegisterGuestEvent Spinner
        while (getEvents(stringID) != 0)
        {
            EventRegisterList.add(getEventName(stringID));
            spinnerAdapter.notifyDataSetChanged();

            id++;
            stringID = Integer.toString(id);
        }
    }

    // when clicked this will register a guest for an event
    public void onRegisterClick(View view)
    {
        // grabs all the texts in each field
        String eventname = RegisterGuestEvent.getSelectedItem().toString();
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String email = Email.getText().toString();
        String accesslevel = Access.getText().toString();
        // checks to see if the FirstName field is empty
        if (firstname.equals(""))
        {
            Toast.makeText(this, "Please enter your first name.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            // checks to see if the LastName field is empty
            if (lastname.equals(""))
            {
                Toast.makeText(this, "Please enter your last name.", Toast.LENGTH_SHORT).show();
            }
            else
            {
                // checks to see if the Email field is empty
                if (email.equals(""))
                {
                    Toast.makeText(this, "Please enter your email.", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    // checks to see if the Access field is empty
                    if (accesslevel.equals(""))
                    {
                        Toast.makeText(this, "Please enter what level of access you have.", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // checks to see if the email contains valid characters for a possible email
                        if (isValidEmail(email))
                        {
                            // checks to see if the user has already signed up for this event
                            if (GetEmailChecked(email, eventname) == 0)
                            {
                                // adds all the fields into the database
                                EventslyDB.execSQL("INSERT INTO eventguestlist (eventname, firstname, lastname, email, accesslevel) VALUES ('" +
                                        eventname + "', '" + firstname + "', '" + lastname + "', '" + email + "', '" + accesslevel + "');");

                                Toast.makeText(this, "Guest Registered", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(this, "A guest with this email already exists.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        }
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

    // when the back button is pressed on the phone goes to host menu screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Host.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

    // returns a count of total rows that have the same ID that is passed in
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

    // returns a count to see if the email and eventname that is passed in has more than one row
    public int GetEmailChecked(String email, String eventname)
    {
        Cursor c = EventslyDB.rawQuery("SELECT eventname , COUNT(*) FROM (SELECT email, eventname FROM eventguestlist WHERE email =? AND eventname =?)" + " GROUP BY eventname HAVING COUNT(*) >= 1;", new String[]{email, eventname});
        c.moveToFirst();
        return c.getCount();

    }

    // checks to see if the email has valid characters to be a possible email
    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


}
