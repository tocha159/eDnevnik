package com.examples.your.ednevnik.Ucenik;

import android.app.FragmentManager;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;

public class StudentNavDraw extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    TextView osoba_info;
    TextView osoba_username;
    CircularImageView imageView;
    Student s;
    SharedPreferences prefs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_nav_draw);
        SugarContext.init(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        s=Student.findById(Student.class,prefs.getLong("studid",1));

        imageView= (CircularImageView) hView.findViewById(R.id.slika_info);
        osoba_info= (TextView) hView.findViewById(R.id.osoba_info);
        osoba_info= (TextView) hView.findViewById(R.id.osoba_info);
        osoba_username= (TextView) hView.findViewById(R.id.osoba_username);

        osoba_info.setText(s.getName()+" "+s.getSurname());
        osoba_username.setText(s.getUsername());

        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Dnevnik/"+s.getUsername()+".jpg");
        try{
            Picasso.with(this).load(file).into(imageView);
        }
        catch (Exception e){
            Picasso.with(this).load(R.drawable.logo).into(imageView);

        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.student_nav_draw, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        FragmentManager fm=getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.moji_predmeti) {
            fm.beginTransaction().replace(R.id.content_frame_student,new StudentPregledPredmeta()).commit();
        } else if (id == R.id.moje_ocjene) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
