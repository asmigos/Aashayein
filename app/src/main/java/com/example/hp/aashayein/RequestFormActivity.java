package com.example.hp.aashayein;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RequestFormActivity extends AppCompatActivity {
    EditText bName, bContact, bloodGrp, hName, pName, dName, dContact, diName, pFrom, bFrom;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_form);

        bName=(EditText)findViewById(R.id.editText_bName);
        bContact=(EditText)findViewById(R.id.editText_bContact);
        bloodGrp=(EditText)findViewById(R.id.editText_bloodGrp);
        hName=(EditText)findViewById(R.id.editText_hName);
        pName=(EditText)findViewById(R.id.editText_pName);
        dName=(EditText)findViewById(R.id.editText_dName);
        dContact=(EditText)findViewById(R.id.editText_dContact);
        diName=(EditText)findViewById(R.id.editText_diName);
        pFrom=(EditText)findViewById(R.id.editText_pFrom);
        bFrom=(EditText)findViewById(R.id.editText_bFrom);

        next=(Button)findViewById(R.id.nextbutton);



        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(RequestFormActivity.this,HomeActivity.class);
                startActivity(i);

            }
        });

    }
}
