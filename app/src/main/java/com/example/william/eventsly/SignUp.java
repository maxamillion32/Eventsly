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

public class SignUp extends Activity
{
    SQLiteDatabase EventslyDB = null;

    Button CreateAccount;

    EditText FirstName, LastName, Email, Password, ConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        // referencing each item to their id in the layout
        CreateAccount = (Button) findViewById(R.id.btnCreateAccount);
        FirstName = (EditText) findViewById(R.id.editTextFirstName);
        LastName = (EditText) findViewById(R.id.editTextLastName);
        Email = (EditText) findViewById(R.id.editTextEmailSignUp);
        Password = (EditText) findViewById(R.id.editTextPasswordSignUp);
        ConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPasswordSignUp);
        // creates or opens database
        EventslyDB = this.openOrCreateDatabase("eventslyDB", MODE_PRIVATE, null);
        // opens or creates accounts table
        EventslyDB.execSQL("CREATE TABLE IF NOT EXISTS accounts " + "(id integer primary key, firstname VARCHAR, lastname VARCHAR, email VARCHAR, password VARCHAR);");

    }

    // when clicked goes to title screen
    public void onCancelSignUpClick(View view)
    {
        Intent getTitleScreenIntent = new Intent(this, Title.class);
        startActivity(getTitleScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

    // when clicked this creates the user an account and goes to login screen
    public void onCreateAccountClick(View view)
    {
        //grabbing all the texts entered into the editText boxes when button is clicked
        String firstname = FirstName.getText().toString();
        String lastname = LastName.getText().toString();
        String email = Email.getText().toString();
        String password = Password.getText().toString();
        String confirmpassword = ConfirmPassword.getText().toString();
        // checks to see if password matches confirmpassword
        if (password.equals(confirmpassword))
        {
            // checks to see if email has valid characters
            if (isValidEmail(email))
            {
                // checks to see if the user already has an account
                if (GetEmailChecked(email) == 0)
                {
                    // inserts users information from editText boxes into the database
                    EventslyDB.execSQL("INSERT INTO accounts (firstname, lastname, email, password) VALUES ('" +
                            firstname + "', '" + lastname + "', '" + email + "', '" + password + "');");

                    Toast.makeText(this, "Account Creation Successful", Toast.LENGTH_SHORT).show();
                    // closing database
                    EventslyDB.close();
                    // goes to login screen
                    Intent getLoginScreenIntent = new Intent(this, Login.class);
                    startActivity(getLoginScreenIntent);

                    finish();
                }
                else
                {
                    Toast.makeText(this, "An account with this email already exists.", Toast.LENGTH_SHORT).show();
                }
            }
            else
            {
                Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            Toast.makeText(this, "Passwords do not match.", Toast.LENGTH_SHORT).show();
        }
    }

    // passes in email to see if user is already registered
    public int GetEmailChecked(String email)
    {
        Cursor c = EventslyDB.rawQuery("SELECT email FROM accounts WHERE email =?", new String[]{email});
        c.moveToFirst();
        return c.getCount();

    }

    // checks email to see if it has valid characters to be a possible email
    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    // when back button is pressed on phone goes to login screen
    public void onBackPressed()
    {
        Intent getPreviousScreenIntent = new Intent(this, Login.class);
        startActivity(getPreviousScreenIntent);
        // closing database
        EventslyDB.close();

        finish();
    }

}
