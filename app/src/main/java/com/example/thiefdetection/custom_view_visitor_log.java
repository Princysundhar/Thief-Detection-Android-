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

public class custom_view_visitor_log extends BaseAdapter {
    String [] vid,image,date,time;
    private Context context;
    SharedPreferences sh;


    public custom_view_visitor_log(Context applicationContext, String[] vid, String[] image, String[] date, String[] time) {
        this.context = applicationContext;
        this.vid = vid;
        this.image = image;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_visitor_log, null);//same class name

        } else {
            gridView = (View) view;

        }
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView4);
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView18);

        TextView tv2 = (TextView) gridView.findViewById(R.id.textView20);
//        TextView tv3 = (TextView) gridView.findViewById(R.id.t61);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
//        tv3.setTextColor(Color.BLACK);


        tv1.setText(date[i]);
        tv2.setText(time[i]);
//        tv3.setText(date[i]);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("ipaddress", "");
        String url = "http://" + ip + ":8000" + image[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(imageView);//circle

//
        return gridView;
    }
}

