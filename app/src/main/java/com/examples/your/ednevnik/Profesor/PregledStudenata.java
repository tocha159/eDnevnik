package com.examples.your.ednevnik.Profesor;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PregledStudenata extends android.app.Fragment {
    EditText ucenik_input;
    Spinner predmet_odb;
    ListView studenti_get;
    SpinnerAdapter spinnerAdapter;
    StudentAdapter studentAdapter;
    SharedPreferences prefs;



    public PregledStudenata() {
        // Required empty public constructor
    }

    public void init(View v){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ucenik_input= (EditText) v.findViewById(R.id.ucenik_input);
        predmet_odb= (Spinner) v.findViewById(R.id.predmet_odb);
        studenti_get= (ListView) v.findViewById(R.id.studenti_get);
        new GetAll().execute();
    }
    public void properties(){
        predmet_odb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentAdapter=new StudentAdapter(getActivity(),R.layout.studenti_customlist_all,Razred.find(Razred.class,"predmet = ?",String.valueOf(spinnerAdapter.getItem(position).getId())));
                studenti_get.setAdapter(studentAdapter);
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
        View v=inflater.inflate(R.layout.fragment_pregled_studenata, container, false);
        getActivity().setTitle("Pregled učenika");
        init(v);
        properties();

        return v;

    }
    private class SpinnerAdapter extends ArrayAdapter<Predmet> {
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
    private class StudentAdapter extends ArrayAdapter<Razred>{
        private int resource;
        private List<Razred> razredi;

        public StudentAdapter(Context context, int resource, List<Razred> objects) {
            super(context,resource,objects);
            this.resource=resource;
            razredi=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Razred razred=razredi.get(position);

            ImageView student_avatar= (ImageView) convertView.findViewById(R.id.student_avatar);
            TextView student_info= (TextView) convertView.findViewById(R.id.student_info);
            TextView student_username= (TextView) convertView.findViewById(R.id.student_username);
            ImageButton student_settigs= (ImageButton) convertView.findViewById(R.id.odabrani_settings);

            Student student=Student.findById(Student.class,razred.getStudent().getId());


            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Dnevnik/"+student.getUsername()+".jpg");
            Picasso.with(getActivity()).load(file).into(student_avatar);


            student_info.setText(student.getName()+" "+student.getSurname());
            student_username.setText(student.getUsername());

            student_settigs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(),"bravoo",Toast.LENGTH_SHORT).show();
                }
            });

            return convertView;

        }
    }
    private class GetAll extends AsyncTask<Void,Void,Void> {
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
            spinnerAdapter=new SpinnerAdapter(getActivity(),R.layout.predmeti_customlist,Predmet.find(Predmet.class,"profesor = ?",String.valueOf(prefs.getLong("profid", 1))));
            studentAdapter=new StudentAdapter(getActivity(),R.layout.studenti_customlist_all,Razred.listAll(Razred.class));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            predmet_odb.setAdapter(spinnerAdapter);
            studenti_get.setAdapter(studentAdapter);

        }
    }

}
