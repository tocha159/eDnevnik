package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import com.examples.your.ednevnik.R;

public class Navi_main extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToggle;
    Toolbar toolbar;
    NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navi_main);
        mDrawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        mToggle=new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open,R.string.close);

        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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

        }else if (id == R.id.id_zakljuci) {

        }
        mDrawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }
}
