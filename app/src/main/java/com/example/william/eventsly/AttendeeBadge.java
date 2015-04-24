package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class AttendeeBadge extends Activity
{
    TextView GuestAccess;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendee_badge);
        // referencing all items to their id in the layout
        GuestAccess = (TextView) findViewById(R.id.labelBadge);
        // file name
        String rFileName = "EventslyAccess.txt";
        // file directory
        File sdcard = Environment.getExternalStorageDirectory();
        // get the text file
        File receivedfile = new File(sdcard, rFileName);
        // read text from file
        StringBuilder text = new StringBuilder();
        // reads the text from the file and adds it to a string
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(receivedfile));
            String line;

            while ((line = br.readLine()) != null)
            {
                text.append(line);
            }
            br.close();
            String getBadge = String.valueOf(text);
            // sets text to our badge
            GuestAccess.setText(getBadge);
        }
        catch (IOException e)
        {
            // file directory
            File sdcard2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            // get the text file
            File receivedfile2 = new File(sdcard2, rFileName);

            // read text from file
            StringBuilder text2 = new StringBuilder();
            // reads the text from the file and adds it to a string
            try
            {
                BufferedReader br = new BufferedReader(new FileReader(receivedfile2));
                String line;

                while ((line = br.readLine()) != null)
                {
                    text2.append(line);
                }
                br.close();
                String getBadge2 = String.valueOf(text2);
                // sets text to our badge
                GuestAccess.setText(getBadge2);
            }
            catch (IOException e2)
            {
                Toast.makeText(this, "File can't be found.", Toast.LENGTH_LONG).show();
            }
            // delete file after use
            receivedfile2.delete();
        }
        // delete file after use
        receivedfile.delete();
    }
    // when clicked goes to attendee menu
    public void onGuestMenuClick(View view)
    {
        Intent getGuestMenuScreen = new Intent(this, Attendee.class);
        startActivity(getGuestMenuScreen);

        finish();
    }
    // when back button is pressed on phone goes to attendee menu
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Attendee.class);
        startActivity(getPreviousScreenIntent);

        finish();
    }

}
