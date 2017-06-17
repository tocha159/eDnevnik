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
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.Model.ZakljucnaOcjena;
import com.examples.your.ednevnik.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StudentPregledPredmeta extends android.app.Fragment{

    ListView pregled_predmeta;
    SharedPreferences prefs;
    PregledPredmetaAdapter adapter;

    public StudentPregledPredmeta() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_student_pregled_predmeta, container, false);
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pregled_predmeta= (ListView) v.findViewById(R.id.pregled_predmeta);
        adapter=new PregledPredmetaAdapter(getActivity(),R.layout.predmeti_student_list, Razred.find(Razred.class,"student = ?",String.valueOf(prefs.getLong("studid", 1))));
        pregled_predmeta.setAdapter(adapter);

        return v;
    }

    private class PregledPredmetaAdapter extends ArrayAdapter<Razred>{
        int resource;
        List<Razred> razredi;

        public PregledPredmetaAdapter(Context context, int resource, List<Razred> objects) {
            super(context, resource, objects);
            this.resource=resource;
            this.razredi=objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView==null){
                convertView=LayoutInflater.from(getContext()).inflate(resource,null);
            }
            Razred razred=razredi.get(position);
            Predmet predmet=Predmet.findById(Predmet.class,razred.getPredmet().getId());


            TextView naziv_predmeta= (TextView) convertView.findViewById(R.id.naziv_predmeta);
            TextView image_text= (TextView) convertView.findViewById(R.id.image_text);
            ImageView predmet_color= (ImageView) convertView.findViewById(R.id.predmet_color);
            TextView predmet_ocjena= (TextView) convertView.findViewById(R.id.predmet_ocjena);
            TextView predmet_status= (TextView) convertView.findViewById(R.id.predmet_status);
            TextView profesor_naziv= (TextView) convertView.findViewById(R.id.profesor_naziv);

            naziv_predmeta.setText(predmet.getNaziv_predmeta());
            image_text.setText(String.valueOf(predmet.getNaziv_predmeta().charAt(0)));
            predmet_color.setBackgroundColor(Color.parseColor(predmet.getBoja()));
            profesor_naziv.setText("prof."+predmet.getProfesor().getName()+" "+predmet.getProfesor().getSurname());

            //ZakljucnaOcjena ocjena=null;
            List<ZakljucnaOcjena> zak=ZakljucnaOcjena.find(ZakljucnaOcjena.class,"predmet = ? and student = ?",String.valueOf(predmet.getId()),String.valueOf(prefs.getLong("studid", 1)));

            if (zak.isEmpty()) {
                predmet_status.setText("Aktivan");
                predmet_ocjena.setText("");
            }
            else{
                predmet_status.setText("Zaključen");
                predmet_ocjena.setText(String.valueOf(zak.get(0).getZakljucna()));
            }
            return convertView;
        }
    }

}
