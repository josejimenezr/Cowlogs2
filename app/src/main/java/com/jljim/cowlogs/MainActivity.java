package com.jljim.cowlogs;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           MainActivity.java
 * File purpose:        MainActivity to add major functions in the application
 */

public class MainActivity extends AppCompatActivity{

    // String Array that hold all the cow breed in the order to present and to move on.
    public static String[] pageNames = {"Angus", "Hereford", "Brahman", "Shorthorn", "Brangus", "Home"};

    // Start the currentPage with zero
    public int currentPage = 0;

    // variable with the list of cow added of a breed
    public static ArrayList<CowLogs>  cowLogsArrayList = new ArrayList<>();

    // variables of logger user
    public static String loggerUsername;
    public static String password;

    // onCreate methods to start he application
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Start the application with the home fragment
        HomeFragment fragment = new HomeFragment(); //New object of the fragment
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        //replace the contain_main layout with the fragment
        fragmentTransaction.replace(R.id.cowPlace, fragment);
        fragmentTransaction.commit(); // commit the replacement

        // Start the toolbar or menu bar on top
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DBAdapter db = new DBAdapter(this);
        try {
            String destPath = "/data/data/" + getPackageName() + "/databases";
            File f = new File(destPath);
            if (!f.exists()) {   // create dir and then copy db
                f.mkdirs();
                f.createNewFile();
                //---copy the db from the assets folder into
                // the databases folder---
                CopyDB(getBaseContext().getAssets().open("CowLogs2.db"),
                        new FileOutputStream(destPath + "/CowLogs2.db"));
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        // open database
        db.open();

        // clear array before adding data
        cowLogsArrayList.clear();

        // create a cursor of all cows in the SB
        Cursor cursor = db.getAllCows();
        // do it if the database have more than one record
        if (cursor.moveToFirst())
        {
            do {
                // New CowLog object
                CowLogs cowObject = new CowLogs();

                // Set id by the column name and index
                cowObject.setId(cursor.getString(0));

                // Set cow by the column name and index
                cowObject.setBreed(cursor.getString(1));

                //Set the date value to the object
                cowObject.setDate(cursor.getString(2));

                // Set weight by the column name and index
                cowObject.setWeight(cursor.getString(3));

                // Set age by the column name and index
                cowObject.setAge(cursor.getString(4));

                // Set condition by the column name and index
                cowObject.setCondition(cursor.getString(5));

                /// Set longitude by the column name and index
                cowObject.setLongitude(cursor.getString(6));

                // Set latitude by the column name and index
                cowObject.setLatitude(cursor.getString(7));

                //Add cow to the array list
                MainActivity.cowLogsArrayList.add(cowObject);

            } while (cursor.moveToNext());
        }

        // Close the database
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_profile) {
            // create a fragment object to the profile
            ProfileFragment profileFragment = new ProfileFragment();
            // fragment transaction to change the fragment
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // Set the fragment on the screen and commit it.
            fragmentTransaction.replace(R.id.cowPlace, profileFragment).commit();

            return true;
        }

        // Method to save all data into the database
        if (id == R.id.action_save) {

            //create DBAdapter instance and open the DB
            DBAdapter db = new DBAdapter(this);
            db.open();

            // Check if there is not records to save
            if (cowLogsArrayList.size () == 0) {
                //Not records to save
                Toast.makeText(getApplication(), "No Record(s) to Save", Toast.LENGTH_SHORT).show();
            }

            // Array has records
            else {
                // Clean all the database to save it again
                if (db.removeAll()) {

                    // for loop to each element in the array list
                    for (CowLogs cow : cowLogsArrayList) {

                        //insert object in the db
                        db.insertCow(cow);
                    }

                    // Successful message to save
                    Toast.makeText(getApplication(), "Record(s) Saved in Database Successfully", Toast.LENGTH_SHORT).show();
                } else {
                    // Failed message to save data
                    Toast.makeText(getApplication(), "Fail Save Record(s) in Database", Toast.LENGTH_SHORT).show();
                }
            }

            // close the database
            db.close();
        }

        // Method to send all data into the database
        if (id == R.id.action_send) {

            // create an alert dialog object
            AlertDialog.Builder sendDialog = new AlertDialog.Builder(this);
            // add icon on the left dialog
            sendDialog.setIcon(R.mipmap.ic_launcher);
            // add title
            sendDialog.setTitle("Are you sure? This will delete all entries.");
            // add message in the dialog
            sendDialog.setMessage("Save entries to DB first?");
            // setting positive button
            sendDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        // Check if the database has records to send
                        if (cowLogsArrayList.size() == 0) {
                            // Not records to send
                            Toast.makeText(getApplication(), "No Record(s) to Email", Toast.LENGTH_SHORT).show();
                        }

                        else {
                            //email addresses
                            String emailAddress = "j.jimenezreid@cqumail.com";
                            String carbonCopies = "j.jimenezreid@cqumail.com";

                            // subject of the email
                            String subject = "New logger data";

                            // check if the Username variable is empty or null
                            if (loggerUsername == null) {
                                // assign an empty space into the username
                                loggerUsername = ""; // assign the variable as empty

                                Toast.makeText(getApplication(), "Username missing in the application", Toast.LENGTH_SHORT).show();

                            }

                            //message to send
                            String message = loggerUsername + "\n";

                            //for loop for the new data to send
                            for (CowLogs cow : cowLogsArrayList) {
                                //add the cows to the messages with the cow breed without longitude and latiti
                                message = message + cow.getBreed() + " " + cow.getCondition() + " "+cow.getDate() + " " + cow.getId() + " " + cow.getWeight() + " " + cow.getAge() + "\n";
                            }

                            String[] emailContent = new String[]{emailAddress, carbonCopies, subject, message};

                            try {
                                // execute the DoBackgroundTask inner class with the email contents
                                new DoBackgroundTask().execute(emailContent);

                            } catch (Exception e) {
                                e.printStackTrace(); // show the error sending the email
                            }
                        }
                    }
                });
            //setting negative button
            sendDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                }).create();

            // show the send dialog
            sendDialog.show();

        }

        return super.onOptionsItemSelected(item);
    }

    // DoBackgroundTask inner class to execute sending the email
    private class DoBackgroundTask extends AsyncTask<String, Void, Void> {
        //create DBAdapter instance and open the DB
        DBAdapter db = new DBAdapter(getApplicationContext());

        protected void onPreExecute(){
            // Open database
            db.open();

        } // pre execute to ope the database
        protected Void doInBackground(String... emailContents) {
            // call the method send email
            sendEmail(emailContents[0], emailContents[1], emailContents[2], emailContents[3]);
            return null;

        } // send the email
        protected void onPostExecute(Void param) {

            // clean the whole database
            if (db.removeAll()) {
                // Clear the array
                cowLogsArrayList.clear();
            }
            // close the database
            db.close();
        }
    }


    //---sends an Email to another person ---
    // Make by Code2care (n.d., http://code2care.org/pages/how-to-send-an-email-from-android-app-using-intent/) and modified by Jose Jimenez
    private void sendEmail(String emailAddresses, String carbonCopies, String subject, String message) {
        //Create an intent object of the email in order to send
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        //set the data to email
        emailIntent.setData(Uri.parse("mailto:"));

        //Array String with the email address to send
        String[] to = new String[] {emailAddresses};
        //Array String to carbon copy to other users
        String[] cc = new String[] {carbonCopies};

        // add the "to" variable to the email to forward
        emailIntent.putExtra(Intent.EXTRA_EMAIL, to);
        // add the "cc" variable to other email address to send the email
        emailIntent.putExtra(Intent.EXTRA_CC, cc);
        // add the subject variable in the email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        // add the message string in the email
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);
        emailIntent.setType("message/rfc822"); // type of send

        //start the email activity to email the message
        startActivity(Intent.createChooser(emailIntent, "Email"));
    }

    //override method to back press and save data before leave the app.
    @Override
    public void onBackPressed() {

        // create an alert dialog object
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        // add icon on the left
        saveDialog.setIcon(R.mipmap.ic_launcher);
        // add title
        saveDialog.setTitle("Database not saved");
        // add text
        saveDialog.setMessage("Save entries to DB first?");
        // setting the positive button
        saveDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {

                        //create DBAdapter instance and open the DB
                        DBAdapter db = new DBAdapter(getApplicationContext());
                        db.open();

                        // Check if there is not records to save
                        if (cowLogsArrayList.size () == 0) {
                            //Not records to save
                            Toast.makeText(getApplication(), "No Record(s) to Save", Toast.LENGTH_SHORT).show();
                        }

                        // Array has records
                        else {
                            // Clean all the database to save it again
                            if (db.removeAll()) {

                                // for loop to each element in the array list
                                for (CowLogs cow : cowLogsArrayList) {

                                    //insert object in the db
                                    db.insertCow(cow);
                                }

                                // Successful message to save
                                Toast.makeText(getApplication(), "Record(s) Saved in Database Successfully", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed message to save data
                                Toast.makeText(getApplication(), "Fail Save Record(s) in Database", Toast.LENGTH_SHORT).show();
                            }
                        }


                        // close the database
                        db.close();
                        // finish app
                        finish();
                    }
                });
        // setting negative button
        saveDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Data no saved
                        Toast.makeText(getApplication(), "New Record(s) not Saved", Toast.LENGTH_SHORT).show();
                        // finish app
                        finish();
                    }
                }).create();

        // show the save dialog
        saveDialog.show();

    }

    //Method to open the
    public void onClickBreed(View view) {
        //Retrieve index from view's tag and assigned to the current page to display
        currentPage = Integer.valueOf((String)view.getTag());

        // generic method to show the current page
        showCurrentPage();
    }

    // Generic method to show the current page
    public void showCurrentPage() {

        if(currentPage>5) currentPage =0;
        if(currentPage<0) currentPage = 5;

        if (currentPage == 5){
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.cowPlace, (Fragment) new HomeFragment()).commit();

        }
        else {

            //New object of the CowFragment
            CowFragment cowFragment = new CowFragment();
            //Communicate the cow breed to the fragment
            Bundle args = new Bundle();
            // set a variable cow wit the tag number integer
            args.putInt("cow", currentPage);
            // set the argument to display the right fragment
            cowFragment.setArguments(args);
            //Fragment transaction to begin the transaction
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            // Set the fragment on the screen and commit it.
            fragmentTransaction.replace(R.id.cowPlace, cowFragment).commit();
        }
    }

    // method to goBackHome
    public void goBackHome (View view) {
        // begin transaction manager
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment(); // new object of home page
        //Change the fragment to the home fragment
        fragmentTransaction.replace(R.id.cowPlace, homeFragment);
        fragmentTransaction.commit(); // commit the replacement of fragment
    }

    // method to move next breed
    public void nextBreed (View view){

        // move to the next page
        currentPage++;

        // display the new fragment
        showCurrentPage();
    }

    // method to move previous breed
    public void previousBreed (View view){

        // move to the next page
        currentPage--;

        // display the new fragment
        showCurrentPage();
    }

    //Method to show list of cows by breed
    public void showBreedList ( View view){

        //New object of the CowFragment
        CowList cowListFragment = new CowList();
        //Communicate the cow breed to the fragment
        Bundle args = new Bundle();
        // set a variable cow with the tag number integer
        args.putInt("cow", currentPage);
        // set the argument to display the right fragment
        cowListFragment.setArguments(args);

        //Fragment transaction to begin the transaction
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        // Set the fragment on the screen and commit it.
        fragmentTransaction.replace(R.id.cowPlace, cowListFragment).commit();

    }

    // method to return to current bred from the list
    public void addReturn(View view) {

        //show the current page again
        showCurrentPage();
    }

    // Copy method
    public void CopyDB(InputStream inputStream,
                       OutputStream outputStream) throws IOException {
        //---copy 1K bytes at a time---
        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }
        inputStream.close();
        outputStream.flush();
        outputStream.close();
    }
}
