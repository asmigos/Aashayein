package com.example.hp.aashayein;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class Splash extends AppCompatActivity {
    CallbackManager callbackManager;
    private TextView info;
    private LoginButton loginButton;
    private TextView tvfirst_name, tvlast_namee, tvfull_name, tvEmail;
    String email,name,first_name,last_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        info = (TextView) findViewById(R.id.info);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        FacebookSdk.sdkInitialize(getApplicationContext());

        callbackManager = CallbackManager.Factory.create();


        tvfirst_name        = (TextView) findViewById(R.id.first_name);
        tvlast_namee        = (TextView) findViewById(R.id.last_name);
        tvfull_name         = (TextView) findViewById(R.id.full_name);
        tvEmail             = (TextView) findViewById(R.id.email);

        loginButton.setReadPermissions(Arrays.asList("public_profile","email"));



        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                loginButton.setVisibility(View.GONE);

                GraphRequest graphRequest   =   GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback()
                {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response)
                    {
                        Log.d("JSON", ""+response.getJSONObject().toString());

                        try
                        {
                            email       =   object.getString("email");
                            name        =   object.getString("name");
                            first_name  =   object.optString("first_name");
                            last_name   =   object.optString("last_name");

                            tvEmail.setText("email = "+email);
                            tvfirst_name.setText("first name = "+first_name);
                            tvlast_namee.setText("last name = "+last_name);
                            tvfull_name.setText("name = "+name);
                            LoginManager.getInstance().logOut();
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,first_name,last_name,email");
                graphRequest.setParameters(parameters);
                graphRequest.executeAsync();

                info.setText(
                        "User ID: "
                                + loginResult.getAccessToken().getUserId()
//                                + "\n" +
//                                "Auth Token: "
//                                + loginResult.getAccessToken().getToken()
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }


}