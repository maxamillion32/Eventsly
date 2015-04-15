package com.example.william.eventsly;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;


public class Login extends ActionBarActivity
{
    SQLiteDatabase AccountsDB = null;

    Button SignIn;

    EditText Email, Password;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ActionBar actionBar = getActionBar();
        actionBar.hide();

        SignIn = (Button) findViewById(R.id.btnSignIn);
        Email = (EditText) findViewById(R.id.editTextEmail);
        Password = (EditText) findViewById(R.id.editTextPassword);

        try
        {
            AccountsDB = this.openOrCreateDatabase("Accounts", MODE_PRIVATE, null);

            AccountsDB.execSQL("CREATE TABLE IF NOT EXISTS accounts " +
                    "(id integer primary key, firstname VARCHAR, lastname VARCHAR, email VARCHAR, password VARCHAR);");

            File database = getApplicationContext().getDatabasePath("Accounts.db");

            if (!database.exists())
            {
                Toast.makeText(this, "Database Created or Exists", Toast.LENGTH_SHORT).show();
            }
            else
            {
                Toast.makeText(this, "Database doesn't exist", Toast.LENGTH_SHORT).show();
            }
        }
        catch(Exception e)
        {
            Log.e("ACCOUNTS ERROR", "Error Creating Database");
        }
    }

    public void onNewUsersClick(View view)
    {
        Intent getSignUpScreenIntent = new Intent(this, SignUp.class);

        startActivity(getSignUpScreenIntent);

        finish();
    }

    public void onCancelClick(View view)
    {
        Intent getTitleScreenIntent = new Intent(this, Title.class);

        startActivity(getTitleScreenIntent);

        AccountsDB.close();

        finish();
    }

    public void onSignInClick(View view)
    {

        String email = Email.getText().toString();
        String password = Password.getText().toString();

        if(isValidEmail(email))
        {
            if (GetAccountChecked(email, password) > 0)
            {
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();

                Intent getRoleScreenIntent = new Intent(this, Role.class);

                startActivity(getRoleScreenIntent);

                AccountsDB.close();

                finish();

            }
            else
            {
                Toast.makeText(this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(this, "Email is not valid.", Toast.LENGTH_SHORT).show();
        }



    }

    public int GetAccountChecked(String email, String password)
    {
        Cursor c = AccountsDB.rawQuery("SELECT email, password FROM accounts WHERE email =? AND password =?", new String[]{email, password});
        c.moveToFirst();
        return c.getCount();

    }

    public static boolean isValidEmail(CharSequence target)
    {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }
}
