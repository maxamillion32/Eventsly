package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;



public class HostCheckIn extends Activity
{
    TextView GuestCreds;

    SQLiteDatabase EventslyDB = null;

    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_check_in);
        Intent intent = getIntent();
        String ChosenEvent = intent.getExtras().getString("GuestEvent");

        GuestCreds = (TextView) findViewById(R.id.labelGuestCreds);


        String rFileAccess = "EventslyAccess.txt";

        File sdcardAccess = Environment.getExternalStorageDirectory();

        //Get the text file
        File receivedAccessFile = new File(sdcardAccess, rFileAccess);

        if (receivedAccessFile.exists()) {
            receivedAccessFile.delete();
        }

        try {
            EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);

            File database = getApplicationContext().getDatabasePath("eventslyDB.db");

            if (!database.exists()) {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Log.e("eventslyDB ERROR", "Error Creating Database");
        }

        PackageManager pm = this.getPackageManager();
        // Check whether NFC is available on device
        if (!pm.hasSystemFeature(PackageManager.FEATURE_NFC)) {
            // NFC is not available on the device.
            Toast.makeText(this, "The device does not has NFC hardware.", Toast.LENGTH_SHORT).show();
        }
        // Check whether device is running Android 4.1 or higher
        else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            // Android Beam feature is not supported.
            Toast.makeText(this, "Android Beam is not supported.", Toast.LENGTH_SHORT).show();
        } else {
            // / NFC and Android Beam file transfer is supported.
            Toast.makeText(this, "Android Beam is supported on your device.", Toast.LENGTH_SHORT).show();
        }

        String rFileName = "EmailAddress.txt";

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
            String getEmail = String.valueOf(text);
            GuestCreds.setText(getEmail);

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
                String getEmail2 = String.valueOf(text2);
                GuestCreds.setText(getEmail2);

            }
            catch (IOException e2)
            {
                Toast.makeText(this, "File can't be found.", Toast.LENGTH_LONG).show();
            }
            receivedfile2.delete();
        }
        receivedfile.delete();

        String checkemail = GuestCreds.getText().toString();
        Toast.makeText(this, checkemail, Toast.LENGTH_LONG).show();

        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // Check whether NFC is enabled on device
        if (!nfcAdapter.isEnabled())
        {
            // NFC is disabled, show the settings UI
            // to enable NFC
            Toast.makeText(this, "Please enable NFC.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
        }
        // Check whether Android Beam feature is enabled on device
        else if (!nfcAdapter.isNdefPushEnabled())
        {
            // Android Beam is disabled, show the settings UI
            // to enable Android Beam
            Toast.makeText(this, "Please enable Android Beam.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Settings.ACTION_NFCSHARING_SETTINGS));
        }
        else
        {

            if (getFileCount(checkemail, ChosenEvent) == 0)
            {
                Toast.makeText(this, "Access Denied.", Toast.LENGTH_LONG).show();

                String content = "Access Denied.";
                File file;
                FileOutputStream outputStream;
                try
                {

                    file = new File(Environment.getExternalStorageDirectory(), "EventslyAccess.txt");

                    outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // NFC and Android Beam both are enabled

                // File to be transferred
                // For the sake of this tutorial I've placed an image
                // named 'wallpaper.png' in the 'Pictures' directory
                String fileName = "EventslyAccess.txt";

                // Retrieve the path to the user's public pictures directory
                File fileDirectory = Environment.getExternalStorageDirectory();

                // Create a new file using the specified directory and name
                File fileToTransfer = new File(fileDirectory, fileName);
                fileToTransfer.setReadable(true, true);
                fileToTransfer.setWritable(true, true);
                EventslyDB.close();

                nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);
            }
            else
            {
                String content = "Name: " + (getAccessFirstName(checkemail, ChosenEvent) + " " + getAccessLastName(checkemail, ChosenEvent) +
                         "          Access Level: " + getAccessLevel(checkemail, ChosenEvent));
                File file;
                FileOutputStream outputStream;
                try
                {

                    file = new File(Environment.getExternalStorageDirectory(), "EventslyAccess.txt");

                    outputStream = new FileOutputStream(file);
                    outputStream.write(content.getBytes());
                    outputStream.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
                // NFC and Android Beam both are enabled

                // File to be transferred
                // For the sake of this tutorial I've placed an image
                // named 'wallpaper.png' in the 'Pictures' directory
                String fileName = "EventslyAccess.txt";

                // Retrieve the path to the user's public pictures directory
                File fileDirectory = Environment.getExternalStorageDirectory();

                // Create a new file using the specified directory and name
                File fileToTransfer = new File(fileDirectory, fileName);
                fileToTransfer.setReadable(true, true);
                fileToTransfer.setWritable(true, true);
                EventslyDB.close();

                nfcAdapter.setBeamPushUris(new Uri[]{Uri.fromFile(fileToTransfer)}, this);

            }
        }
    }

    public String getAccessFirstName(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("firstname"));
    }

    public String getAccessLastName(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("lastname"));
    }

    public String getAccessLevel(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getString(c.getColumnIndex("accesslevel"));
    }

    public int getFileCount(String email, String checkevent)
    {
        Cursor c = EventslyDB.rawQuery("SELECT * FROM eventguestlist WHERE email =? and eventname =?", new String[]{email, checkevent});
        c.moveToFirst();
        return c.getCount();
    }

    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, HostSelectEventCheckIn.class);
        startActivity(getPreviousScreenIntent);

        EventslyDB.close();

        finish();
    }

}


