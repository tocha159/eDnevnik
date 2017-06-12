package com.examples.your.ednevnik.Profesor;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import com.examples.your.ednevnik.Model.Ocjena;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.Model.Student;
import com.examples.your.ednevnik.Model.ZakljucnaOcjena;
import com.examples.your.ednevnik.R;
import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
    AlertDialog.Builder builderSingle;




    public PregledStudenata() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.zakljuci_ocjenu,menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.ocjena_zakljuci:
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Zaključivanje ocjene");
                builder.setMessage("Jeste li sigurni da želite automatski zaključiti ocjene?");
                builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        new ZakljuciOcjene().execute();
                    }
                });
                builder.show();
                break;
            case R.id.ocjena_ponisti:
                AlertDialog.Builder builder2=new AlertDialog.Builder(getActivity());
                builder2.setTitle("Poništite zaključne ocjene");
                builder2.setMessage("Jeste li sigurni da želite poništiti zaključne ocjene?");
                builder2.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder2.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for(int i=0;i<studentAdapter.getCount();i++) {
                            Razred r = studentAdapter.getItem(i);
                            ZakljucnaOcjena.deleteAll(ZakljucnaOcjena.class, "predmet = ? and student = ?", String.valueOf(r.getPredmet().getId()), String.valueOf(r.getStudent().getId()));
                        }
                        Toast.makeText(getActivity(),"Uspiješno ste obrisali zaključne ocjene!!",Toast.LENGTH_LONG).show();
                        studenti_get.setAdapter(studentAdapter);
                    }
                });
                builder2.show();

                break;
        }
        return true;
    }

    public void init(View v){
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        ucenik_input= (EditText) v.findViewById(R.id.ucenik_input);
        predmet_odb= (Spinner) v.findViewById(R.id.predmet_odb);
        studenti_get= (ListView) v.findViewById(R.id.studenti_get);
        new GetAll().execute();
        builderSingle = new AlertDialog.Builder(getActivity());
        builderSingle.setTitle("Odaberite opciju");

        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        arrayAdapter.add("Vidi ocjene");
        arrayAdapter.add("Vidi izostanke");
        arrayAdapter.add("Ocjeni");
        arrayAdapter.add("Zapiši izostanak");

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // vidi ocjene
                        startActivity(new Intent(getActivity(),ViewOcjeneStudent.class));
                        break;
                    case 1: // vidi izostanke
                        startActivity(new Intent(getActivity(),ViewIzostanciStudent.class));
                        break;
                    case 2: // ocjeni
                        startActivity(new Intent(getActivity(),DodajOcijenu.class));
                        break;
                    case 3: // zapisi izostanak
                        startActivity(new Intent(getActivity(),DodajIzostanak.class));
                        break;

                }
            }
        });
    }
    public void properties(){
        predmet_odb.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentAdapter=new StudentAdapter(getActivity(),R.layout.studenti_customlist_all,Razred.find(Razred.class,"predmet = ?",String.valueOf(spinnerAdapter.getItem(position).getId())));
                studenti_get.setAdapter(studentAdapter);
                studentAdapter.filter(ucenik_input.getText().toString());

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
       ucenik_input.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {
               studentAdapter.filter(s.toString());

           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       });
        studenti_get.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Predmet p= (Predmet) predmet_odb.getSelectedItem();
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Odaberite opciju");
                builder.setMessage("Jeste li sigurno da želite ukloniti učenika iz predmeta "+p.getNaziv_predmeta()+"?");

                builder.setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Razred razred=studentAdapter.getItem(position);
                        try {
                            Razred r = Razred.findById(Razred.class,razred.getId());
                            r.delete();
                            Toast.makeText(getActivity(),"Uspiješno ste obrisali učenika",Toast.LENGTH_SHORT).show();
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"Ne možete obrisati učenika jer postoje zapisi(ocjene, izostanci)",Toast.LENGTH_SHORT).show();
                        }
                        studentAdapter.remove(razred);
                        studentAdapter.filter.remove(razred);
                        studenti_get.setAdapter(studentAdapter);

                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pregled_studenata, container, false);
        setHasOptionsMenu(true);
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
        private List<Razred>filter;

        public StudentAdapter(Context context, int resource, List<Razred> objects) {
            super(context,resource,objects);
            this.resource=resource;
            razredi=objects;
            filter=new ArrayList<>();
            filter.addAll(this.razredi);

        }
        public void filter (String text){
            text=text.toLowerCase(Locale.getDefault());
            razredi.clear();
            if(text.length()==0){
                razredi.addAll(filter);
            }
            else {
                for (Razred r:filter) {
                    if(r.getStudent().getSurname().toLowerCase(Locale.getDefault()).contains(text)){
                        razredi.add(r);
                    }
                }
            }
            notifyDataSetChanged();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(resource, null);
            }
            Razred razred=razredi.get(position);

            CircularImageView student_avatar= (CircularImageView) convertView.findViewById(R.id.student_avatar);
            TextView student_info= (TextView) convertView.findViewById(R.id.student_info);
            TextView student_username= (TextView) convertView.findViewById(R.id.student_username);
            ImageButton student_settigs= (ImageButton) convertView.findViewById(R.id.odabrani_settings);
            TextView student_zaključna= (TextView) convertView.findViewById(R.id.student_zaključna);

            final Student student=Student.findById(Student.class,razred.getStudent().getId());


            File file = new File(Environment.getExternalStorageDirectory().getPath() +"/Dnevnik/"+student.getUsername()+".jpg");
            try{
                Picasso.with(getActivity()).load(file).into(student_avatar);
            }
            catch (Exception e){
                Picasso.with(getActivity()).load(student.getPicture()).into(student_avatar);

            }

            List<ZakljucnaOcjena> pok=ZakljucnaOcjena.find(ZakljucnaOcjena.class,"predmet = ? and student = ?", String.valueOf(razred.getPredmet().getId()), String.valueOf(razred.getStudent().getId()));
            if(pok.size()>0)
                student_zaključna.setText("Zaključna ocjena: "+ pok.get(0).getZakljucna());
            else
                student_zaključna.setText("");

            student_info.setText(student.getName()+" "+student.getSurname());
            student_username.setText(student.getUsername());

            student_settigs.setTag(position);
            student_settigs.setOnClickListener(mMyButtonClickListener);

            return convertView;
        }
        private View.OnClickListener mMyButtonClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();

                prefs.edit().putLong("id_student_info",razredi.get(position).getStudent().getId()).commit();

                prefs.edit().putLong("id_predmet_info",razredi.get(position).getPredmet().getId()).commit();


                builderSingle.show();

            }
        };
    }
    public float dajProsjek(List<Ocjena>ocjena){
        int suma=0;
        for(Ocjena o:ocjena){
            suma+=o.getOcjena();
        }
        return (float) suma/ocjena.size();
    }
    public int dajOcjenu(float prosjek){
        if(Float.compare(prosjek,1.5f)< 0)
            return 1;
        else if((Float.compare(prosjek,1.5f)>=0)&&((Float.compare(prosjek,2.5f)<0)))
            return 2;
        else if((Float.compare(prosjek,2.5f)>=0)&&((Float.compare(prosjek,3.5f)<0)))
            return 3;
        else if((Float.compare(prosjek,3.5f)>=0)&&((Float.compare(prosjek,4.5f)<0)))
            return 4;
        else
            return 5;

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
            //studentAdapter=new StudentAdapter(getActivity(),R.layout.studenti_customlist_all,Razred.listAll(Razred.class));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            predmet_odb.setAdapter(spinnerAdapter);
            //studenti_get.setAdapter(studentAdapter);

        }
    }
    private class ZakljuciOcjene extends AsyncTask<Void,Void,Void> {
        ProgressDialog pDialog=new ProgressDialog(getActivity());
        @Override
        protected void onPreExecute() {

            pDialog.setMessage("Molimo pričekajte zaključujem ocjene...");
            pDialog.setCancelable(false);
            pDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            for(int i=0;i<studentAdapter.getCount();i++){
                Razred r=studentAdapter.getItem(i);
                //Student s=Student.findById(Student.class, r.getStudent().getId());
                //Predmet p=Predmet.findById(Predmet.class, r.getPredmet().getId());
                ZakljucnaOcjena.deleteAll(ZakljucnaOcjena.class,"predmet = ? and student = ?", String.valueOf(r.getPredmet().getId()), String.valueOf(r.getStudent().getId()));
                List<Ocjena>ocjena=Ocjena.find(Ocjena.class,"ucenik = ? and predmet = ?",String.valueOf(r.getStudent().getId()),String.valueOf(r.getPredmet().getId()));
                if(!ocjena.isEmpty()){
                    ZakljucnaOcjena zakljucna=new ZakljucnaOcjena(dajOcjenu(dajProsjek(ocjena)),r.getPredmet(),r.getStudent());
                    zakljucna.save();
                }
                else {
                    ZakljucnaOcjena zakljucna=new ZakljucnaOcjena(1,r.getPredmet(),r.getStudent());
                    zakljucna.save();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            studenti_get.setAdapter(studentAdapter);
            if (pDialog.isShowing()){
                pDialog.dismiss();
            }
            Toast.makeText(getActivity(),"Uspiješno ste zaključili ocjene!!",Toast.LENGTH_LONG).show();


        }
    }

}
