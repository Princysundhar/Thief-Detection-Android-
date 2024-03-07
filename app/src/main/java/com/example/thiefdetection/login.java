package com.example.thiefdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class login extends AppCompatActivity {
    EditText e1,e2;
    Button b1,b2;
    SharedPreferences sh;
    String url;
    String password_pattern="[A-Za-z0-9]{3,8}";


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        e1 = findViewById(R.id.editTextTextPersonName2);
        e2 = findViewById(R.id.editTextTextPassword);
        b1 = findViewById(R.id.button2);
        b2 = findViewById(R.id.button3);            // Register
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),user_register.class);
                startActivity(i);

            }
        });
        b1.setOnClickListener(new View.OnClickListener() {      // login
            @Override
            public void onClick(View view) {
                String username = e1.getText().toString();
                String password = e2.getText().toString();
                int flag =0;
                if(username.equalsIgnoreCase("")){
                    e1.setError("Enter username");
                    flag ++;
                }
                if(!password.matches(password_pattern)){
                    Toast.makeText(login.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                if(flag == 0) {
                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ipaddress","");
                    url = sh.getString("url","")+"android_login";

                    RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
                    StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                    try {
                                        JSONObject jsonObj = new JSONObject(response);
                                        if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
//                                Toast.makeText(Login.this, "welcome", Toast.LENGTH_SHORT).show();
                                            String typ = jsonObj.getString("type");
                                            String id = jsonObj.getString("lid");
                                            String name = jsonObj.getString("name");
                                            String email = jsonObj.getString("email");
                                            String photo = jsonObj.getString("photo");
                                            SharedPreferences.Editor ed = sh.edit();
                                            ed.putString("lid", id);
                                            ed.putString("name", name);
                                            ed.putString("email", email);
                                            ed.putString("photo", photo);
                                            ed.commit();
                                            if (typ.equalsIgnoreCase("user")) {
                                                Toast.makeText(getApplicationContext(), "Welcome", Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(getApplicationContext(), Home.class);
                                                startActivity(i);
                                            }
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Not found", Toast.LENGTH_LONG).show();
                                        }

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    // error
                                    Toast.makeText(getApplicationContext(), "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                    ) {

                        //                value Passing android to python
                        @Override
                        protected Map<String, String> getParams() {
                            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                            Map<String, String> params = new HashMap<String, String>();

                            params.put("name", username);//passing to python
                            params.put("password", password);


                            return params;
                        }
                    };


                    int MY_SOCKET_TIMEOUT_MS = 100000;

                    postRequest.setRetryPolicy(new DefaultRetryPolicy(
                            MY_SOCKET_TIMEOUT_MS,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(postRequest);



                }
            }
        });


    }
}