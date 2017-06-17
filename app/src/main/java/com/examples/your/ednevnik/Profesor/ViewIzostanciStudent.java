package com.examples.your.ednevnik.Profesor;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Izostanak;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.examples.your.ednevnik.Ucenik.NonScrollListView;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class ViewIzostanciStudent extends AppCompatActivity {
    Toolbar toolbar;
    ImageView student_avatar;
    Student s;
    Predmet p;
    NonScrollListView izostanci_student;
    IzostanciAdapter adapter;
    TextView student_podaci;
    TextView predmet_podaci;

    SharedPreferences prefs;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_izostanci_student);
        init();
        properties();
    }
    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        SugarContext.init(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        student_podaci= (TextView) findViewById(R.id.student_podaci);
        predmet_podaci= (TextView) findViewById(R.id.predmet_podaci);
        student_avatar= (ImageView) findViewById(R.id.student_avatar);
        izostanci_student= (NonScrollListView) findViewById(R.id.izostanci_student);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        s=Student.findById(Student.class,prefs.getLong("id_student_info",1));
        p=Predmet.findById(Predmet.class, prefs.getLong("id_predmet_info", 1));


        File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Dnevnik/"+s.getUsername()+".jpg");
        try{
            Picasso.with(this).load(file).into(student_avatar);
        }
        catch (Exception e){
            Picasso.with(this).load(R.drawable.logo).into(student_avatar);

        }
        student_podaci.setText(s.getName()+" "+s.getSurname());
        predmet_podaci.setText(p.getNaziv_predmeta());

        adapter=new IzostanciAdapter(this,R.layout.izostanci_lista, Izostanak.find(Izostanak.class,"ucenik = ? and predmet = ?",String.valueOf(prefs.getLong("id_student_info",1)),String.valueOf(prefs.getLong("id_predmet_info",1))));
        izostanci_student.setAdapter(adapter);

        TextView textView = new TextView(this);
        textView.setText("Pregled izostanaka");
        textView.setTextSize(20);
        textView.setTextColor(Color.BLACK);
        textView.setTypeface(null, Typeface.BOLD);



        izostanci_student.addHeaderView(textView,null,false);

    }
    public  void properties(){
        izostanci_student.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(ViewIzostanciStudent.this);
                builder.setTitle("Odaberite akciju");
                builder.setMessage("Jeste li sigurni da želite ukloniti izostanak za ovog studenta");
                builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Izostanak iz= (Izostanak) parent.getItemAtPosition(position);
                        iz.delete();
                        adapter.remove(iz);
                        izostanci_student.setAdapter(adapter);
                    }
                });
                builder.show();
                return true;

            }
        });
    }
    private class IzostanciAdapter extends ArrayAdapter<Izostanak> {
        private int resource;
        private List<Izostanak> izostanci;

        public IzostanciAdapter(Context context, int resource, List<Izostanak> objects) {
            super(context, resource, objects);
            this.resource = resource;
            izostanci = objects;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Izostanak izostanak = izostanci.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(izostanak.getDatum());

            TextView napomena_izostanka, datum_izostanka,brojac_ocjena;
            napomena_izostanka = (TextView) convertView.findViewById(R.id.napomena_izostanka);
            datum_izostanka = (TextView) convertView.findViewById(R.id.datum_izostanka);
            brojac_ocjena = (TextView) convertView.findViewById(R.id.brojac_ocjena);

            brojac_ocjena.setText(String.valueOf(position+1)+".");
            napomena_izostanka.setText(izostanak.getNapomena());
            datum_izostanka.setText("Datum: "+new SimpleDateFormat("yyy-MM-dd ").format(calendar.getTime()));


            return convertView;
        }
    }
}
