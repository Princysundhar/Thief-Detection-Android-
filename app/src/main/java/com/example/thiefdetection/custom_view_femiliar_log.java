package com.example.thiefdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class custom_view_femiliar_log extends BaseAdapter {
    String[] fid, fname,fimage,fcontact,date, time;
    private Context context;


    public custom_view_femiliar_log(Context applicationContext, String[] fid,String[] fname,String[] fimage,String[] fcontact, String[] date, String[] time) {
        this.context = applicationContext;
        this.fid = fid;
        this.fname = fname;
        this.fimage = fimage;
        this.fcontact = fcontact;
        this.date = date;
        this.time = time;

    }

    @Override
    public int getCount() {
        return date.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_femiliar_log, null);//same class name

        } else {
            gridView = (View) view;

        }
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView5);
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView22);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView24);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView26);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView28);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);


        tv1.setText(fname[i]);
        tv2.setText(fcontact[i]);
        tv3.setText(date[i]);
        tv4.setText(time[i]);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("ipaddress", "");
        String url = "http://" + ip + ":8000" + fimage[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(imageView);//circle

//
        return gridView;
    }
}
