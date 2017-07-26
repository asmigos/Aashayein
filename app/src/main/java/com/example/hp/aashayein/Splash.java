package com.example.hp.aashayein;


import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;

import com.facebook.Profile;
import com.facebook.ProfileTracker;

import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class Splash extends AppCompatActivity implements View.OnClickListener{
    private Button loginbutton;
    private EditText editTextemail,editText_Password;
    private TextView signupbutton;
    private ProgressDialog pb;
    private FirebaseAuth firebaseAuth;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    //Facebook login button
    private FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            Profile profile = Profile.getCurrentProfile();
            nextActivity(profile);
        }
        @Override
        public void onCancel() {        }
        @Override
        public void onError(FacebookException e) {      }
    };






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldToken, AccessToken newToken) {
            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                nextActivity(newProfile);
            }
        };
        accessTokenTracker.startTracking();
        profileTracker.startTracking();
        LoginButton loginButton = (LoginButton)findViewById(R.id.fblogin_button);
        callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken accessToken = loginResult.getAccessToken();
                Profile profile = Profile.getCurrentProfile();
                nextActivity(profile);
                Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException e) {
            }
        };
        loginButton.setReadPermissions("user_friends");
        loginButton.registerCallback(callbackManager, callback);





        firebaseAuth=FirebaseAuth.getInstance();
//        if(firebaseAuth.getCurrentUser()!=null){
//            //start profile activity directly
//            finish();
//            startActivity(new Intent(Login.this, Profile.class));
//        }

        loginbutton=(Button)findViewById(R.id.loginbutton);
        editTextemail=(EditText)findViewById(R.id.editText_Email);
        editText_Password=(EditText)findViewById(R.id.editText_Password);
        signupbutton=(Button)findViewById(R.id.signupbutton);
        pb=new ProgressDialog(this);
        loginbutton.setOnClickListener(this);
        signupbutton.setOnClickListener(this);
    }

    private void loginUser(){
        String em= editTextemail.getText().toString().trim();
        String pw= editText_Password.getText().toString().trim();

        if(TextUtils.isEmpty(em)){
            Toast.makeText(this,"please enter the email",Toast.LENGTH_SHORT).show();
            return;
        }



        if(TextUtils.isEmpty(pw)){
            Toast.makeText(this,"please enter the password",Toast.LENGTH_SHORT).show();
            return;
        }
        pb.setMessage("you req is being prcessed.. please wait....!! ");
        pb.show();


        firebaseAuth.signInWithEmailAndPassword(em,pw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pb.dismiss();
                        if(task.isSuccessful()){
                            //start profile activity here
                            finish();
                            startActivity(new Intent(Splash.this, Profile.class));
                        }

                    }
                });

    }

    @Override
    public void onClick(View v) {
        if(v == loginbutton) {
            loginUser();
        }
        if(v == signupbutton){
            // will open login activity here
            finish();
            startActivity(new Intent(this,SignUpActivity.class));
        }


    }









    @Override
    protected void onResume() {
        super.onResume();
        //Facebook login
        Profile profile = Profile.getCurrentProfile();
        nextActivity(profile);
    }

    @Override
    protected void onPause() {

        super.onPause();
    }

    protected void onStop() {
        super.onStop();
        //Facebook login
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        super.onActivityResult(requestCode, responseCode, intent);
        //Facebook login
        callbackManager.onActivityResult(requestCode, responseCode, intent);

    }


    private void nextActivity(Profile profile) {
        if (profile != null) {
            Intent main = new Intent(Splash.this, HomeActivity.class);
            main.putExtra("name", profile.getFirstName());
            main.putExtra("email", profile.getId());
            main.putExtra("surname", profile.getLastName());
            main.putExtra("imageUrl", profile.getProfilePictureUri(200, 200).toString());

            startActivity(main);

        }
    }
}



