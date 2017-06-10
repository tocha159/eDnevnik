package com.examples.your.ednevnik.Profesor;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.examples.your.ednevnik.Model.Predmet;
import com.examples.your.ednevnik.Model.Razred;
import com.examples.your.ednevnik.R;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class PregledPredmeta extends android.app.Fragment {
    ListView pregled_predmeta;
    FloatingActionButton predmet_add;
    Adapter adapter;
    SharedPreferences prefs;


    public PregledPredmeta() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_pregled_predmeta, container, false);
        getActivity().setTitle("Dodavanje predmeta");
        prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        pregled_predmeta= (ListView) v.findViewById(R.id.pregled_predmeta);
        predmet_add= (FloatingActionButton) v.findViewById(R.id.predmet_add);
        adapter=new Adapter(getActivity(),R.layout.predmeti_customlist,Predmet.find(Predmet.class,"profesor = ?",String.valueOf(prefs.getLong("profid", 1))));
       /*
        List<Predmet>bb=new ArrayList<>();
        new Professor("Marko","Maric","aaa","bbb").save();
        bb.add(new Predmet("Matematika","#147854",Professor.first(Professor.class)));
        bb.add(new Predmet("Biologija","#174854",Professor.first(Professor.class)));
        bb.add(new Predmet("Kemija","#167854",Professor.first(Professor.class)));
        bb.add(new Predmet("Fizika","#147324",Professor.first(Professor.class)));
        adapter=new Adapter(getActivity(),R.layout.predmeti_customlist,bb);
        */
        pregled_predmeta.setAdapter(adapter);


        predmet_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getActivity(),Dodaj_predmet.class);
                startActivityForResult(i,2);
                //startActivity(new Intent(getActivity(),Dodaj_predmet.class));

            }
        });
        pregled_predmeta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
                builder.setTitle("Obrišite");
                builder.setMessage("Brisanjem predmeta "+adapter.getItem(position).getNaziv_predmeta()+" obrisat će te sve informacije,uključujući i učenike za taj predmet");

                builder.setPositiveButton("DA", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        try {
                            //prvo obrisati razrede zbog stranog kljuca, a tek onda predmete
                            for (Razred raz : Razred.find(Razred.class, "predmet = ?", String.valueOf(adapter.getItem(position).getId()))) {
                                raz.delete();
                            }
                            Predmet p = Predmet.findById(Predmet.class, adapter.getItem(position).getId());
                            p.delete();
                            Toast.makeText(getActivity(),"Uspiješno ste obrisali predmet "+ adapter.getItem(position).getNaziv_predmeta(),Toast.LENGTH_LONG).show();

                            adapter.remove(adapter.getItem(position));
                            adapter.notifyDataSetChanged();
                        }catch (Exception e){
                            Toast.makeText(getActivity(),"Dogodila se neočekivana greška: "+e.getMessage(),Toast.LENGTH_LONG).show();

                        }
                    }

                });

                builder.setNegativeButton("NE", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.show();
                return true;
            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((resultCode == 20)&&(requestCode==2)){
            adapter=new Adapter(getActivity(),R.layout.predmeti_customlist,Predmet.find(Predmet.class,"profesor = ?",String.valueOf(prefs.getLong("profid", 1))));
            pregled_predmeta.setAdapter(adapter);
        }
    }

    public class Adapter extends ArrayAdapter<Predmet>{
        private int resource;
        private List<Predmet> predmeti;


        public Adapter(Context context, int resource, List<Predmet> objects) {
            super(context, resource, objects);
            this.resource=resource;
            predmeti=objects;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
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

}
