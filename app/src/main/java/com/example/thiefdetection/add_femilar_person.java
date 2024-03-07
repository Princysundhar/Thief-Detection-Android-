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

public class add_femilar_person extends AppCompatActivity {
    EditText e1, e2, e3, e4;
    ImageView img;
    Button b1;
    SharedPreferences sh;
    String url;
    Bitmap bitmap = null;
    ProgressDialog pd;
    String MobilePattern = "[6-9][0-9]{9}";
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_femilar_person);
        e1 = findViewById(R.id.editTextTextPersonName9);
        e2 = findViewById(R.id.editTextTextPersonName10);
        e3 = findViewById(R.id.editTextTextEmailAddress);
        e4 = findViewById(R.id.editTextPhone);
        img = findViewById(R.id.imageView7);
        b1 = findViewById(R.id.button5);
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
                String email = e3.getText().toString();
                String contact = e4.getText().toString();
                int flag = 0;
                if(name.equalsIgnoreCase("")){
                    e1.setError("Enter name");
                    flag++;
                }
                else if(place.equalsIgnoreCase("")){
                    e2.setError("Enter place");
                    flag++;
                }
                else if(!email.matches(emailPattern)){
                    e3.setError("Enter email");
                    flag++;
                }
                else if(!contact.matches(MobilePattern)){
                    e4.setError("Enter contact");
                    flag++;
                }
                if  (flag == 0) {
                    sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    sh.getString("ip", "");
                    url = sh.getString("url", "") + "android_add_familiar_person";

                    uploadBitmap(name, place, email, contact);
                }
            }

        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

    private void uploadBitmap(String name, String place, String email, String contact) {
        {


            pd = new ProgressDialog(add_femilar_person.this);
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
                                    Toast.makeText(getApplicationContext(), "adding success", Toast.LENGTH_SHORT).show();
                                    android.content.Intent i = new Intent(getApplicationContext(), view_femiliar_person.class);
                                    startActivity(i);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Adding failed", Toast.LENGTH_SHORT).show();
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
                    params.put("lid",sh.getString("lid",""));
                    params.put("name", name);//passing to python
                    params.put("place", place);//passing to python
                    params.put("email", email);
                    params.put("contact", contact);
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
