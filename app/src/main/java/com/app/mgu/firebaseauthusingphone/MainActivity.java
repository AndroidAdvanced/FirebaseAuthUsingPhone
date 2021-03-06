package com.app.mgu.firebaseauthusingphone;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();
    }

    PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = null;

    @Override
    public  void onResume() {
        super.onResume();

       mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {

                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                // Log.d(TAG, "onVerificationCompleted:" + credential);

                signInWithPhoneAuthCredential(credential);

                Toast.makeText(getBaseContext(), "Verification Completed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                //Log.w(TAG, "onVerificationFailed", e);

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request

                    Toast.makeText(getBaseContext(), "Verification Failed" + e.getMessage().toString(), Toast.LENGTH_LONG).show();

                    // ...
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // ...

                    Toast.makeText(getBaseContext(), "Verification Failed 111" + e.getMessage().toString(), Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(getBaseContext(), "Verification Failed 222" + e.getMessage().toString(), Toast.LENGTH_LONG).show();

                }


                // Show a message and update the UI
                // ...
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                //  Log.d(TAG, "onCodeSent:" + verificationId);

                Toast.makeText(getBaseContext(), "Code Sent" , Toast.LENGTH_LONG).show();

                // Save verification ID and resending token so we can use them later
                //  mVerificationId = verificationId;
                //  mResendToken = token;

                // ...
            }
        };

        ((Button) findViewById(R.id.btnSend)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseApp.initializeApp(getBaseContext());

                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "9574306750",        // Phone number to verify
                        60,                 // Timeout duration
                        TimeUnit.SECONDS,   // Unit of timeout
                        MainActivity.this,               // Activity (for callback binding)
                        mCallbacks);        // OnVerificationStateChangedCallbacks


            }
        });
    }

    private FirebaseAuth mAuth;

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                          //  Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            Toast.makeText(getBaseContext(), "Success" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Success" + user.getEmail(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Success" + user.getPhoneNumber(), Toast.LENGTH_LONG).show();
                            Toast.makeText(getBaseContext(), "Success" + user.getProviderId(), Toast.LENGTH_LONG).show();
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Toast.makeText(getBaseContext(), "signInWithCredential:failure", Toast.LENGTH_LONG).show();

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }
}
