package com.example.hp.aashayein;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class FacebookActivity extends AppCompatActivity implements View.OnClickListener{
    CallbackManager callbackManager;
    private TextView info;
    private LoginButton fbloginButton;
    private TextView  tvfull_name, tvEmail;
    String email, name;
    Button loginbutton;
    private EditText edemail, edpwd;
    ProgressDialog pb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        info = (TextView) findViewById(R.id.info);
        fbloginButton = (LoginButton) findViewById(R.id.fblogin_button);

        loginbutton=(Button)findViewById(R.id.loginbutton);
        edemail=(EditText)findViewById(R.id.ed_email);
        edpwd=(EditText)findViewById(R.id.ed_pwd);

        pb=new ProgressDialog(this);



        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


      tvfull_name = (TextView) findViewById(R.id.full_name);
        tvEmail = (TextView) findViewById(R.id.email);

        fbloginButton.setReadPermissions(Arrays.asList("public_profile", "email"));


        fbloginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


//                fbloginButton.setVisibility(View.GONE);

                GraphRequest graphRequest = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.d("JSON", "" + response.getJSONObject().toString());

                        try {
                            email = object.getString("email");
                            name = object.getString("name");


                            tvEmail.setText("email = " + email);
                          tvfull_name.setText("name = " + name);
//                            LoginManager.getInstance().logOut();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
                                + "\n" +
                                "Auth Token: "
                                + loginResult.getAccessToken().getToken()
                );

            }

            @Override
            public void onCancel() {
                info.setText("Login attempt canceled.");

            }

            @Override
            public void onError(FacebookException e) {
                info.setText("Login attempt failed.");

            }
        });

    }

    private void loginUser(){
        String em= edemail.getText().toString().trim();
        String pw= edpwd.getText().toString().trim();

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


//        firebaseAuth.signInWithEmailAndPassword(em,pw)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        pb.dismiss();
//                        if(task.isSuccessful()){
//                            //start profile activity here
//                            finish();
//                            startActivity(new Intent(FacebookActivity.this, Profile.class));
//                        }
//
//                    }
//                });

    }

    @Override
    public void onClick(View v) {
        if(v == loginbutton) {
            loginUser();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
