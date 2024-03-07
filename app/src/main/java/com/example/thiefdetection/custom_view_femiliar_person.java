package com.example.thiefdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class custom_view_femiliar_person extends BaseAdapter {
    String [] fid,fname,fplace,femail,fcontact,fimage;
    private Context context;
    SharedPreferences sh;
    String url;

    public custom_view_femiliar_person(Context applicationContext, String[] fid, String[] fname, String[] fplace, String[] femail, String[] fcontact, String[] fimage) {
        this.context = applicationContext;
        this.fid = fid;
        this.fname =fname;
        this.fplace = fplace;
        this.femail = femail;
        this.fcontact = fcontact;
        this.fimage = fimage;

    }
    @Override
    public int getCount() {
        return fname.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View gridView;
        if (view == null) {
            gridView = new View(context);
            //gridView=inflator.inflate(R.layout.customview, null);
            gridView = inflator.inflate(R.layout.activity_custom_view_femiliar_person, null);//same class name

        } else {
            gridView = (View) view;

        }
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView8);
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView48);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView50);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView52);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView54);
//        TextView tv5 = (TextView) gridView.findViewById(R.id.textView38);

        Button b1 = (Button)gridView.findViewById(R.id.button6);// UPDATE
        b1.setTag(i);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int pos=(int)view.getTag();
                

                SharedPreferences sh=PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor ed=sh.edit();
                ed.putString("fid",fid[pos]);
                Toast.makeText(context, ""+fid[pos], Toast.LENGTH_SHORT).show();
                ed.commit();
                Intent i = new Intent(context,update_femiliar_person.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(i);

            }
        });
        Button b2 = (Button)gridView.findViewById(R.id.button7);        // Delete
        b2.setTag(i);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int pos=(int)view.getTag();
                sh = PreferenceManager.getDefaultSharedPreferences(context);
                sh.getString("ipaddress","");
                url = sh.getString("url","")+"android_delete_familiar_person";

                RequestQueue requestQueue = Volley.newRequestQueue(context);
                StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //  Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                                try {
                                    JSONObject jsonObj = new JSONObject(response);
                                    if (jsonObj.getString("status").equalsIgnoreCase("ok")) {
                                        Toast.makeText(context, "Delete Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(context.getApplicationContext(),view_femiliar_person.class);
                                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        context.startActivity(i);


                                    } else {
                                        Toast.makeText(context, "Not found", Toast.LENGTH_LONG).show();
                                    }

                                } catch (Exception e) {
                                    Toast.makeText(context, "Error" + e.getMessage().toString(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Toast.makeText(context, "eeeee" + error.toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                ) {

                    //                value Passing android to python
                    @Override
                    protected Map<String, String> getParams() {
                        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("fid", fid[pos]);//passing to python
//                        params.put("lid",sh.getString("lid",""));
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
        });



        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
//        tv5.setTextColor(Color.BLACK);



        tv1.setText(fname[i]);
        tv2.setText(fplace[i]);
        tv3.setText(femail[i]);
        tv4.setText(fcontact[i]);


        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("ipaddress", "");
        String url = "http://" + ip + ":4000" + fimage[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(imageView);//circle

//
        return gridView;
    }
}