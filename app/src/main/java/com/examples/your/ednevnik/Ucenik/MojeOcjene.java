package com.examples.your.ednevnik.Ucenik;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Ocjena;
import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Professor;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MojeOcjene extends android.app.Fragment {
    Spinner predmet_odabrani;
    ListView moje_ocjene;
    SharedPreferences prefs;
    SpinnerAdapter spinnerAdapter;
    DajOcjene ocjene_adapter;



    public MojeOcjene() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_moje_ocjene, container, false);
        getActivity().setTitle("Moje ocjene");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        predmet_odabrani= (Spinner) v.findViewById(R.id.predmet_odabrani);
        moje_ocjene= (ListView) v.findViewById(R.id.moje_ocjene);

        spinnerAdapter=new SpinnerAdapter(getActivity(),R.layout.predmeti_customlist_student,Razred.find(Razred.class,"student = ?",String.valueOf(prefs.getLong("studid", 1))));
        predmet_odabrani.setAdapter(spinnerAdapter);
        Razred r= (Razred) predmet_odabrani.getSelectedItem();
        //ocjene_adapter=new DajOcjene(getActivity(),R.layout.moje_ocjene_list,Ocjena.find(Ocjena.class,"predmet = ? and ucenik = ?",String.valueOf(r.getPredmet().getId()),String.valueOf(r.getStudent().getId())));
       // moje_ocjene.setAdapter(ocjene_adapter);


        predmet_odabrani.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ocjene_adapter=new DajOcjene(getActivity(),R.layout.moje_ocjene_list,Ocjena.find(Ocjena.class,"predmet = ? and ucenik = ?",String.valueOf(spinnerAdapter.getItem(position).getPredmet().getId()),String.valueOf(spinnerAdapter.getItem(position).getStudent().getId())));
                moje_ocjene.setAdapter(ocjene_adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        return v;
    }
    private class SpinnerAdapter extends ArrayAdapter<Razred> {
        private int resource;
        private List<Razred> razredi;

        public SpinnerAdapter(Context context, int resource, List<Razred> objects) {
            super(context, resource, objects);
            this.resource=resource;
            razredi=objects;
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
            Razred razred=razredi.get(position);
            Predmet predmet=Predmet.findById(Predmet.class,razred.getPredmet().getId());
            Professor profesor=Professor.findById(Professor.class,predmet.getProfesor().getId());
            TextView naziv_profesora= (TextView) convertView.findViewById(R.id.naziv_profesora);

            TextView naziv_predmeta= (TextView) convertView.findViewById(R.id.naziv_predmeta);
            TextView image_text= (TextView) convertView.findViewById(R.id.image_text);
            ImageView predmet_color= (ImageView) convertView.findViewById(R.id.predmet_color);

            naziv_predmeta.setText(predmet.getNaziv_predmeta());
            image_text.setText(String.valueOf(predmet.getNaziv_predmeta().charAt(0)));
            predmet_color.setBackgroundColor(Color.parseColor(predmet.getBoja()));
            naziv_profesora.setText("prof."+profesor.getName()+" "+profesor.getSurname());
            return convertView;
        }
    }
    private class DajOcjene extends ArrayAdapter<Ocjena>{
        int resource;
        List<Ocjena> ocjene;

        public DajOcjene(Context context, int resource, List<Ocjena> objects) {
            super(context, resource, objects);
            this.resource=resource;
            this.ocjene=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView==null)
                convertView=LayoutInflater.from(getContext()).inflate(resource,null);

            Ocjena o=ocjene.get(position);
            TextView ocjena_tip= (TextView) convertView.findViewById(R.id.ocjena_tip);
            TextView ocjena= (TextView) convertView.findViewById(R.id.ocjena);
            TextView datum= (TextView) convertView.findViewById(R.id.datum);


            ocjena_tip.setText(o.getTip());
            ocjena.setText(String.valueOf(o.getOcjena()));

            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(o.getDatum());
            datum.setText(new SimpleDateFormat("yyy-MM-dd").format(calendar.getTime()));

            return convertView;

        }
    }

}
