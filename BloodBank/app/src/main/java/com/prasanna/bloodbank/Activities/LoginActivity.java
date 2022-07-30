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

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {
     private EditText EmailidEt, PasswordEt;
     private TextView signup;
     private Button btnlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EmailidEt = findViewById(R.id.emailid);
        PasswordEt = findViewById(R.id.password);
        btnlogin = findViewById(R.id.btnlogin);
        signup = findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EmailidEt.setError(null);
                PasswordEt.setError(null);
                String emailid = EmailidEt.getText().toString();
                String password = PasswordEt.getText().toString();
                if(isValid(emailid, password)){
                    Login (emailid, password);
                }
            }
        });
    }
    private void Login (String emailid, String password){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Endpoints.login_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Success")) {
                    Toast.makeText(LoginActivity.this,response, Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    LoginActivity.this.finish();
                } else {
                    Toast.makeText(LoginActivity.this,response, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(LoginActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                Log.d("VOLLEY", error.getMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("emailid", emailid);
                params.put("password", password);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest);
    }

    private boolean isValid(String emailid, String password){
        if(emailid.isEmpty()){
            showMessage("Enter your Email Id!!");
            EmailidEt.setError("Enter your Email Id!!");
            return false;
        }else if(password.isEmpty()){
            showMessage("Enter the Password");
            PasswordEt.setError("Enter your Password");
            return false;
        }
        return false;
    }

    private void showMessage(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}

