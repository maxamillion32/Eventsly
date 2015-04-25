package com.example.william.eventsly;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class Login extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button SignIn;

    EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // referencing all items to their id in the layout
        SignIn = (Button) findViewById(R.id.btnSignIn);
        Email = (EditText) findViewById(R.id.editTextEmail);
        Password = (EditText) findViewById(R.id.editTextPassword);
        // opens or creates database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // opens or creates table accounts
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS accounts " + "(id integer primary key, firstname VARCHAR, lastname VARCHAR, email VARCHAR, password VARCHAR);");
        // opens or creates table currentuser
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS currentuser (id integer primary key, email VARCHAR);");
        // deletes all rows from currentuser table
        EventslyDB.execSQL("DELETE FROM currentuser");
    }
    // when clicked this goes to signup screen
    public void onNewUsersClick(View view)
    {
        Intent getSignUpScreenIntent = new Intent(this, SignUp.class);
        startActivity(getSignUpScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }
    // when clicked this checks to see if login credentials are valid, if so goes to role screen
    public void onSignInClick(View view)
    {
        // gets text from editTexts
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        // checking to see if email is valid
        if (isValidEmail(email))
        {
            // checks database to see if this is a valid account
            if (GetAccountChecked(email, password) > 0)
            {
                // inserts email if it is valid into currentuser table
                EventslyDB.execSQL("INSERT INTO currentuser (email) VALUES ('" + email + "');");

                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // goes to role screen now
                Intent getRoleScreenIntent = new Intent(this, Role.class);
                startActivity(getRoleScreenIntent);
                // closing database
                EventslyDB.close();

                finish();

            }
            else
            {
                // user doesn't exist in database
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            // email isn't valid
            Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
        }

    }
    // when back button on phone is pressed the app will close
    public void onBackPressed()
    {
        // closing database
        EventslyDB.close();

        finish();
        System.exit(0);
    }
    // passes in email and password from editText boxes and checks to see if user exists in database
    public int GetAccountChecked(String email, String password)
    {
        Cursor c = EventslyDB.rawQuery("SELECT email, password FROM accounts WHERE email =? AND password =?", new String[]{email, password});
        c.moveToFirst();
        return c.getCount();

    }
    // checks to see if the email has valid characters to be a possible email
    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
