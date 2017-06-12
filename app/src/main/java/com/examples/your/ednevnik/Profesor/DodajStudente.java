package com.examples.your.ednevnik.Profesor;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class DodajStudente extends android.app.Fragment {
    Spinner predmet_odabrani;
    ListView studenti_reg;
    SpinnerAdapter spiner_adapter;
    SharedPreferences prefs;
    Adapter adapter;
    List<Student>odabrani;
    Predmet predmet_get;


    public DodajStudente() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
       inflater.inflate(R.menu.context_menu_list_selected,menu);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case(R.id.add_item):

                if(!odabrani.isEmpty()&&(!spiner_adapter.isEmpty())){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setTitle("Potvrdite");
                    builder.setMessage("Jeste li sigurni da želite dodati "+odabrani.size()+" učenika "+"u predmet "+predmet_get.getNaziv_predmeta()+"?");

                    builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                                new SpasiUcenike().execute();
                        }
                    });

                    builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
                else{
                    Toast.makeText(getActivity(),"Niste odabrarli ni jednog učenika ili niste dodali ni jedan predmet",Toast.LENGTH_LONG).show();
                }
                break;
        }
        return true;
    }

    public void init(View v){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        predmet_odabrani= (Spinner) v.findViewById(R.id.predmet_odabrani);
        studenti_reg= (ListView) v.findViewById(R.id.studenti_reg);
        studenti_reg.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        odabrani=new ArrayList<>();

    }
    public void properties(){
        new GetAll().execute();

        /*
        studenti_reg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                //Toast.makeText(getActivity(),"radii",Toast.LENGTH_SHORT).show();
                RelativeLayout lin= (RelativeLayout) studenti_reg.getChildAt(position);
                CheckBox cb= (CheckBox) lin.findViewById(R.id.odabrani_student);
                if(!cb.isChecked()){
                    cb.setChecked(true);
                    odabrani.add(adapter.getItem(position));
                }
                else {
                    cb.setChecked(false);
                    odabrani.remove(adapter.getItem(position));
                }

            }
        });
        */
        /*
        studenti_reg.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CheckBox cb= (CheckBox) view.findViewById(R.id.odabrani_student);
                if(!cb.isChecked()){
                    cb.setChecked(true);
                    odabrani.add(adapter.getItem(position));
                }
                else {
                    cb.setChecked(false);
                    odabrani.remove(adapter.getItem(position));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */



        predmet_odabrani.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //predmet_get=Predmet.findById(Predmet.class,adapter.getItem(position).getId());
                predmet_get=spiner_adapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_dodaj_studente, container, false);
        init(v);
        properties();
        setHasOptionsMenu(true);
        getActivity().setTitle("Dodavanje učenika");

        return v;
    }

    private class Adapter extends ArrayAdapter<Student>{
        private int resource;
        private List<Student> studenti;
        private List<Boolean>oznacen=new ArrayList<>();


        public Adapter(Context context, int resource, List<Student> objects) {
            super(context, resource, objects);
            this.resource=resource;
            studenti=objects;
            for(int i=0;i<studenti.size();i++){
                oznacen.add(false);
            }
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Student student=studenti.get(position);
            CircularImageView student_avatar= (CircularImageView) convertView.findViewById(R.id.student_avatar);
            TextView student_info= (TextView) convertView.findViewById(R.id.student_info);
            TextView student_username= (TextView) convertView.findViewById(R.id.student_username);
            final CheckBox cb= (CheckBox) convertView.findViewById(R.id.odabrani_student);

            if(oznacen.get(position)){
                cb.setChecked(true);
            }
            else{
                cb.setChecked(false);
            }
            cb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(cb.isChecked()){
                        oznacen.set(position,true);
                        odabrani.add(adapter.getItem(position));
                    }
                    else {
                        oznacen.set(position,false);
                        odabrani.remove(adapter.getItem(position));
                    }
                }
            });
            //File f = new File("/Dnevnik/"+ student.getUsername()+".jpg");
            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Dnevnik/"+student.getUsername()+".jpg");
            try{
                Picasso.with(getActivity()).load(file).into(student_avatar);
            }
            catch (Exception e){
                Picasso.with(getActivity()).load(student.getPicture()).into(student_avatar);

            }
            student_info.setText(student.getName()+" "+student.getSurname());
            student_username.setText(student.getUsername());

            /*
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    cb.setChecked(isChecked);
                    if(isChecked){
                        cb.setChecked(true);
                        odabrani.add(adapter.getItem(position));
                    }
                    else {
                        cb.setChecked(false);
                        odabrani.remove(adapter.getItem(position));
                    }
                }
            });
            */

            return convertView;

        }


    }

    private class SpinnerAdapter extends ArrayAdapter<Predmet>{
        private int resource;
        private List<Predmet> predmeti;

        public SpinnerAdapter(Context context, int resource, List<Predmet> objects) {
            super(context, resource, objects);
            this.resource=resource;
            predmeti=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position,convertView,parent);

        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position,convertView,parent);
        }
        public View getCustomView(int position,View convertView, ViewGroup parent){
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Predmet predmet=predmeti.get(position);

            TextView naziv_predmeta= (TextView) convertView.findViewById(R.id.naziv_predmeta);
            TextView image_text= (TextView) convertView.findViewById(R.id.image_text);
            ImageView predmet_color= (ImageView) convertView.findViewById(R.id.predmet_color);

            naziv_predmeta.setText(predmet.getNaziv_predmeta());
            image_text.setText(String.valueOf(predmet.getNaziv_predmeta().charAt(0)));
            predmet_color.setBackgroundColor(Color.parseColor(predmet.getBoja()));
            return convertView;

        }
    }
    private class GetAll extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {

            pDialog.setMessage("Molimo pričekajte ažuriram podatke...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            spiner_adapter=new SpinnerAdapter(getActivity(),R.layout.predmeti_customlist,Predmet.find(Predmet.class,"profesor = ?",String.valueOf(prefs.getLong("profid", 1))));
            adapter=new Adapter(getActivity(),R.layout.studenti_customlist,Student.listAll(Student.class));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            predmet_odabrani.setAdapter(spiner_adapter);
            studenti_reg.setAdapter(adapter);

        }
    }
    private class SpasiUcenike extends AsyncTask<Void,Void,Void>{
        ProgressDialog pDialog=new ProgressDialog(getActivity());
        boolean duplikat=false;
        @Override
        protected void onPreExecute() {
            pDialog.setMessage("Molimo pričekajte...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for (Student s:odabrani){
                Razred razred=new Razred(predmet_get,s);
                if(Razred.find(Razred.class,"predmet = ? and student = ?",String.valueOf(predmet_get.getId()),String.valueOf(s.getId())).isEmpty()){
                    razred.save();
                }
                else{
                    duplikat=true;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            if(duplikat)
                Toast.makeText(getActivity(),"Uspiješno ste dodali učenike,duplikati su zanemareni",Toast.LENGTH_LONG).show();
            else
                Toast.makeText(getActivity(),"Upsiješno ste dodali učenike",Toast.LENGTH_LONG).show();
        }
    }

}
