package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.MainActivity;
import com.examples.your.ednevnik.Model.Professor;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;

public class Navi_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    TextView osoba_info;
    TextView osoba_username;
    Professor p;
    SharedPreferences prefs;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_main);
        SugarContext.init(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);

        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout, toolbar,R.string.open,R.string.close);
        mDrawerLayout.setDrawerListener(mToggle);
        mToggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);
        p=Professor.findById(Professor.class,prefs.getLong("profid",1));

        osoba_info= (TextView) hView.findViewById(R.id.osoba_info);
        osoba_username= (TextView) hView.findViewById(R.id.osoba_username);

        osoba_info.setText(p.getName()+" "+p.getSurname());
        osoba_username.setText(p.getUsername());



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        /*
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Odaberite opciju");
        builder.setMessage("Jeste li sigurni da želite izaći?");
        builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.show();
        */

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        FragmentManager fm=getFragmentManager();
        int id = item.getItemId();

        if (id == R.id.add_subject) {
            fm.beginTransaction().replace(R.id.content_frame,new PregledPredmeta()).commit();
        }else if (id == R.id.add_student) {
            fm.beginTransaction().replace(R.id.content_frame,new DodajStudente()).commit();
        }else if (id == R.id.view_student) {
            fm.beginTransaction().replace(R.id.content_frame,new PregledStudenata()).commit();
        }else if (id == R.id.prof_odjava) {
            AlertDialog.Builder builder=new AlertDialog.Builder(this);
            builder.setMessage("Odaberite opciju");
            builder.setMessage("Jeste li sigurni da se  želite odjaviti?");
            builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    startActivity(new Intent(Navi_main.this, MainActivity.class));
                    Toast.makeText(getApplicationContext(),"Uspiješno ste se odjavili!!",Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
            builder.show();
        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
