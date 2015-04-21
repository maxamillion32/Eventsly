package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
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

        GuestAccess = (TextView) findViewById(R.id.labelBadge);

        String rFileName = "EventslyAccess.txt";

        File sdcard = Environment.getExternalStorageDirectory();

        //Get the text file
        File receivedfile = new File(sdcard, rFileName);

        //Read text from file
        StringBuilder text = new StringBuilder();

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
            GuestAccess.setText(getBadge);
        }
        catch (IOException e)
        {
            File sdcard2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);

            //Get the text file
            File receivedfile2 = new File(sdcard2, rFileName);

            //Read text from file
            StringBuilder text2 = new StringBuilder();

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
                GuestAccess.setText(getBadge2);

            }
            catch (IOException e2)
            {
                Toast.makeText(this, "File can't be found.", Toast.LENGTH_LONG).show();
            }
            receivedfile2.delete();
        }
        receivedfile.delete();


    }

    public void onGuestMenuClick(View view)
    {
        Intent getGuestMenuScreen = new Intent(this, Attendee.class);
        startActivity(getGuestMenuScreen);

        finish();
    }
}
