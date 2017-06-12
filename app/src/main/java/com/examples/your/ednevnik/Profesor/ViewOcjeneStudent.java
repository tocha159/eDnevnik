package com.examples.your.ednevnik.Profesor;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Ocjena;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.orm.SugarContext;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * Created by PINJUH on 11.6.2017..
 */

public class ViewOcjeneStudent extends AppCompatActivity {
    //TextView ocjena_info_ime;
    //TextView ocjena_info_prezime;
   // TextView ocjena_info_korisnickoime;
    //TextView ocjena_info_predmet;
    Toolbar toolbar;
    ImageView student_avatar;
    Ocjene_recycler_adapter adapter;
    Student s;
    Predmet p;
    RecyclerView ocjena_student2;
    ListView ocjena_student;
    OcjeneAdapter adapter2;
    TextView predmet_ime;

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
        setContentView(R.layout.activity_view_ocjene_student);
        init();
        properties();


    }
/*
    @Override
    public void onBackPressed() {

    }
    */

    public void init(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        SugarContext.init(this);
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        predmet_ime= (TextView) findViewById(R.id.predmet_ime);
       // ocjena_info_ime= (TextView) findViewById(R.id.ocjena_info_ime);
        //ocjena_info_prezime= (TextView) findViewById(R.id.ocjena_info_prezime);
       // ocjena_info_korisnickoime= (TextView) findViewById(R.id.ocjena_info_korisnickoime);
       // ocjena_info_predmet= (TextView) findViewById(R.id.ocjena_info_predmet);
        student_avatar= (ImageView) findViewById(R.id.student_avatar);
        ocjena_student= (ListView) findViewById(R.id.ocjene_student);
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

        //ocjena_info_ime.setText(s.getName());
        //ocjena_info_prezime.setText(s.getSurname());
       // ocjena_info_korisnickoime.setText(s.getUsername());
       // ocjena_info_predmet.setText(p.getNaziv_predmeta());

        //adapter=new Ocjene_recycler_adapter(Ocjena.find(Ocjena.class,"ucenik = ? and predmet = ?",String.valueOf(prefs.getLong("id_student_info",1)),String.valueOf(prefs.getLong("id_predmet_info",1))));
       // adapter=new Ocjene_recycler_adapter(Ocjena.listAll(Ocjena.class));
        predmet_ime.setText(p.getNaziv_predmeta());
        adapter2=new OcjeneAdapter(this,R.layout.ocjene_lista,Ocjena.find(Ocjena.class,"ucenik = ? and predmet = ?",String.valueOf(prefs.getLong("id_student_info",1)),String.valueOf(prefs.getLong("id_predmet_info",1))));

        ocjena_student.setAdapter(adapter2);

    }
    public  void properties(){
        ocjena_student.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Ocjena o = adapter2.getItem(position);
                Intent i=new Intent(ViewOcjeneStudent.this,UrediObrisiOcjenu.class);
                i.putExtra("id_ocjena",o.getId());
                i.putExtra("datum_ocjena",o.getDatum());
                i.putExtra("tip_ocjena",o.getTip());
                i.putExtra("_ocjena",o.getOcjena());
                i.putExtra("napomena_ocjena",o.getNapomena());
                startActivity(i);
                finish();
            }
        });

    }



    public class Ocjene_recycler_adapter extends RecyclerView.Adapter<Ocjene_recycler_adapter.MyViewHolder>{
        public List<Ocjena> ocjenaList;


        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tip_ocjene, datum_ocjene,napomena_ocjene, ocjena_predmet;

            public MyViewHolder(View view) {
                super(view);
                tip_ocjene= (TextView) view.findViewById(R.id.tip_ocjene);
                datum_ocjene= (TextView) view.findViewById(R.id.datum_ocjene);
                napomena_ocjene= (TextView) view.findViewById(R.id.napomena_ocjene);
                ocjena_predmet= (TextView) view.findViewById(R.id.ocjena_predmet);


            }
        }

        public Ocjene_recycler_adapter(List<Ocjena> ocjenaList) {
            this.ocjenaList = ocjenaList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.ocjene_lista, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            Ocjena ocjena=ocjenaList.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ocjena.getDatum());

            holder.ocjena_predmet.setText(String.valueOf(ocjena.getOcjena()));
            holder.tip_ocjene.setText(ocjena.getTip());
            holder.datum_ocjene.setText(new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime()));
            //holder.napomena_ocjene.setText(ocjena.getNapomena());

        }

        @Override
        public int getItemCount() {
           return ocjenaList.size();
        }

    }
    private class OcjeneAdapter extends ArrayAdapter<Ocjena> {
        private int resource;
        private List<Ocjena> ocjene;

        public OcjeneAdapter(Context context, int resource, List<Ocjena> objects) {
            super(context, resource, objects);
            this.resource = resource;
            ocjene = objects;


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Ocjena ocjena = ocjene.get(position);
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(ocjena.getDatum());

            TextView tip_ocjene, datum_ocjene, napomena_ocjene, ocjena_predmet,predmet_prikaz;
            tip_ocjene = (TextView) convertView.findViewById(R.id.tip_ocjene);
            datum_ocjene = (TextView) convertView.findViewById(R.id.datum_ocjene);
            napomena_ocjene = (TextView) convertView.findViewById(R.id.napomena_ocjene);
            ocjena_predmet = (TextView) convertView.findViewById(R.id.ocjena_predmet);


            ocjena_predmet.setText(String.valueOf(ocjena.getOcjena()));
            tip_ocjene.setText(ocjena.getTip());
            datum_ocjene.setText("Datum: "+new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime()));
            napomena_ocjene.setText(ocjena.getNapomena());

            return convertView;
        }
    }
}
