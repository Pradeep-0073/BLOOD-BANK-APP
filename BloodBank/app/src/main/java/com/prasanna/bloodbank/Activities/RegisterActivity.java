package com.prasanna.bloodbank.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.prasanna.bloodbank.R;
import com.prasanna.bloodbank.Utils.Endpoints;
import com.prasanna.bloodbank.Utils.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    private EditText usernameEt, emailidEt, passwordEt,phoneEt,bloodEt;
    private Button btnregister;
    private TextView already;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEt = findViewById(R.id.username);
        emailidEt=findViewById(R.id.emailid);
        passwordEt=findViewById(R.id.password);
        phoneEt=findViewById(R.id.phone);
        bloodEt=findViewById(R.id.blood);
        btnregister=findViewById(R.id.btnregister);
        already=findViewById(R.id.alreadyhaveaccount);
        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username,emailid,password,phone,blood;
                username=usernameEt.getText().toString();
                emailid=emailidEt.getText().toString();
                password=passwordEt.getText().toString();
                phone=phoneEt.getText().toString();
                blood=bloodEt.getText().toString();
                if (isValid(username,emailid,password,phone,blood)){
                     register(username,emailid,password,phone,blood);
               }
            }
        });
    }

    private void register(final String username, String emailid, String password, String phone, String blood){
        StringRequest stringRequest= new StringRequest(Request.Method.POST, Endpoints.register_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(response.equals("Success")){
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    RegisterActivity.this.finish();
                }else{
                    Toast.makeText(RegisterActivity.this, response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error)
            {
                Toast.makeText(RegisterActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params= new HashMap<>();
                params.put("username", username);
                params.put("emailid", emailid);
                params.put("password", password);
                params.put("phone", phone);
                params.put("blood", blood);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String username, String emailid, String password, String phone, String blood)
    {
        List<String>valid_blood_group= new ArrayList<>();
        valid_blood_group.add("A+");
        valid_blood_group.add("A-");
        valid_blood_group.add("B+");
        valid_blood_group.add("B-");
        valid_blood_group.add("AB+");
        valid_blood_group.add("AB-");
        valid_blood_group.add("O+");
        valid_blood_group.add("O-");
        valid_blood_group.add("A1B+");

        if(username.isEmpty()) {
            showmessage("Name is empty");
            return false;
        }else if (emailid.isEmpty()){
            showmessage("Email id is required");
            return false;
        }else if (!valid_blood_group.contains(blood)){
            showmessage("Blood Group is invalid choose from"+valid_blood_group);
            return false;
        }else if (phone.length()!=10){
            showmessage("Invalid Mobile Number. Number should be of 10 Digits");
            return false;
        }
        return true;
    }

    private void showmessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}