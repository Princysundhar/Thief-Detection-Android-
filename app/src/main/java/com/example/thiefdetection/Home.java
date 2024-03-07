package com.example.thiefdetection;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.thiefdetection.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    ImageView img1;
    TextView t1,t2;
    SharedPreferences sh;
    String url;

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        View headerView = navigationView.getHeaderView(0);
        t1 = headerView.findViewById(R.id.textView2);
        t2 = headerView.findViewById(R.id.textView);
        img1 = headerView.findViewById(R.id.imageView);
//
        SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String ip = sh.getString("ipaddress", "");

        String url = "http://" + ip + ":4000" + sh.getString("photo","");    // For Image
//        Toast.makeText(this, ""+url, Toast.LENGTH_SHORT).show();
        Picasso.with(getApplicationContext()).load(url).transform(new CircleTransform()).into(img1);//circle

        t1.setText(sh.getString("name",""));
        t2.setText(sh.getString("email",""));

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        navigationView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            SharedPreferences.Editor ed = sh.edit();
            ed.commit();
            ed.clear();
            Intent i = new Intent(getApplicationContext(),ip_page.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            return true;

        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id==R.id.nav_home){
            Intent i = new Intent(getApplicationContext(),view_profile.class);
            startActivity(i);

        }
        else if(id==R.id.nav_gallery){
            Intent i = new Intent(getApplicationContext(),view_visitor_log.class);
            startActivity(i);

        }
        else if(id==R.id.nav_slideshow){
            Intent i = new Intent(getApplicationContext(),view_femiliar_log.class);
            startActivity(i);

        }

        else if(id==R.id.nav_slideshoww){
            Intent i = new Intent(getApplicationContext(),view_criminal.class);
            startActivity(i);

        }
        else if(id==R.id.nav_galleryy){
            Intent i = new Intent(getApplicationContext(),view_femiliar_person.class);
            startActivity(i);

        }
        else if(id==R.id.nav_gallerys){
            Intent i = new Intent(getApplicationContext(),view_camera.class);
            startActivity(i);

        }
//        else if(id == R.id.nav_slideshows){                 // Logout
//            SharedPreferences sh = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
//            SharedPreferences.Editor ed = sh.edit();
//            ed.commit();
//            ed.clear();
//            Intent i = new Intent(getApplicationContext(),ip_page.class);
//            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(i);
//
//        }
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            drawer.closeDrawer(GravityCompat.START);
            return true;
    }
}
