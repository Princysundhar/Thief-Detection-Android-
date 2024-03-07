package com.example.thiefdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class user_register extends AppCompatActivity {
    EditText e1, e2, e3, e4, e5, e6, e7;
    ImageView img;
//    TextView t1;
    Button b1;
    SharedPreferences sh;
    String url;
    Bitmap bitmap = null;
    ProgressDialog pd;

    String MobilePattern = "[6-9][0-9]{9}";
    String PinPattern = "[6-9][0-9]{5}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    //    String password_pattern="[A-Z][a-z][0-9]{3,8}";
    String password_pattern="[A-Za-z0-9]{3,8}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_register);

        sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        sh.getString("ipaddress", "");
        url = sh.getString("url", "") + "android_user_registration";

        e1 = findViewById(R.id.editTextTextPersonName3);
        e2 = findViewById(R.id.editTextTextPersonName4);
        e3 = findViewById(R.id.editTextTextPersonName5);
        e4 = findViewById(R.id.editTextTextPersonName6);
        e5 = findViewById(R.id.editTextTextPersonName7);
        e6 = findViewById(R.id.editTextTextPersonName8);
        e7 = findViewById(R.id.editTextTextPassword2);
//        t1 = findViewById(R.id.textView16);
        img = findViewById(R.id.imageView3);
        b1 = findViewById(R.id.button4);

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, 100);
            }
        });

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = e1.getText().toString();
                String place = e2.getText().toString();
                String post = e3.getText().toString();
                String pin = e4.getText().toString();
                String email = e5.getText().toString();
                String contact = e6.getText().toString();
                String password = e7.getText().toString();

                int flag = 0;
                if(name.equalsIgnoreCase("")){
                    e1.setError("Enter name");
                    flag++;
                }
                if(place.equalsIgnoreCase("")){
                    e2.setError("Enter place");
                    flag++;
                }
                if(post.equalsIgnoreCase("")){
                    e3.setError("Enter post");
                    flag++;
                }
                if(!pin.matches(PinPattern)){
                    e4.setError("Enter valid pin");
                    flag++;
                }
                if(!email.matches(emailPattern)){
                    e5.setError("Enter the valid email");
                    flag++;
                }
                if(!contact.matches(MobilePattern)){
                    e6.setError("Enter contact");
                    flag++;
                }
                if(!password.matches(password_pattern)){
                    Toast.makeText(user_register.this, "Enter valid password", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                if(bitmap==null){
                    Toast.makeText(user_register.this, "Choose image", Toast.LENGTH_SHORT).show();
                    flag++;
                }
                if(flag==0) {
                    uploadBitmap(name, place, post, pin, email, contact, password);
                }
            }
        });
    }

        @Override
        protected void onActivityResult ( int requestCode, int resultCode, Intent data){
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == 100 && resultCode == RESULT_OK && data != null) {

                Uri imageUri = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

                    img.setImageBitmap(bitmap);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //converting to bitarray
        public byte[] getFileDataFromDrawable (Bitmap bitmap){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
            return byteArrayOutputStream.toByteArray();
        }
        private void uploadBitmap(String name, String place, String post, String pin, String email, String contact, String password) {
        {


            pd = new ProgressDialog(user_register.this);
            pd.setMessage("Uploading....");
            pd.show();
            VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, url,
                    new Response.Listener<NetworkResponse>() {
                        @Override
                        public void onResponse(NetworkResponse response) {
                            try {
                                pd.dismiss();


                                JSONObject obj = new JSONObject(new String(response.data));

                                if (obj.getString("status").equals("ok")) {
                                    Toast.makeText(getApplicationContext(), "Registration success", Toast.LENGTH_SHORT).show();
                                    android.content.Intent i = new Intent(getApplicationContext(), login.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Registration failed", Toast.LENGTH_SHORT).show();
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }) {


                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    SharedPreferences o = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    params.put("name", name);//passing to python
                    params.put("place", place);//passing to python
                    params.put("post", post);
                    params.put("pin", pin);
                    params.put("email", email);
                    params.put("contact", contact);
                    params.put("password", password);
                    return params;
                }


                @Override
                protected Map<String, DataPart> getByteData() {
                    Map<String, DataPart> params = new HashMap<>();
                    long imagename = System.currentTimeMillis();
                    params.put("pic", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(volleyMultipartRequest);

        }
    }

}