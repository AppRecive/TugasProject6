package com.faislll.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {
    // variable for Firebase Auth
    private FirebaseAuth mFirebaseAuth;

    // declaring a const int value which we
    // will be using in Firebase auth.
    public static final int RC_SIGN_IN = 1;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private Button buttonLoginGoogle;

    // below is the list which we have created in which
    // we can add the authentication which we have to
    // display inside our app.
    List<AuthUI.IdpConfig> providers = Arrays.asList(

            // below is the line for adding
            // email and password authentication.
//            new AuthUI.IdpConfig.EmailBuilder().build(),

            // below line is used for adding google
            // authentication builder in our app.
            new AuthUI.IdpConfig.GoogleBuilder().build());

            // below line is used for adding phone
            // authentication builder in our app.
//            new AuthUI.IdpConfig.PhoneBuilder().build());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // below line is for getting instance
        // for our firebase auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        buttonLoginGoogle = findViewById(R.id.button_login_google);

        // below line is used for calling auth listener
        // for oue Firebase authentication.
        mAuthStateListner = firebaseAuth -> {

            // we are calling method for on authentication state changed.
            // below line is used for getting current user which is
            // authenticated previously.
            FirebaseUser user = firebaseAuth.getCurrentUser();

            // checking if the user
            // is null or not.
            if (user != null) {
                // if the user is already authenticated then we will
                // redirect our user to next screen which is our home screen.
                // we are redirecting to new screen via an intent.
                Intent i = new Intent(Login.this, MainActivity.class);
                startActivity(i);
                // we are calling finish method to kill or
                // mainactivity which is displaying our login ui.
                finish();
            } else {
                buttonLoginGoogle.setOnClickListener(view -> {
                    startActivityForResult(
                            AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
                                    .setAvailableProviders(providers)
                                    .setLogo(R.drawable.cook).setTheme(R.style.Theme)
                                    .build(),
                            RC_SIGN_IN
                    );
                });

            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();
        // we are calling our auth
        // listener method on app resume.
        mFirebaseAuth.addAuthStateListener(mAuthStateListner);
    }

    @Override
    protected void onPause() {
        super.onPause();
        // here we are calling remove auth
        // listener method on stop.
        mFirebaseAuth.removeAuthStateListener(mAuthStateListner);
    }
}