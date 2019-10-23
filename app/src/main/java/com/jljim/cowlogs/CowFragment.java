package com.jljim.cowlogs;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           CowFragement.java
 * File purpose:        Fragment to add new cow breed of 5 different types.
 * A simple {@link Fragment} subclass.
 */
public class CowFragment extends Fragment{

    // String Array with conditions
    String[] conditions;

    // Create variable to read on the fragment
    Button save, show;
    TextView cowTitle, cowId, cowAge, cowWeight;
    Spinner cowSpinner;

    // Initialised the TrackGPS variable
    TrackGPS tracker;

    //start cow variable static to used in the CowList fragment
    public static int cow;

    // Create variable of the TextView in the fragment

    public CowFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cow, container, false);

        //get the integers of the tag of each button of cow breed
        cow = getArguments().getInt("cow");

        //Modify label by the id
        cowTitle = (TextView) view.findViewById(R.id.cowTitle);
        // Set text according to the cow list integers
        cowTitle.setText(MainActivity.pageNames[cow]);

        //initialised the tracker variable
        tracker = new TrackGPS(getContext());

        // return the view
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Set values to the value
        save = (Button) getActivity().findViewById(R.id.saveLogEntry);
        show = (Button) getActivity().findViewById(R.id.showLogEntries);

        // Set textView
        cowId = (TextView) getActivity().findViewById(R.id.idField);
        cowAge = (TextView) getActivity().findViewById(R.id.ageField);
        cowWeight = (TextView) getActivity().findViewById(R.id.weightField);
        cowSpinner = (Spinner) getActivity().findViewById(R.id.spinner);

        // On click method to save a cow in the MainActivity array
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create id variable
                String cow_Id = null;
                Integer cow_Id_int = 0;

                // variable to evaluate that everything is alright
                boolean verification = true;

                // Validation of the ID entry
                try {
                    //get text from the id field
                    cow_Id = cowId.getText().toString();
                    // convert to number to check if only contains numbers
                    cow_Id_int = Integer.parseInt(cow_Id);

                }
                catch (Exception e){
                    //Display success message to the customer
                    Toast.makeText(getContext(),"ID must be a number between 1111 to 9999",Toast.LENGTH_SHORT).show();
                    // false number
                    verification = false;
                }

                // Check that the id is four digits
                if (cow_Id.length() != 4 || cow_Id_int < 1111 || cow_Id_int > 9999) {
                    //Display success message to the customer
                    Toast.makeText(getContext(),"ID must be a number between 1111 to 999", Toast.LENGTH_SHORT).show();
                    //no 4 digits
                    verification = false;
                }

                //Check that the cow id is unique
                if (MainActivity.cowLogsArrayList.size()>0) {
                    for (CowLogs cow: MainActivity.cowLogsArrayList){
                        // To ids in the file
                        if (cow.getId().equals(cow_Id)){
                            //Display success message to the customer
                            Toast.makeText(getContext(),"ID must be unique", Toast.LENGTH_SHORT).show();
                            //Id is used
                            verification = false;
                        }
                    }
                }

                // Create weight variable
                String cow_Weight = null;
                double cow_Weight_Double = 0; //variable to check the weight double

                // Validation of the Weight entry
                try {
                    //get text from the id field
                    cow_Weight = cowWeight.getText().toString();
                    // convert to number to check if only contains numbers
                    cow_Weight_Double = Double.parseDouble(cow_Weight);

                }
                catch (Exception e){
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Weight must be a number between 1-5000", Toast.LENGTH_SHORT).show();
                    // It is not a number
                    verification = false;
                }

                // Check that the weight is a number between 1 and 5000
                if ((cow_Weight_Double<1)||(cow_Weight_Double>5000)) {
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Weight must be a number between 1-5000", Toast.LENGTH_SHORT).show();
                    // It is not between 1 - 5000
                    verification = false;
                }

                // Create age variable
                String cow_Age = null;
                int cow_Age_Int = 0;

                // Validation of the Age entry
                try {
                    //get text from the id field
                    cow_Age = cowAge.getText().toString();
                    // convert to number to check if only contains numbers
                    cow_Age_Int = Integer.parseInt(cow_Age);

                }
                catch (Exception e){
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Age must be a number between 1-120", Toast.LENGTH_SHORT).show();
                    // It is not a number
                    verification = false;
                }

                // Check that the weight is a number between 1 and 120
                if ((cow_Age_Int<1)||(cow_Age_Int>120)) {

                    //Display success message to the customer
                    Toast.makeText(getContext(),"Age must be a number between 1-120", Toast.LENGTH_SHORT).show();
                    // it is not between 1-120
                    verification = false;

                }

                //Get value of the cow condition
                String cow_condition = cowSpinner.getSelectedItem().toString();

                //Check if the condition is not empty
                if (cow_condition.equals("")||cow_condition == null){
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Condition cannot be null", Toast.LENGTH_SHORT).show();
                    // No conditions selected
                    verification = false;

                }

                // Create a time variable
                Calendar date = Calendar.getInstance();

                //Check if the getTime does not have any problem
                try {
                    // Get the current time of the saving of information
                    date.getTime();
                }
                catch (Exception e) {
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Error reading time", Toast.LENGTH_SHORT).show();
                    // Impossible read the data
                    verification = false;

                }

                // Convert date to string
                String dateString = String.format("%1$td/%1$tm/%1$tY %1$tH:%1$tM", date);

                //initialised the tracker variable
                tracker = new TrackGPS(getContext());

                // get the location of the device with the save button is trigger
                boolean verifyTracker = tracker.canGetLocation();

                // get the variables of longitude and latitude
                Double longitude = 0.0;
                Double latitude = 0.0;
                String longitudeString="";
                String latitudeString="";

                // check if it is possible get the location
                if (verifyTracker) {
                    // Get the values of latitude and longitude
                    longitude = tracker.getLongitude();
                    latitude = tracker.getLatitude();

                    // convert the longitude and latitude to string to the class
                    longitudeString = String.format("%.1f",longitude);
                    latitudeString = String.format("%.1f",latitude);
                }
                else {
                    //Display success message to the customer
                    Toast.makeText(getContext(),"Error getting GPS coordinates", Toast.LENGTH_SHORT).show();
                    // Impossible read the data
                    verification = false;
                }

                // all verifications are good
                if (verification) {

                    // Create a variable of the cow
                    CowLogs newCow = new CowLogs(cowTitle.getText().toString(), cow_Id, dateString, cow_Weight, cow_Age, cow_condition,longitudeString,latitudeString);

                    // Save entry in the ArrayList on the MainActivity
                    MainActivity.cowLogsArrayList.add(newCow);

                    //Display success message to the customer
                    Toast.makeText(getContext(), "Entry Save", Toast.LENGTH_SHORT).show();

                    //Clean the entries
                    cleanEntries();
                }

                // a problem in a input
                else if (!verification) {

                    //String to display
                    String message = "Entry not saved as not all entered.\nComplete all entries and try again.";
                    // Toast the message
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                }
            }
        });

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //New object of the CowFragment
                CowList cowListFragment = new CowList();

                //Fragment transaction to begin the transaction
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Set the fragment on the screen and commit it.
                fragmentTransaction.replace(R.id.cowPlace, cowListFragment).commit();

            }
        });

        // --- Spinner values  ---

        //Assign the String values in the conditions array
        conditions = getResources().getStringArray(R.array.conditions_array);

        // get a spinner object from the id spinner
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        // Use an array of string to display the values in the spinner as an adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this.getActivity(),
                android.R.layout.simple_spinner_item, conditions);

        //Assign the dropdown in the spinner
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // set the adapter into the spinner
        spinner.setAdapter(adapter);

        // make the selection of the item in the spinner
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> arg0,
                                       View arg1, int arg2, long arg3)
            {
                int index = arg0.getSelectedItemPosition();
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                conditions[0] = "";
            }
        });

        // --- Close Spinner code ---
    }

    // Clean the entry fields on the fragment
    void cleanEntries (){

        // Clean the three entries
        cowId.setText("");
        cowAge.setText("");
        cowWeight.setText("");

        //Set the spinner to default
        cowSpinner.setSelection(0);
    }

    // on destroy method to close the GPS when the fragment is close
    @Override
    public void onDestroy() {
        super.onDestroy();
        tracker.stopUsingGPS();
    }
}

