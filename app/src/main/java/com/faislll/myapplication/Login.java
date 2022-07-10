package com.faislll.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;


public class Login extends AppCompatActivity {
    private static final String TAG = "LOGIN";
    // variable for Firebase Auth
    private FirebaseAuth mFirebaseAuth;
    private GoogleSignInClient mSignInClient;

    // declaring a const int value which we
    // will be using in Firebase auth.
    public static final int RC_SIGN_IN = 1;

    // creating an auth listener for our Firebase auth
    private FirebaseAuth.AuthStateListener mAuthStateListner;
    private Button buttonLoginGoogle;

    // below is the list which we have created in which
    // we can add the authentication which we have to
    // display inside our app.
//    List<AuthUI.IdpConfig> providers = Arrays.asList(

            // below is the line for adding
            // email and password authentication.
//            new AuthUI.IdpConfig.EmailBuilder().build(),

            // below line is used for adding google
            // authentication builder in our app.
//            new AuthUI.IdpConfig.GoogleBuilder().build());

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
//                buttonLoginGoogle.setOnClickListener(view -> startActivityForResult(
//                        AuthUI.getInstance().createSignInIntentBuilder().setIsSmartLockEnabled(false)
//                                .setAvailableProviders(providers)
//                                .setLogo(R.drawable.cook).setTheme(R.style.Theme)
//                                .build(),
//                        RC_SIGN_IN
//                ));

                buttonLoginGoogle.setOnClickListener(v -> {
                    Intent signInIntent = mSignInClient.getSignInIntent();
                    startActivityForResult(signInIntent, RC_SIGN_IN);

                });

                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
                mSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(Login.this, MainActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }
}