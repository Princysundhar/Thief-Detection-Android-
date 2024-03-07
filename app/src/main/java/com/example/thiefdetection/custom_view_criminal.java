package com.example.thiefdetection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class custom_view_criminal extends BaseAdapter {
    String [] cid,name,category,age,gender,place,post,pin,id_proof,number,photo;
    private Context context;

    public custom_view_criminal(Context applicationContext, String[] cid, String[] name,String[] category, String[] age, String[] gender, String[] place, String[] post, String[] pin, String[] id_proof,String[] number,String[] photo) {
        this.context = applicationContext;
        this.cid = cid;
        this.name = name;
        this.category = category;
        this.age = age;
        this.gender = gender;
        this.place = place;
        this.post = post;
        this.pin = pin;
        this.id_proof = id_proof;
        this.number = number;
        this.photo = photo;

    }


    @Override
    public int getCount() {
        return number.length;
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
            gridView = inflator.inflate(R.layout.activity_custom_view_criminal, null);//same class name

        } else {
            gridView = (View) view;

        }
        ImageView imageView = (ImageView) gridView.findViewById(R.id.imageView6);
        TextView tv1 = (TextView) gridView.findViewById(R.id.textView30);
        TextView tv2 = (TextView) gridView.findViewById(R.id.textView32);
        TextView tv3 = (TextView) gridView.findViewById(R.id.textView34);
        TextView tv4 = (TextView) gridView.findViewById(R.id.textView36);
        TextView tv5 = (TextView) gridView.findViewById(R.id.textView38);
        TextView tv6 = (TextView) gridView.findViewById(R.id.textView40);
        TextView tv7 = (TextView) gridView.findViewById(R.id.textView42);
        TextView tv8 = (TextView) gridView.findViewById(R.id.textView44);
        TextView tv9 = (TextView) gridView.findViewById(R.id.textView46);


        tv1.setTextColor(Color.RED);//color setting
        tv2.setTextColor(Color.BLACK);
        tv3.setTextColor(Color.BLACK);
        tv4.setTextColor(Color.BLACK);
        tv5.setTextColor(Color.BLACK);
        tv6.setTextColor(Color.BLACK);
        tv7.setTextColor(Color.BLACK);
        tv8.setTextColor(Color.BLACK);
        tv9.setTextColor(Color.BLACK);

        tv8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
                String url1 = sh.getString("url", "") + id_proof[i];
                openFile(url1);

            }
        });
        tv1.setText(name[i]);
        tv2.setText(category[i]);
        tv3.setText(age[i]);
        tv4.setText(gender[i]);
        tv5.setText(place[i]);
        tv6.setText(post[i]);
        tv7.setText(pin[i]);
        tv8.setText(id_proof[i]);
        tv9.setText(number[i]);

        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(context);
        String ip = sh.getString("ipaddress", "");
        String url = "http://" + ip + ":4000" + photo[i];
        Picasso.with(context).load(url).transform(new CircleTransform()).into(imageView);//circle

//
        return gridView;
    }

        public void openFile(String url) {

            Uri uri = Uri.parse(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip") || url.toString().contains(".rar")) {
                // WAV audio file
                intent.setDataAndType(uri, "application/x-wav");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") || url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                //if you want you can also define the intent type for any other file
                //additionally use else clause below, to manage other unknown extensions
                //in this case, Android will show all applications installed on the device
                //so you can choose which application to use
                intent.setDataAndType(uri, "*/*");
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }
}