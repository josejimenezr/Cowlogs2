package com.jljim.cowlogs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Assessment purpose:  Record cow breed into a database
 * Assessment number:   2
 * Unit Coordinator:    Ron Balsys
 * Lecture & Tutor:     Partha Gangavalli
 * File name:           ProfileFragment.java
 * File purpose:        Fragment to show the profile to add a new username
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {

    // Initialised the button  variables
    Button save;
    Button cancel;

    // Initialised textView variables
    TextView usernameView;
    TextView passwordView;
    TextView repeatPasswordView;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        // Set values to the buttons
        save = (Button) getActivity().findViewById(R.id.saveProfile);
        cancel = (Button) getActivity().findViewById(R.id.showProfile);

        // Set value to the text view
        usernameView = (TextView) getActivity().findViewById(R.id.usernameInput);
        passwordView = (TextView) getActivity().findViewById(R.id.passwrodInput);
        repeatPasswordView = (TextView) getActivity().findViewById(R.id.repeatPasswordInput);

        // function to the save
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //verification variable as a all right to check erroes
                int verification = 1;

                //Initialised username variable with the value from user
                String username = usernameView.getText().toString();

                // verify that the username is not empty and at least 8 characters
                if (username.equals("")||username == null||username.contains(" ")) {
                    //Error message
                    Toast.makeText(getContext(),"Username cannot be empty or have whitespaces",Toast.LENGTH_SHORT).show();

                    // wrong username
                    verification = 0;
                }
                else if (username.length()>20){
                    //Error message
                    Toast.makeText(getContext(),"Username cannot be longer than 20 characters",Toast.LENGTH_SHORT).show();

                    // long username
                    verification = 0;
                }

                //Initialised the password and repeat password
                String password = passwordView.getText().toString();
                String repeatPassword = repeatPasswordView.getText().toString();

                // check that the password is at least 8 characters
                if (password.equals("")||password == null||password.length()<8||password.length()>20) {
                    // Error message
                    Toast.makeText(getContext(),"Password must be be between 8 to 20 characters", Toast.LENGTH_SHORT).show();

                    // worn type of password
                    verification = 0;
                }

                // check that the password and repeat password are the same
                if (!(password.equals(repeatPassword))) {
                    // Error message
                    Toast.makeText(getContext(),"Passwords must match", Toast.LENGTH_SHORT).show();

                    // worn type of password
                    verification = 0;
                }

                // check that data is correct
                if (verification == 1) {

                    //Save data in the local variable in MainActivity
                    MainActivity.loggerUsername = username;
                    MainActivity.password = password;

                    // Successfully username creation
                    Toast.makeText(getContext(),"Logger User Successfully Created", Toast.LENGTH_SHORT).show();

                    // clear the inputs
                    usernameView.setText("");
                    passwordView.setText("");
                    repeatPasswordView.setText("");

                    // Return home

                    // home fragment object to coomit the replaciment
                    HomeFragment homeFragment = new HomeFragment();

                    //Fragment transaction to begin the transaction
                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                    // Set the fragment on the screen and commit it.
                    fragmentTransaction.replace(R.id.cowPlace, homeFragment).commit();

                }

                // Any of the inputs is not right
                else if (verification == 0) {
                    Toast.makeText(getContext(),"Logger User Not Created", Toast.LENGTH_SHORT).show();

                }


            }
        });

        // function to cancel button
        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                // home fragment object to commit the replacement
                HomeFragment homeFragment = new HomeFragment();

                //Fragment transaction to begin the transaction
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Set the fragment on the screen and commit it.
                fragmentTransaction.replace(R.id.cowPlace, homeFragment).commit();
            }
        });
    }

}
